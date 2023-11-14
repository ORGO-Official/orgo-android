package com.orgo.climb_complete.mountainDetailBox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.ui.component.PretendardText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MountainNameBtn(
    name : String,
    modifier: Modifier = Modifier
) {
    // 백그라운드 blur 30% 처리
    Surface(
        onClick = {},
        modifier = modifier,
        color = colorResource(id = OrgoColor.Black).copy(.3f),
        shape = RoundedCornerShape(13.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .padding(end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 19.dp),
                iconType = IconType.DrawableResourceIcon(OrgoIcon.MountainLocation),
                tint = colorResource(id = OrgoColor.White)
            )
            PretendardText(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = name,
                fontSize = 32.sp,
                color = colorResource(id = OrgoColor.White),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
