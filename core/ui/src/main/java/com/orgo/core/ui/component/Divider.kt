package com.orgo.core.ui.component

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.orgo.core.designsystem.R


@Composable
fun BasicDivider(
    modifier : Modifier = Modifier,
    color : Color = colorResource(id = R.color.light_gray)
) {
    Divider(
        modifier = modifier,
        thickness = 1.dp,
        color = color
    )
}