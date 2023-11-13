package com.orgo.feature.settings

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.ui.component.BasicDivider
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.ui.component.PretendardText

const val PrivacyPolicyUrl = "https://orgo-offical.notion.site/2b4924eb179d48c8a48793d45c13f831?pvs=4"
const val TermsOfServiceUrl = "https://orgo-offical.notion.site/e4fd74248ca04a8096ac209be33d5464?pvs=4"
const val TestUrl = "https://www.naver.com/"

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val isWebView = uiState is SettingsUiState.PrivacyPolicyWebView || uiState is SettingsUiState.TermsOfServiceWebView

    BackHandler(enabled = isWebView) {
        viewModel.setDefaultUiState()
    }

    when (uiState) {
//        is SettingsUiState.LogoutSuccess, is SettingsUiState.WithdrawSuccess -> navigateToLogin()
        is SettingsUiState.ShowLogoutDialog -> {
            SettingsDialog(
                text = "정말 로그아웃 하시겠어요?",
                subText = "로그아웃 시에도\n오르GO 서비스는 계속 이용 가능합니다.",
                onDismissRequest = { viewModel.setDefaultUiState() },
                onSubmit = { viewModel.logout() }
            )
        }
        is SettingsUiState.ShowWithdrawDialog -> {
            SettingsDialog(
                text = "정말 회원탈퇴 하시겠어요?",
                subText = "회원탈퇴시 모든 정보가 사라집니다.",
                onDismissRequest = { viewModel.setDefaultUiState() },
                onSubmit = { viewModel.withdraw() }
            )
        }
        else -> Unit
    }
    Box(modifier = modifier.fillMaxSize()) {
        when(uiState){
            is SettingsUiState.Loading -> Unit
            is SettingsUiState.PrivacyPolicyWebView -> {
                SettingsWebView(
                    text = "개인정보처리방침",
                    url = PrivacyPolicyUrl,
                    onBackClicked = { viewModel.setDefaultUiState() }
                )
            }
            is SettingsUiState.TermsOfServiceWebView -> {
                SettingsWebView(
                    text = "이용약관",
                    url = TermsOfServiceUrl,
                    onBackClicked = {viewModel.setDefaultUiState()}
                )
            }
            else -> {
                if (uiState is SettingsUiState.Failure){
                    Toast.makeText(context, (uiState as SettingsUiState.Failure).message,Toast.LENGTH_SHORT).show()
                }
                SettingsScreen(
                    isLoggedIn = uiState !is SettingsUiState.Unauthenticated,
                    onBackClicked = onBackClicked,
                    onPrivacyPolicyClicked = {viewModel.updateUiState(SettingsUiState.PrivacyPolicyWebView) },
                    onTermsOfServiceClicked = { viewModel.updateUiState(SettingsUiState.TermsOfServiceWebView) },
                    onLogoutClicked = { viewModel.updateUiState(SettingsUiState.ShowLogoutDialog) },
                    onWithdrawClicked = { viewModel.updateUiState(SettingsUiState.ShowWithdrawDialog) }
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    isLoggedIn : Boolean,
    onBackClicked: () -> Unit,
    onPrivacyPolicyClicked : () -> Unit,
    onTermsOfServiceClicked  : () -> Unit,
    onLogoutClicked: () -> Unit,
    onWithdrawClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopAppBar(
                text = "설정" ,
                onBackClicked = onBackClicked
            )
        }
    ) { padding ->
        Column(
            modifier = modifier.padding(padding)
        ) {
            BasicDivider()
            SettingRow(
                text = "개인정보처리방침",
                onRowClicked = onPrivacyPolicyClicked
            )
            BasicDivider()
            SettingRow(
                text = "이용약관",
                onRowClicked = onTermsOfServiceClicked
            )
            BasicDivider()
            if(isLoggedIn){
                SettingRow(
                    text = "로그아웃",
                    onRowClicked = onLogoutClicked
                )
                BasicDivider()
                SettingRow(
                    text = "회원탈퇴",
                    onRowClicked = onWithdrawClicked
                )
                BasicDivider()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(
    text : String,
    onBackClicked: () -> Unit,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    iconType = IconType.DrawableResourceIcon(OrgoIcon.ArrowBack),
                    contentDescription = null
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PretendardText(
                    text = text,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
        },
    )
}

@Composable
fun SettingRow(
    modifier: Modifier = Modifier,
    text: String,
    onRowClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.vertical_margin))
            .clickable(
                onClick = onRowClicked,
                interactionSource = MutableInteractionSource(),
                indication = null,
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PretendardText(
            text = text,
            fontWeight = FontWeight.Medium
        )
    }

}


@Preview
@Composable
fun PrevSettingsDialog() {
    SettingsDialog(
        text = "정말 로그아웃 하시겠어요?",
        subText = "로그아웃 시에도\n오르GO 서비스는 계속 이용 가능합니다. ",
        onDismissRequest = { /*TODO*/ }) {

    }

}