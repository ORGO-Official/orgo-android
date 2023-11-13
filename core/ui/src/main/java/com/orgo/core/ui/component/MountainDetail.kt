package com.orgo.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.orgo.core.model.data.mountain.Mountain


@Composable
fun MountainDetailInfo(
    modifier : Modifier = Modifier,
    mountain: Mountain
) {
    Column(modifier  = modifier){
        MountainDetailRow(category = "주소", detail = mountain.address)
        MountainDetailRow(category = "고도", detail = "${mountain.location.altitude.toInt()}m")
        MountainDetailRow(category = "완등 시간", detail = mountain.requiredTime)
        MountainDetailRow(category = "문의", detail = mountain.contact)
    }
}

@Composable
fun MountainDetailRow(
    modifier: Modifier = Modifier,
    category: String,
    detail: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        PretendardText(
            text = category,
            modifier = modifier.widthIn(min = 80.dp)
        )
        PretendardText(
            detail,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
