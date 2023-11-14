package com.orgo.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon

@Composable
fun ErrorScreen(
    modifier : Modifier = Modifier,
    errorMsg : String = "",
    onRefreshClicked : () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onRefreshClicked) {
            Icon(iconType = IconType.DrawableResourceIcon(OrgoIcon.Refresh))

        }
        PretendardText(
            text = "페이지 로딩에 실패하였습니다." ,
            fontWeight = FontWeight.Medium
        )
        PretendardText(
            text = errorMsg
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PrevErrorScreen() {
    ErrorScreen(
        errorMsg = "에러 메시지 테스트",
        onRefreshClicked = {}
    )

}