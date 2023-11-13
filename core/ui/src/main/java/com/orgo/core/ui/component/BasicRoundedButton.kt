package com.orgo.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orgo.core.designsystem.theme.OrgoColor

@Composable
fun BasicRoundedButton(
    text : String,
    onClick : () -> Unit
) {
    RoundedButton(
        modifier = Modifier
            .padding(top = 1.dp)
            .width(110.dp)
            .height(21.dp),
        basicBackgroundColor = colorResource(id = OrgoColor.White),
        pressedBackgroundColor = colorResource(id = OrgoColor.Light_Gray).copy(.3f),
        buttonBorder = BorderStroke(width = 1.dp, color = colorResource(id = OrgoColor.Light_Gray)),
        roundedCornerSize = (10.5).dp,
        onClick = onClick
    ) {
        PretendardText(
            text = text,
            fontSize = 13.sp,
        )
    }

}