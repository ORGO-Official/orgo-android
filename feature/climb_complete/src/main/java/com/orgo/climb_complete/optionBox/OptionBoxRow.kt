package com.orgo.climb_complete.optionBox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.ui.component.PretendardText


@Composable
fun OptionBoxRow(
    modifier: Modifier = Modifier,
    iconType: IconType,
    iconColor: Color,
    text: String,
    onRowClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onRowClicked,
                interactionSource = MutableInteractionSource(),
                indication = null
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 20.dp)
                .size(28.dp),
            iconType = iconType,
            tint = iconColor
        )
        PretendardText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 19.dp),
            text = text,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = OrgoColor.White),
            textAlign = TextAlign.Center
        )
    }
}
