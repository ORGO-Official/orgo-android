package com.orgo.climb_complete.optionBox

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.ui.component.BasicDivider


@Composable
fun SelectPictureOptionBox(
    modifier: Modifier = Modifier,
    onGalleryClicked: () -> Unit,
    onTakePictureClicked: () -> Unit
) {
    OptionBoxLayout(modifier) {
        OptionBoxRow(
            text = "갤러리에서 선택",
            iconType = IconType.DrawableResourceIcon(OrgoIcon.Gallery),
            iconColor = colorResource(id = OrgoColor.White),
            onRowClicked = onGalleryClicked
        )
        BasicDivider(
            modifier = Modifier.padding(vertical = 17.dp),
            color = colorResource(id = OrgoColor.White)
        )
        OptionBoxRow(
            text = "사진 촬영",
            iconType = IconType.DrawableResourceIcon(OrgoIcon.Camera),
            iconColor = colorResource(id = OrgoColor.White),
            onRowClicked = onTakePictureClicked

        )
    }
}

