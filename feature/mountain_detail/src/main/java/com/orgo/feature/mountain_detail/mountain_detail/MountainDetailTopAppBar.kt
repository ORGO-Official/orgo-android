package com.orgo.feature.mountain_detail.mountain_detail

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.ui.component.PretendardText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MountainDeatilTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                PretendardText(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        iconType = IconType.DrawableResourceIcon(OrgoIcon.ArrowBack),
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrevMountainDetailTopAppBar() {
    MountainDeatilTopAppBar(
        title = "계양산",
        onBackClicked = {}
    )
}