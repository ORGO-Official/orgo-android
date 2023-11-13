package com.orgo.featuremypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.orgo.core.common.util.date.convertDateFormat
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.domain.usecase.user.DUMMY_IMAGE
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.ClimbingRecordDto
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.ui.component.BasicDivider
import com.orgo.core.ui.component.BasicRoundedButton
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.core.ui.component.CircularAsyncImage
import com.orgo.core.ui.component.ErrorScreen
import com.orgo.core.ui.component.PretendardText

@Composable
internal fun MypageRoute(
    modifier: Modifier = Modifier,
    viewModel: MypageViewModel = hiltViewModel(),
    onProfileEditClicked: () -> Unit,
    onSettingClicked: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val uiState by viewModel.mypageUiState.collectAsStateWithLifecycle()

    when (uiState) {
        is MypageUiState.Loading -> CenterCircularProgressIndicator()
        is MypageUiState.Failure -> {
            ErrorScreen(
                errorMsg = (uiState as MypageUiState.Failure).message,
                onRefreshClicked = {
                    viewModel.fetchUserData()
                }
            )
        }
        is MypageUiState.Unauthenticated -> {
            MypageScreenUnauthenticated(
                navigateToLogin = navigateToLogin,
                onSettingClicked = onSettingClicked
            )
        }

        is MypageUiState.Success -> {
            MypageScreen(
                profile = (uiState as MypageUiState.Success).userProfile,
                record = (uiState as MypageUiState.Success).userClimbingRecord,
                onProfileEditClicked = onProfileEditClicked,
                onSettingClicked = onSettingClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MypageScreen(
    modifier: Modifier = Modifier,
    profile: UserProfile,
    record: ClimbingRecord,
    onProfileEditClicked: () -> Unit,
    onSettingClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MypageTopAppBar(
                nickname = profile.nickname,
                onProfileEditClicked = onProfileEditClicked,
                onSettingClicked = onSettingClicked
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin)),
        ) {
            BasicDivider()
            ProfileDetailRow(
                profileImageUrl = profile.profileImage ?: DUMMY_IMAGE,
                totalAltitude = record.climbedAltitude,
                totalCompletion = record.climbingCnt
            )
            BasicDivider()
            Row(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.vertical_margin))
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PretendardText(
                    text = "내 기록",
                    fontWeight = FontWeight.Medium
                )
            }
            BasicDivider()
            MyRecordsContent(record = record)
        }
    }
}

@Composable
fun MyRecordsContent(
    modifier : Modifier = Modifier,
    record: ClimbingRecord
) {
    if(record.climbingRecordDtoList.isEmpty()){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.padding(bottom = dimensionResource( id = R.dimen.vertical_margin)),
                painter = painterResource(id = OrgoIcon.Today_Orgo),
                contentDescription = null,
                tint = colorResource(id = OrgoColor.Light_Gray)
            )
            PretendardText(
                modifier = Modifier.padding(bottom = dimensionResource( id = R.dimen.vertical_margin)),
                text = "아직 완등 기록이 없어요.",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = OrgoColor.Black)
            )
            PretendardText(
                modifier = Modifier.padding(bottom = dimensionResource( id = R.dimen.vertical_margin)),
                text = "산을 오르고 완등 기록을 남겨보세요!",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = colorResource(id = OrgoColor.Gray)
            )
        }
    }else{
        LazyColumn(
            contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.vertical_margin))
        ) {
            items(record.climbingRecordDtoList) { record ->
                MyRecordRow(record)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
@Composable
fun ProfileDetailRow(
    modifier: Modifier = Modifier,
    profileImageUrl: String,
    totalAltitude: Double,
    totalCompletion: Int,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(vertical = dimensionResource(id = R.dimen.vertical_margin)),
    ) {
        CircularAsyncImage(
            imageUrl = profileImageUrl,
            imageSize = 68.dp,
            placeholder = painterResource(id = R.drawable.placeholder),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(64.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PretendardText(
                text = "총 고도",
                fontSize = 14.sp
            )
            Spacer(Modifier.height(dimensionResource( id = R.dimen.vertical_margin_half)))
            PretendardText(
                text = "${"%.2f".format(totalAltitude)}m",
                color = colorResource(id = R.color.gray),
                fontSize = 13.sp
            )
        }
        Spacer(Modifier.width(64.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PretendardText(
                text = "완등 횟수",
                fontSize = 14.sp
            )
            Spacer(Modifier.height(dimensionResource( id = R.dimen.vertical_margin_half)))
            PretendardText(
                text = "${totalCompletion}회",
                color = colorResource(id = R.color.gray),
                fontSize = 13.sp
            )
        }
    }
}


@Composable
fun MyRecordRow(
    record: ClimbingRecordDto
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = Color.LightGray)
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.horizontal_margin)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        PretendardText(convertDateFormat(record.date))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PretendardText(record.mountainName)
            PretendardText(
                text = "해발 ${record.altitude.toInt()}m",
                fontSize = 13.sp,
                color = colorResource(id = R.color.gray)
            )
        }
        PretendardText("${record.climbingOrder}회차")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MypageTopAppBar(
    modifier: Modifier = Modifier,
    nickname: String,
    onProfileEditClicked: () -> Unit,
    onSettingClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                PretendardText(
                    text = nickname,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier.width(24.dp))
                BasicRoundedButton(
                    text = "프로필 관리",
                    onClick = onProfileEditClicked
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingClicked) {
                Icon(
                    painter = painterResource(id = OrgoIcon.Setting),
                    contentDescription = null
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PrevMypageScreen() {
    MypageScreen(
        modifier = Modifier,
        profile = UserProfile.DUMMY_PROFILE,
        record = ClimbingRecord.DUMMY_RECORD,
        onProfileEditClicked = {},
        onSettingClicked = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PrevMypageTopAppBar() {
    MypageTopAppBar(
        nickname = "nickname",
        onProfileEditClicked = {},
        onSettingClicked = {}
    )
}
