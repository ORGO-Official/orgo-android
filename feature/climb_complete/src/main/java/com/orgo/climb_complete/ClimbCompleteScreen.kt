package com.orgo.climb_complete

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.core.view.drawToBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.orgo.climb_complete.mountainDetailBox.MountainDetailBox
import com.orgo.climb_complete.optionBox.SelectPictureOptionBox
import com.orgo.climb_complete.optionBox.ShareOptionBox
import com.orgo.core.common.image.GetImageFromGallery
import com.orgo.core.common.image.saveImageToGallery
import com.orgo.core.common.util.date.convertDateFormat
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.ClimbingRecordDto
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.core.ui.component.ErrorScreen
import com.orgo.core.ui.component.PretendardText
import timber.log.Timber
import java.io.File
import java.io.IOException


@Composable
internal fun ClimbCompleteRoute(
    modifier: Modifier = Modifier,
    viewModel: ClimbCompleteViewModel = hiltViewModel(),
    onDoneClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    when (uiState) {
        is ClimbCompleteUiState.Loading -> {
            CenterCircularProgressIndicator()
        }

        is ClimbCompleteUiState.Failure -> {
            ErrorScreen(
                errorMsg = (uiState as ClimbCompleteUiState.Failure).message,
                onRefreshClicked = {
                    viewModel.fetchRecentRecord()
                }
            )
        }

        is ClimbCompleteUiState.Success -> {
//            ClimbCompleteScreen(
//                climbingRecordDto = (uiState as ClimbCompleteUiState.Success).climbingRecordDto,
//                onDoneClicked = onDoneClicked
//            )
            ClimbCompleteScreen2(
                climbingRecordDto = (uiState as ClimbCompleteUiState.Success).climbingRecordDto,
                onDoneClicked = onDoneClicked
            )
        }
    }
}

@Composable
fun ClimbCompleteScreen2(
    climbingRecordDto: ClimbingRecordDto,
    onDoneClicked: () -> Unit
) {
    val context = LocalContext.current
    var optionBoxState by remember { mutableStateOf<OptionBoxState>(OptionBoxState.Closed) }
    var isSelectImageClicked by remember { mutableStateOf(false) }
    var isTakePictureClicked by remember { mutableStateOf(false) }
    var customImage by remember { mutableStateOf<Bitmap?>(null) }
    lateinit var snapshot: () -> Bitmap

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = OrgoColor.Black))
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(bottomStart = 23.dp, bottomEnd = 23.dp)
            ) {
                snapshot = captureBitmap {
                    CompleteView(
                        climbingRecordDto = climbingRecordDto,
                        customImage = customImage)
                }
            }
            if (optionBoxState == OptionBoxState.SelectPicture) {
                SelectPictureOptionBox(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 17.dp, bottom = 8.dp),
                    onGalleryClicked = {
                        isSelectImageClicked = true
                    },
                    onTakePictureClicked = {
                        isTakePictureClicked = true

                    }
                )
            }
            if (optionBoxState is OptionBoxState.Share) {
                ShareOptionBox(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 132.dp, bottom = 8.dp),
                    onInstagramShareClicked = {

                    },
                    onKakaotalkShareClicked = {

                    },
                )
            }
        }

        ClimbCompleteBottomBar(
            onSelectImageClicked = {
                optionBoxState = if (optionBoxState is OptionBoxState.SelectPicture)
                    OptionBoxState.Closed
                else OptionBoxState.SelectPicture
            },
            onShareClicked = {
                Toast.makeText(context,"아직 지원하지 않는 기능입니다.",Toast.LENGTH_SHORT).show()
//                optionBoxState = if (optionBoxState is OptionBoxState.Share)
//                    OptionBoxState.Closed
//                else OptionBoxState.Share
            },
            onSaveImageClicked = {
                val bitmap = snapshot.invoke()
                Timber.d("bitmap is $bitmap")
                saveImageToGallery(context, bitmap) { isSuccess ->
                    Toast.makeText(
                        context,
                        if (isSuccess) "이미지 저장 완료" else "이미지 저장 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onDoneClicked = onDoneClicked
        )
    }

    GetImageFromGallery(
        shouldExecute = isSelectImageClicked,
    ) { btm ->
        btm?.let {
            customImage = it
        }
        isSelectImageClicked = false
    }

    GetImageFromCamera(
        shouldExecute = isTakePictureClicked,
        onSuccess = { uri ->
            customImage = uri.parseBitmap(context)
            isTakePictureClicked = false
        },
        onFailure =  {
            isTakePictureClicked = false
        }
    )
}
@Suppress("DEPRECATION", "NewApi")
private fun Uri.parseBitmap(context: Context): Bitmap {
    return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // 28
        true -> {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(
                source,
                ImageDecoder.OnHeaderDecodedListener{ decoder, info, source ->
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    decoder.isMutableRequired = true
                }
            )
        }
        else -> {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }
    }
}

fun Context.createTempPictureUri(
    provider: String = "com.orgo.android.provider",
    fileName: String = "picture_${System.currentTimeMillis()}",
    fileExtension: String = ".png"
): Uri {
    val tempFile = File.createTempFile(
        fileName, fileExtension, cacheDir
    ).apply {
        createNewFile()
    }
    return FileProvider.getUriForFile(applicationContext, provider, tempFile)
}

@Composable
private fun GetImageFromCamera(
    shouldExecute: Boolean,
    onSuccess: (Uri) -> Unit,
    onFailure :() -> Unit,
) {
    val context = LocalContext.current
    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }

    // TODO : 이건 지금 프리뷰만 보여주는데 실제로 이미지 저장하고 가져와야함.
    val takePhotoFromCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                onSuccess(tempPhotoUri)
            }else onFailure()
        }

    LaunchedEffect(shouldExecute) {
        if (PermissionChecker.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            TedPermission.create()
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        Toast.makeText(context, "카메라 권한이 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        Toast.makeText(context, "카메라 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
                .setPermissions(Manifest.permission.CAMERA)
                .setDeniedMessage("서비스 사용을 위해 카메라 권한을 설정해주세요\n[Setting] > [Permissions]")
                .check()
        } else {
            if (shouldExecute) {
                tempPhotoUri = context.createTempPictureUri()
                takePhotoFromCameraLauncher.launch(tempPhotoUri)
            }
        }
    }
}


@Composable
fun ClimbCompleteBottomBar(
    onSelectImageClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onSaveImageClicked: () -> Unit,
    onDoneClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
            .padding(top = 4.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onSelectImageClicked,
            shape = RoundedCornerShape(19.5.dp),
            border = BorderStroke(2.dp, colorResource(id = OrgoColor.White)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            PretendardText(
                text = "사진 선택",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = colorResource(id = OrgoColor.White)
            )
        }
        Spacer(modifier = Modifier.width(13.dp))
        Button(
            onClick = onShareClicked,
            shape = RoundedCornerShape(19.5.dp),
            border = BorderStroke(2.dp, colorResource(id = OrgoColor.White)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            PretendardText(
                text = "공유",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = colorResource(id = OrgoColor.White)
            )
        }
        Spacer(Modifier.width(18.dp))
        Box(
            modifier = Modifier.clickable {
                onSaveImageClicked()
            },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                iconType = IconType.DrawableResourceIcon(OrgoIcon.SaveImage),
                tint = colorResource(id = OrgoColor.White)
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onDoneClicked) {
            Icon(
                iconType = IconType.DrawableResourceIcon(OrgoIcon.CompleteCheck),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun captureBitmap(
    content: @Composable () -> Unit,
): () -> Bitmap {
    val context = LocalContext.current
    val composeView = remember { ComposeView(context) }
    fun captureBitmap(): Bitmap {
        return composeView.drawToBitmap()
    }
    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content.invoke()
                }
            }
        }
    )
    return ::captureBitmap
}

@Composable
fun CompleteView(
    climbingRecordDto: ClimbingRecordDto,
    customImage: Bitmap?
) {
    var viewSize by remember { mutableStateOf<IntSize>(IntSize(0, 0)) }
    var logoColor: Color = colorResource(id = OrgoColor.White)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                viewSize = layoutCoordinates.size
            },
    ) {
        if (customImage == null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.orgo_background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.climb_complete_logo),
                contentDescription = null
            )
        } else {
            // 로고 색 다르게 처리해주려고 했던 코드
//            if(!checkLogoInView(viewSize, customImage)) {
//                logoColor = colorResource(id = OrgoColor.Black)
//            }else if (isBottomRightPixelBright(customImage)){
//                logoColor = colorResource(id = OrgoColor.Black)
//            }
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = rememberAsyncImagePainter(customImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(
                    color = colorResource(OrgoColor.Black).copy(.1f),
                    blendMode = BlendMode.ColorBurn
                )
            )
        }
        MountainDetailBox(climbingRecordDto = climbingRecordDto)
        Icon(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 17.dp, bottom = 8.dp),
            iconType = IconType.DrawableResourceIcon(OrgoIcon.Orgo_WaterMark),
            tint = logoColor
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimbCompleteScreen(
    modifier: Modifier = Modifier,
    climbingRecordDto: ClimbingRecordDto,
    onDoneClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = colorResource(id = R.color.orgo_green),
        topBar = {
            ClimbCompleteTopAppBar(
                date = convertDateFormat(climbingRecordDto.date),
                count = climbingRecordDto.climbingOrder
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.climb_complete_logo),
                    contentDescription = null
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PretendardText(
                        text = "축하드립니다!",
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.white),
                        fontSize = 20.sp,
                    )
                    PretendardText(
                        text = "${climbingRecordDto.mountainName} 완등 완료",
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.white),
                        fontSize = 20.sp,
                    )
                    PretendardText(
                        text = "해발 ${climbingRecordDto.altitude.toInt()}m",
                        color = colorResource(id = R.color.white),
                        fontSize = 13.sp,
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_roundedCorner)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.ios_blue)
                ),
                onClick = onDoneClicked,
            ) {
                PretendardText(
                    text = "확인",
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.white)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimbCompleteTopAppBar(
    modifier: Modifier = Modifier,
    date: String,
    count: Int,
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.orgo_green),
            titleContentColor = colorResource(id = R.color.white),
            actionIconContentColor = colorResource(id = R.color.white),
        ),
        title = {
            PretendardText(
                text = date,
                color = colorResource(id = R.color.white)
            )
        },
        actions = {
            PretendardText(
                text = "${count}회차",
                color = colorResource(id = R.color.white)
            )
        }
    )

}

//@Preview(showBackground = true)
@Composable
fun PrevClimbCompleteScreen() {

    ClimbCompleteScreen(
        climbingRecordDto = ClimbingRecord.DUMMY_RECORD.getLatestRecord() ?: throw IOException("")
    ) {

    }

}

@Preview(showBackground = true)
@Composable
fun PrevClimbCompleteScreen2() {

    ClimbCompleteScreen2(
        climbingRecordDto = ClimbingRecord.DUMMY_RECORD.getLatestRecord() ?: throw IOException(""),
//        onSelectPictureClicked = {},
//        onShareClicked = {},
        onDoneClicked = {}
    )
}

//
//fun isBottomRightPixelBright(bitmap: Bitmap): Boolean {
//    val width = bitmap.width
//    val height = bitmap.height
//    val x = width - 1  // 우측 끝 픽셀의 X 좌표
//    val y = height - 1 // 하단 끝 픽셀의 Y 좌표
//
//    val pixelColor = bitmap.getPixel(x, y)
//
//    // 밝은 색상인지 확인 (예: 흰색 또는 밝은 회색)
//    val isBright = ColorUtils.calculateLuminance(pixelColor) > 0.9
//    Timber.d("isBright = $isBright")
//    return isBright
//}
//fun checkLogoInView(viewSize: IntSize, customImage: Bitmap): Boolean {
//    Timber.d("viewSize width= ${viewSize.width} height = ${viewSize.height}")
//    Timber.d("bitmapSize width = ${customImage.width} height = ${customImage.height}")
//
//    // viewSize의 너비와 높이
//    val viewWidth = viewSize.width.toFloat()
//    val viewHeight = viewSize.height.toFloat()
//    // 비트맵의 너비와 높이
//    val bitmapWidth = customImage.width.toFloat()
//    val bitmapHeight = customImage.height.toFloat()
//
//    // viewSize의 화면 비율 계산
//    val screenAspectRatio = viewWidth / viewHeight
//    // 비트맵의 화면 비율 계산
//    val bitmapAspectRatio = bitmapWidth / bitmapHeight
//
//    // 조건에 따라 true 또는 false 반환
//    val result = bitmapAspectRatio < screenAspectRatio
//
//    Timber.d("checkLogoPosition result = $result")
//    return result
//}
