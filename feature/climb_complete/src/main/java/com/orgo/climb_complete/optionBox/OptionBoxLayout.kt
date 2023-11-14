package com.orgo.climb_complete.optionBox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.orgo.core.designsystem.theme.OrgoColor

@Composable
fun OptionBoxLayout(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = colorResource(id = OrgoColor.Black).copy(.7f)
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(vertical = 20.dp, horizontal = 11.dp),
            horizontalAlignment = Alignment.Start
        ) {
            content()
        }
    }
}
