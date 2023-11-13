package com.orgo.feature.map.ui.mapview

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.orgo.core.common.util.getCurrentLocation
import com.orgo.core.common.util.getTestLocation
import net.daum.mf.map.api.MapView
import com.orgo.core.designsystem.animation.OrgoAnimation
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.core.ui.component.ErrorScreen
import com.orgo.feature.map.ui.bottomsheet.MapModalBottomSheet
import com.orgo.feature.map.ui.search.MapSearchBar
import com.orgo.feature.map.ui.search.SearchScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.system.exitProcess

@ExperimentalMaterial3Api
@Composable
internal fun MapRoute(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onDetailClicked: (Int) -> Unit,
    navigateToComplete: () -> Unit,
    onSearchScreenShowed: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bottomSheetState = rememberSheetState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val selectedMountain =
        if (uiState is MapUiState.Map) (uiState as MapUiState.Map).selectedMountain else null
    var lastBackPressedMillis by rememberSaveable { mutableStateOf(0L) }
    var isLocationPermissionChecked by rememberSaveable { mutableStateOf(false) }

    MapView.setMapTilePersistentCacheEnabled(true)

    // TODO : 5초마다 한 번씩 완등 가능한지 체크 -> 5초마다 현재 위치 업데이트 -> 위치 업데이트 됐을 때 함수 호출
    LaunchedEffect(uiState) {
        if (selectedMountain != null) {
            viewModel.checkCompletePossible(
                getCurrentLocation(context)
                //완등인증 테스트
//                getTestLocation(selectedMountain)
            )
            bottomSheetState.show()
        }
        if (uiState is MapUiState.Map) {
            focusManager.clearFocus()
            Timber.d("isLocationPermissionGranted : ${(uiState as MapUiState.Map).isLocationPermissionGranted}")
            if(selectedMountain == null){
                viewModel.updateLocationPermission(checkLocationPermission(context))
            }

//            if(!isLocationPermissionChecked){
//                val permission = checkLocationPermission(context){
//                    viewModel.refreshScreen()
//                    viewModel.updateLocationPermission(true)
//                }
//                Timber.d("첫번째 위치 권한 체크 : $permission")
//                viewModel.updateLocationPermission(permission)
//                isLocationPermissionChecked = true
//            }

            if((uiState as MapUiState.Map).errMsg.isNotEmpty()){
                Toast.makeText(context, (uiState as MapUiState.Map).errMsg,Toast.LENGTH_SHORT).show()
                viewModel.resetErrorMsg()
            }
        }
    }


    BackHandler(uiState is MapUiState.Search) {
        viewModel.updateSearchBarActive(false)
        onSearchScreenShowed(false)
    }

    BackHandler(uiState is MapUiState.Map) {
        val currentMillis = System.currentTimeMillis()
        if (currentMillis - lastBackPressedMillis > 3000) {
            // '뒤로 가기' 버튼이 한 번 클릭된 경우,
            // 현재 시간을 lastBackPressedMillis에 저장하고 토스트 메시지 출력
            lastBackPressedMillis = currentMillis
            Toast.makeText(context, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            // '뒤로 가기' 버튼이 3초 이내에 연속으로 클릭된 경우, 앱 종료
            exitProcess(0)
        }
    }

    when (uiState) {
        is MapUiState.Loading -> {
            CenterCircularProgressIndicator()
        }
        is MapUiState.Failure -> {
            ErrorScreen(
                errorMsg = (uiState as MapUiState.Failure).errMsg,
                onRefreshClicked = {
                    viewModel.refreshScreen()
                }
            )
        }
        is MapUiState.CompleteSuccess -> {
            navigateToComplete()
        }
        is MapUiState.Map, is MapUiState.Search -> {
            MapScreen(
                uiState = uiState,
                coroutineScope = scope,
                selectedMountain = selectedMountain,
                bottomSheetState = bottomSheetState,
                onSearchBarClicked = {
                    viewModel.updateSearchBarActive(true)
                    onSearchScreenShowed(true)
                },
                onDetailClicked = onDetailClicked,
                onCompleteBtnClicked = {
                    viewModel.completeClimbing()
                },
                onResultClicked = { result ->
                    viewModel.updateSelectedMountain(result)
                    onSearchScreenShowed(false)
                },
                onSelectMountain = { viewModel.updateSelectedMountain(it) },
                onSearch = { viewModel.updateKeyword(it) },
                onSearchQueryChanged = { viewModel.updateKeyword(it) },
                onBackClicked = {
                    viewModel.updateSearchBarActive(false)
                    onSearchScreenShowed(false)
                },
                onPermissionGranted = {viewModel.refreshScreen()}
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    uiState: MapUiState,
    selectedMountain: Mountain?,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onSelectMountain: (String?) -> Unit,
    onSearchBarClicked: () -> Unit,
    onDetailClicked: (Int) -> Unit,
    onCompleteBtnClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    onResultClicked: (String) -> Unit,
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val markerEventListener = MarkerEventListener.instance
    val markerFlow = markerEventListener.marker

    LaunchedEffect(markerFlow) {
        if (uiState is MapUiState.Map) {
            markerFlow.collect { mountainId ->
                Timber.d("marker : $mountainId")
                if (mountainId != -1) {
                    onSelectMountain(uiState.mountains.first { it.id == mountainId }.name)
                }
            }
        }
    }

    if (bottomSheetState.isVisible && uiState is MapUiState.Map) {
        MapModalBottomSheet(
            modifier = modifier,
            sheetState = bottomSheetState,
            mountain = selectedMountain ?: throw IllegalArgumentException("선택된 산이 없습니다"),
            isCompleteBtnEnabled = uiState.isCompleteBtnEnabled,
            isLocationPermissionGranted = uiState.isLocationPermissionGranted,
            onDetailClicked = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    onDetailClicked(it)
                }
            },
            onCompleteBtnClicked = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    onCompleteBtnClicked()
                }
            },
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
                markerEventListener.onPOIItemUnselected()
                onSelectMountain(null)
            }
        )
    }

    Box(
        modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = uiState is MapUiState.Map || uiState is MapUiState.Loading,
            enter = OrgoAnimation.MapViewEnterTransition,
            exit = OrgoAnimation.MapViewExitTransition
        ) {
            if (uiState is MapUiState.Map) {
                KaKaoMapView(
                    mountains = uiState.mountains,
                    markerEventListener = markerEventListener,
                    selectedMountain = selectedMountain,
                    onPermissionGranted = onPermissionGranted
                )
            }
        }
        AnimatedVisibility(
            visible = uiState is MapUiState.Search,
            enter = OrgoAnimation.SearchScreenEnterTransition,
            exit = OrgoAnimation.SearchScreenExitTransition
        ) {
            if (uiState is MapUiState.Search) {
                SearchScreen(
                    searchedMountains = uiState.mountains,
                    searchText = uiState.keyword,
                    onResultClicked = onResultClicked
                )
            }
        }

        MapSearchBar(
            modifier = modifier
                .align(Alignment.TopCenter),
            active = uiState is MapUiState.Search,
            onSearchBarClicked = onSearchBarClicked,
            onQueryChange = onSearchQueryChanged,
            onSearch = onSearch,
            onBackClicked = {
                onBackClicked()
            }
        )

    }
}

internal fun checkLocationPermission(
    context: Context
) : Boolean {
    val accessFineLocationIsGranted = checkSelfPermission(context,ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
    val accessCoarseLocationIsGranted = checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
    return accessFineLocationIsGranted && accessCoarseLocationIsGranted
}


// 위치정보 권한 체크
internal fun  checkLocationPermissionWithDialog(
    context: Context,
    onPermissionGranted : () -> Unit
) : Boolean {
    val accessFineLocationIsGranted = checkSelfPermission(context,ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
    val accessCoarseLocationIsGranted = checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    val isGranted = accessFineLocationIsGranted && accessCoarseLocationIsGranted
    Timber.d("fineLocation : $accessFineLocationIsGranted")
    Timber.d("CoarseLocation : $accessCoarseLocationIsGranted")

    if(!isGranted){
        TedPermission.create()
            .setPermissions(ACCESS_FINE_LOCATION)
            .setPermissionListener(object  : PermissionListener{
                override fun onPermissionGranted() {
                    if(checkSelfPermission(context,ACCESS_FINE_LOCATION) != PERMISSION_GRANTED){
                        Toast.makeText(context,"정확한 위치정보 사용에 동의하여야합니다.",Toast.LENGTH_SHORT).show()
                    }else Toast.makeText(context, "위치정보 권한이 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    onPermissionGranted()
                }
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(context, "위치정보 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            })
            .setDeniedMessage("서비스 사용을 위해 정확한 위치정보 권한을 설정해주세요\n[Setting] > [Permissions]")
            .check()
    }
    return isGranted
}


//
//@Preview(showBackground = true)
//@Composable
//fun PrevSearchResult() {
//    SearchResult(
//        searchText = "계양",
//        results = listOf(SearchResult("계양산", "인천 계양구 계산동"),SearchResult("계양산", "인천 계양구 계산동"))
//    )
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PrevSearchScreen() {
//    SearchScreen(searchText = "계양")
//}