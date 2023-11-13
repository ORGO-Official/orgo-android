package com.orgo.feature.mountain_detail.mountain_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.Pretendard
import com.orgo.core.model.data.mountain.FeatureTag
import com.orgo.core.model.data.mountain.FeatureType
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.data.mountain.toFeatureTagLists
import com.orgo.core.ui.component.PretendardText

@Composable
fun MountainFeatureTagRow(
    modifier: Modifier = Modifier,
    featureTag: FeatureTag
) {
    val featureTagLists = featureTag.toFeatureTagLists()
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        userScrollEnabled = false,
    ) {
        featureTagLists.enableList.forEach { featureType ->
            featureTagItem(
                featureType = featureType,
                totalCourse =  featureTagLists.totalCourse,
                enabled = true
            )
        }
        featureTagLists.unableList.forEach { featureType ->
            featureTagItem(
                featureType = featureType,
                totalCourse =  featureTagLists.totalCourse,
                enabled = false
            )
        }
    }
}

fun LazyListScope.featureTagItem(
    featureType: FeatureType,
    totalCourse : Int,
    enabled : Boolean,
) {
    val (iconId, description) = getFeatureItemDetail(featureType,totalCourse)
    val itemColorId = if(enabled) R.color.black else R.color.light_gray

    item {
        Column(
            modifier = Modifier.size(72.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                iconType = IconType.DrawableResourceIcon(iconId),
                tint = colorResource(id = itemColorId)
            )
            Spacer(modifier = Modifier.height(6.dp))
            PretendardText(
                text = description,
                color = colorResource(id = itemColorId),
                fontSize = 14.sp,
            )
        }
    }
}

private fun getFeatureItemDetail(
    featureType: FeatureType,
    totalCourse: Int
) : Pair<Int,String> {
    val iconId : Int
    val description : String

    when(featureType){
        FeatureType.TOTAL_COURSE -> {
            iconId = OrgoIcon.Course
            description = "${totalCourse}개의 코스"
        }
        FeatureType.GOOD_NIGHT_VIEW -> {
            iconId = OrgoIcon.NightView
            description = "야경 맛집"
        }
        FeatureType.CABLE_CAR -> {
            iconId = OrgoIcon.CableCar
            description = "케이블카"
        }
        FeatureType.PARKING_LOT -> {
            iconId = OrgoIcon.Parking
            description = "주차장"
        }
        FeatureType.REST_ROOM -> {
            iconId = OrgoIcon.Restroom
            description = "화장실"
        }
    }
    return Pair(iconId,description)
}

@Preview (showBackground = true)
@Composable
fun PrevMountainFeatureTagRow() {

    MountainFeatureTagRow(
        featureTag = Mountain.DUMMY_MOUNTAIN.featureTag
    )
}