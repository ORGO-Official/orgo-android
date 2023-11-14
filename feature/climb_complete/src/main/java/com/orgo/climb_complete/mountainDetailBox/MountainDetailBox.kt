package com.orgo.climb_complete.mountainDetailBox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orgo.core.common.util.date.convertDateFormat
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.model.data.user.ClimbingRecordDto
import com.orgo.core.ui.component.PretendardText


@Composable
fun MountainDetailBox(
    climbingRecordDto: ClimbingRecordDto,
) {
    Column(
        modifier = Modifier
            .padding(top = 45.dp)
            .padding(start = 17.dp)
    ) {
        MountainNameBtn(name = climbingRecordDto.mountainName)
        Column(modifier = Modifier.padding(start = 19.dp, top = 16.dp)) {
            PretendardText(
                text = "${climbingRecordDto.climbingOrder} 회차",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = colorResource(id = OrgoColor.White)
            )
            PretendardText(
                text = "${climbingRecordDto.altitude.toInt()}m",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = colorResource(id = OrgoColor.White)
            )
            PretendardText(
                text =  convertDateFormat(climbingRecordDto.date),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = colorResource(id = OrgoColor.White)
            )
        }

    }
}
