package com.orgo.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon

@Composable
fun ModalBottomSheetDragHandle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.vertical_margin)),
            iconType = IconType.DrawableResourceIcon(OrgoIcon.DragHandle),
            tint = colorResource(id = R.color.light_gray)
        )
    }
}
