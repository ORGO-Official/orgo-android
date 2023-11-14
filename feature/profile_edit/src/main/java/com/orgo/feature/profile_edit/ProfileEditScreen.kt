package com.orgo.feature.profile_edit

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.orgo.core.common.image.GetImageFromGallery
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.theme.Pretendard
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.ui.component.BasicDivider
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.core.ui.component.CircularAsyncImage
import com.orgo.core.ui.component.CircularBitmapImage
import com.orgo.core.ui.component.ErrorScreen
import com.orgo.core.ui.component.PretendardText
import timber.log.Timber

const val ProfileDetailWeight1 = .25f
const val ProfileDetailWeight2 = .75f


@Composable
internal fun ProfileEditRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileEditViewModel = hiltViewModel(),
    onDoneClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isProfileImageEditClicked = remember { mutableStateOf(false) }

    when (uiState) {
        is ProfileEditUiState.Loading -> CenterCircularProgressIndicator()
        is ProfileEditUiState.UpdateSuccess -> {
            onDoneClicked()
        }

        is ProfileEditUiState.Failure -> {
            ErrorScreen(
                errorMsg = (uiState as ProfileEditUiState.Failure).message,
                onRefreshClicked = {
                    viewModel.getUserProfile()
                }
            )

        }

        is ProfileEditUiState.Edit -> {
            ProfileEditScreen(
                userProfile = (uiState as ProfileEditUiState.Edit).userProfile,
                profileImage = (uiState as ProfileEditUiState.Edit).changedImage,
                onProfileImageEditClicked = {
                    isProfileImageEditClicked.value = true
                },
                onNicknameChanged = {
                    viewModel.updateNickname(it)
                },
                onEmailChanged = {},
                onDoneClicked = { viewModel.submit() },
                onCancelClicked = onCancelClicked,
            )

            GetImageFromGallery(
                shouldExecute = isProfileImageEditClicked.value,
                onSuccess = { btm ->
                    btm?.let { viewModel.updateProfileImage(it) }
                    isProfileImageEditClicked.value = false
                    Timber.tag("bitmap").d("bitmap : $btm")
                }
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    userProfile: UserProfile,
    profileImage: Bitmap?,
    onProfileImageEditClicked: () -> Unit,
    onDoneClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    onNicknameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
) {

    Scaffold(
        topBar = {
            ProfileEditTopAppBar(
                onCancelClicked = onCancelClicked,
                onDoneClicked = onDoneClicked
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.vertical_margin)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
//                ProfileImage(imageUrl = userProfile.profileImage)
                ProfileImage(bitmap = profileImage)
                PretendardText(
                    text = "프로필 사진 수정",
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.ios_blue),
                    modifier = Modifier.clickable(MutableInteractionSource(), null) {
                        onProfileImageEditClicked()
                    }
                )
            }
            BasicDivider()
            Column(
                modifier = modifier.padding(dimensionResource(id = R.dimen.horizontal_margin))
            ) {
                ProfileDetailRow(
                    category = "사용자 이름",
                    detail = userProfile.nickname,
                    onDetailChanged = onNicknameChanged
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.vertical_margin))
                ) {
                    Spacer(modifier = Modifier.weight(ProfileDetailWeight1))
                    Spacer(modifier = Modifier.width(16.dp))
                    BasicDivider(modifier = Modifier.weight(ProfileDetailWeight2))
                }
                ProfileDetailRow(
                    category = "계정",
                    detail = userProfile.email,
                    readOnly = true,
                    onDetailChanged = onEmailChanged,
                )
            }
            BasicDivider()
        }

    }
}

@Composable
fun ProfileImage(
    bitmap: Bitmap?,
) {
    if (bitmap != null) {
        CircularBitmapImage(
            modifier = Modifier.padding(vertical = 8.dp),
            bitmap = bitmap,
            imageSize = dimensionResource(id = R.dimen.profile_image_size),
            contentScale = ContentScale.Crop
        )
    } else {
        CircularAsyncImage(
            modifier = Modifier.padding(vertical = 8.dp),
            imageUrl = "",
            imageSize = dimensionResource(id = R.dimen.profile_image_size),
            placeholder = painterResource(id = R.drawable.placeholder),
            contentScale = ContentScale.FillBounds
        )
    }
}
//
//@Composable
//fun ProfileImage(
//    imageUrl: String?,
//) {
//    CircularAsyncImage(
//        imageUrl = imageUrl ?: "",
//        modifier = Modifier.padding(vertical = 8.dp),
//        imageSize = dimensionResource(id = R.dimen.profile_image_size),
//        placeholder = painterResource(id = R.drawable.placeholder),
//        contentScale = ContentScale.Crop
//    )
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditTopAppBar(
    onCancelClicked: () -> Unit,
    onDoneClicked: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            PretendardText(
                text = "취소",
                modifier = Modifier.clickable(MutableInteractionSource(), null) {
                    onCancelClicked()
                }
            )
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PretendardText(
                    text = "프로필 편집",
                    fontWeight = FontWeight.Medium
                )
            }
        },
        actions = {
            PretendardText(
                text = "완료",
                fontSize = 15.sp,
                modifier = Modifier.clickable(MutableInteractionSource(), null) {
                    onDoneClicked()
                },
                color = colorResource(id = R.color.ios_blue)
            )
        }
    )
}


@Composable
fun ProfileDetailRow(
    category: String,
    detail: String,
    readOnly: Boolean = false,
    onDetailChanged: (String) -> Unit,
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = colorResource(id = R.color.ios_blue),
        backgroundColor = colorResource(id = R.color.ios_blue).copy(0.4f),
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        Row(modifier = Modifier.fillMaxWidth()) {
            PretendardText(
                modifier = Modifier.weight(ProfileDetailWeight1),
                text = category
            )
            Spacer(modifier = Modifier.width(16.dp))
            BasicTextField(
                modifier = Modifier.weight(ProfileDetailWeight2),
                value = detail,
                readOnly = readOnly,
                onValueChange = onDetailChanged,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(colorResource(id = R.color.ios_blue))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevProfileEditScreen() {
    PrevProfileEditScreen()
}