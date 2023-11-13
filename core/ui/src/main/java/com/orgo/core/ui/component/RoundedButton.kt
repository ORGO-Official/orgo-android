package com.orgo.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    basicBackgroundColor : Color,
    pressedBackgroundColor : Color,
    buttonBorder : BorderStroke? = null,
    roundedCornerSize : Dp = 8.dp,
    onClick : () -> Unit,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if(isPressed) pressedBackgroundColor else basicBackgroundColor
    Surface(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null,
            ),
        shape = RoundedCornerShape(roundedCornerSize),
        color = backgroundColor,
        border = buttonBorder
    ){
        Box(
            contentAlignment = Alignment.Center
        ){
           content()
        }
    }

}