package com.orgo.feature.map.ui.mapview

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.orgo.core.common.util.getCurrentLocation
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.ui.custom_modifier.customCircleShadow
import net.daum.mf.map.api.CameraUpdateFactory
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import timber.log.Timber

val DEFAULT_LOCATION = Pair<Double, Double>(37.57149, 127.103764)

@Composable
fun KaKaoMapView(
    modifier: Modifier = Modifier,
    mountains: List<Mountain>,
    selectedMountain: Mountain?,
    markerEventListener: MarkerEventListener,
    onPermissionGranted : () -> Unit
) {
    //  Search 화면 갔다가 돌아왔을 때 MapView를 다시 생성해준다
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    mapView.setPOIItemEventListener(markerEventListener)
    mapView.setCustomCurrentLocationMarkerTrackingImage(
        OrgoIcon.CurrentLocationMarker,
        MapPOIItem.ImageOffset(26, 26)           // MarkerTrackingIcon의 크기가 53px이라서
    )

    LaunchedEffect(selectedMountain) {
        if (selectedMountain != null) {
            Timber.d("selectedMountain : ${selectedMountain.name}")
            mapView.moveCamera(
                CameraUpdateFactory.newMapPoint(
                    MapPoint.mapPointWithGeoCoord(
                        selectedMountain.location.latitude,
                        selectedMountain.location.longitude
                    )
                )
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView.onSurfaceDestroyed()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = {
                mapView.apply {
                    mountains.forEach { mountain ->
                        Timber.tag("hotfix").d("mountain : $mountain")
                        this.addPOIItem(mountainPOIItem(mountain))
                    }
                    currentLocationTrackingMode =
                        MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving

                    setZoomLevel(6,true)
                }
            },
            update = {
//                it.addPOIItem(MapPOIItem().apply {
//                    itemName = "현 위치"
//                    mapPoint = getCurrentMapPoint(context)
//                })
            }
        )
        MyLocationBtn(
            onBtnClicked = {
                if (checkLocationPermissionWithDialog(context,onPermissionGranted)) {
                    val mapPoint = getCurrentMapPoint(context)
                    mapView.setMapCenterPoint(mapPoint, true)
                }
            }
        )
    }
}

private fun mountainPOIItem(mountain: Mountain): MapPOIItem {
    return MapPOIItem().apply {
        itemName = mountain.name
        userObject = mountain
        mapPoint = MapPoint.mapPointWithGeoCoord(
            mountain.location.latitude,
            mountain.location.longitude
        )
        markerType = MapPOIItem.MarkerType.CustomImage
        customImageResourceId = getMountainIconId(mountain.location.altitude)
        isCustomImageAutoscale = true
        showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
        this.isShowCalloutBalloonOnTouch = false
    }
}

private fun getMountainIconId(mountainAltitude: Double): Int {
    return when {
        mountainAltitude < 400.0 -> OrgoIcon.Mountain1
        mountainAltitude < 600.0 -> OrgoIcon.Mountain2
        mountainAltitude < 800.0 -> OrgoIcon.Mountain3
        else -> OrgoIcon.Mountain4
    }
}


@Composable
fun BoxScope.MyLocationBtn(
    modifier: Modifier = Modifier,
    onBtnClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(
                bottom = 64.dp,
                end = 8.dp
            )
            .customCircleShadow(
                color = colorResource(id = OrgoColor.Black).copy(0.2f),
                blurRadius = 7.dp
            ),
        shape = CircleShape,
//        shadowElevation = dimensionResource(id = R.dimen.shadowElevation)
    ) {
        // 안에 아이콘으로 버튼을 꽉 채우기 위해 FilledIconButton을 사용함
        // 그냥 IconButton은 기본적으로 Indication이 설정되어있어 커스텀 불가능해 size를 조정했을 경우 안에 아이콘 크기만큼만 ripple 효과 자동적용됨
        FilledIconButton(
            modifier = modifier
                .size(48.dp),
            onClick = onBtnClicked,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White,
            ),
            interactionSource = remember { MutableInteractionSource() }
        ) {
            Icon(
                modifier = modifier,
                painter = painterResource(id = OrgoIcon.MyLocation),
                contentDescription = null
            )
        }
    }
}


//카카오의 CurrentLocationEventListener를 사용해서 현재 트래킹 이벤트 받아오는걸로 수정
fun getCurrentMapPoint(
    context: Context
): MapPoint {
    val currentLocation = getCurrentLocation(context)
    return if (currentLocation != null)
        MapPoint.mapPointWithGeoCoord(currentLocation.latitude, currentLocation.longitude)
    else {
        Timber.d("currentLocation is null")
        MapPoint.mapPointWithGeoCoord(DEFAULT_LOCATION.first, DEFAULT_LOCATION.second)
    }
}


@Preview(showBackground = true)
@Composable
fun PrevMyLocationIcon() {
    Box() {
        MyLocationBtn() {

        }
    }
}
