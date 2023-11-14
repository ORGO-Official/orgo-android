package com.orgo.climb_complete.optionBox

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.ui.component.BasicDivider


@Composable
fun ShareOptionBox(
    modifier: Modifier = Modifier,
    onInstagramShareClicked: () -> Unit,
    onKakaotalkShareClicked: () -> Unit
) {
    OptionBoxLayout(modifier) {
        OptionBoxRow(
            text = "Instagram으로 공유",
            iconType = IconType.DrawableResourceIcon(OrgoIcon.Instagram),
            iconColor = Color.Unspecified,
            onRowClicked = onInstagramShareClicked
        )
        BasicDivider(
            modifier = Modifier.padding(vertical = 17.dp),
            color = colorResource(id = OrgoColor.White)
        )
        OptionBoxRow(
            text = "카카오톡으로 공유",
            iconType = IconType.DrawableResourceIcon(OrgoIcon.KakaoTalk),
            iconColor = Color.Unspecified,
            onRowClicked = onKakaotalkShareClicked
        )

    }

}
