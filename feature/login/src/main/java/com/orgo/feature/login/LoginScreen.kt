package com.orgo.feature.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.orgo.core.common.auth.KakaoAuthManager
import com.orgo.core.common.auth.NaverAuthManager
import com.orgo.core.common.di.AuthManagerModule.provideKakaoAuthManager
import com.orgo.core.common.di.AuthManagerModule.provideNaverAuthManager
import kotlinx.coroutines.launch
import timber.log.Timber
import com.orgo.core.designsystem.R
import com.orgo.core.ui.component.BasicDivider
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.core.ui.component.PretendardText


@Composable
internal fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is LoginUiState.Loading -> Unit
        else -> {
            LoginScreen(
                modifier,
                onNaverLoginBtnClicked = {
                    coroutineScope.launch {
                        try {
                            val token = NaverAuthManager().executeLogin(context)
                            Timber.d("naverToken : $token")
                            viewModel.naverLogin(token)
                        } catch (t: Throwable) {
                            Timber.d("네이버 로그인 실패 : ${t.message}")
//                            Toast.makeText(context, "네이버 로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onKakaoLoginBtnClicked = {
                    coroutineScope.launch {
                        try {
                            val token = KakaoAuthManager().executeLogin(context)
                            Timber.d("kakaoToken : $token")
                            viewModel.kakaoLogin(token)
                        } catch (t: Throwable) {
                            Timber.d("카카오 로그인 실패 : ${t.message}")
                            Toast.makeText(context, "카카오 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onLoginNextBtnClicked = navigateToHome
            )
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNaverLoginBtnClicked: () -> Unit,
    onKakaoLoginBtnClicked: () -> Unit,
    onLoginNextBtnClicked: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(.4f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.orgo_logo_green),
                contentDescription = null
            )
        }
        LoginBtns(
            modifier = Modifier.weight(.4f),
            onNaverLoginBtnClicked = onNaverLoginBtnClicked,
            onKakaoLoginBtnClicked = onKakaoLoginBtnClicked,
            onLoginNextBtnClicked = onLoginNextBtnClicked
        )
        Spacer(modifier = Modifier.weight(.2f))
    }
}

@Composable
fun LoginBtns(
    modifier: Modifier = Modifier,
    onNaverLoginBtnClicked: () -> Unit,
    onKakaoLoginBtnClicked: () -> Unit,
    onLoginNextBtnClicked: () -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin_double)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginBtn(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.vertical_margin_half)),
            hasLogo = true,
            text = "카카오로 시작하기",
            backgroundColor = colorResource(id = R.color.kakao_yellow),
            textColor = colorResource(id = R.color.kakao_black),
            onBtnClicked = onKakaoLoginBtnClicked,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.kakao_logo),
                contentDescription = null
            )
        }
        LoginBtn(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.vertical_margin_half)),
            hasLogo = true,
            text = "네이버로 시작하기",
            backgroundColor = colorResource(id = R.color.naver_green),
            textColor = colorResource(id = R.color.white),
            onBtnClicked = onNaverLoginBtnClicked,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.naver_logo),
                contentDescription = null,
                tint = colorResource(id = R.color.white)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.vertical_margin_half)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicDivider(modifier = Modifier.weight(.5f))
            PretendardText(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "또는",
                fontSize = 13.sp,
                color = colorResource(id = R.color.gray)
            )
            BasicDivider(modifier = Modifier.weight(.5f))
        }
        LoginBtn(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.vertical_margin_half)),
            hasLogo = false,
            text = "다음에 로그인하기",
            backgroundColor = Color.Transparent,
            textColor = colorResource(id = R.color.black),
            borderStroke = BorderStroke(
                width = 1.dp,
                color = colorResource(id = R.color.black)
            ),
            onBtnClicked = onLoginNextBtnClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBtn(
    modifier: Modifier = Modifier,
    hasLogo: Boolean,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderStroke: BorderStroke? = null,
    onBtnClicked: () -> Unit,
    logoIcon: @Composable () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        onClick = onBtnClicked,
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        border = borderStroke,
    ) {
        if (hasLogo) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
                    .padding(vertical = 16.dp),
            ) {
                logoIcon()
                PretendardText(
                    modifier = Modifier.fillMaxWidth(),
                    text = text,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                PretendardText(
                    modifier = Modifier.fillMaxWidth(),
                    text = text,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun PrevLoginScreen() {
    LoginScreen(
        onNaverLoginBtnClicked = {},
        onKakaoLoginBtnClicked = {},
        onLoginNextBtnClicked = {}
    )
}


@Preview(showBackground = true)
@Composable
fun PrevLogoBtn() {
    LoginBtn(
        hasLogo = true,
        text = "카카오로 시작하기",
        backgroundColor = Color.Yellow,
        textColor = Color.Black,
        onBtnClicked = {}
    ) {
        Icon(
            painter = painterResource(id = R.drawable.kakao_logo),
            contentDescription = null
        )
    }

}