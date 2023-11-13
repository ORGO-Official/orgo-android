package com.orgo.featuremypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.ui.component.BasicDivider
import com.orgo.core.ui.component.PretendardText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MypageScreenUnauthenticated(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    onSettingClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MypageTopAppBarUnauthenticated(
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
                    text = "로그인하고 완등 기록을 남겨보세요!",
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = colorResource(id = OrgoColor.Gray)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_roundedCorner)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = OrgoColor.OrgoGreen)
                    ),
                    onClick = navigateToLogin,
                ) {
                    PretendardText(
                        text = "로그인하러 가기",
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = OrgoColor.White)
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MypageTopAppBarUnauthenticated(
    onSettingClicked: () -> Unit
) {
    TopAppBar(
        title = {
            PretendardText(
                text = "로그인이 필요합니다.",
                color = colorResource(id = OrgoColor.Gray),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }, actions = {
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
fun PrevMypageTopAppBarUnauthenticated() {
    MypageTopAppBarUnauthenticated(
        onSettingClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PrevMypageScreenUnauthenticated() {
    MypageScreenUnauthenticated(
        navigateToLogin = { },
        onSettingClicked = {}
    )
}