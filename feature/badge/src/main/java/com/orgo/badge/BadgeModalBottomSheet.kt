package com.orgo.badge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orgo.badge.util.badgeIdToData
import com.orgo.core.common.util.date.convertDateFormat
import com.orgo.core.designsystem.R
import com.orgo.core.model.data.badge.AcquiredBadge
import com.orgo.core.model.data.badge.Badge
import com.orgo.core.model.data.badge.UnacquiredBadge
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.ui.component.ModalBottomSheetDragHandle
import com.orgo.core.ui.component.PretendardText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    selectedBadge : Badge,
    totalBadges : List<Badge>,
    onDismissRequest: () -> Unit
) {
    val selectedGroupId = (selectedBadge.id - 1) / 3
    val selectedGroupRange = (selectedGroupId * 3 + 1..selectedGroupId * 3 + 3)
    val badges = if (selectedBadge.id in 1..30) {
        totalBadges.filter { it.id in selectedGroupRange }
    } else listOf(selectedBadge)

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.bottomSheet_roundedCorner),
            topEnd = dimensionResource(id = R.dimen.bottomSheet_roundedCorner)
        ),
        dragHandle = { ModalBottomSheetDragHandle() },
        onDismissRequest = onDismissRequest,
    ) {
        BadgeBottomSheetContent(
            selectedBadge = selectedBadge,
            badges = badges,
        )
    }
}


@Composable
fun BadgeBottomSheetContent(
    modifier: Modifier = Modifier,
    selectedBadge : Badge,
    badges: List<Badge>,
) {
    val isMountainBadges = badges.size > 1
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.vertical_margin)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isMountainBadges){
            badges.forEach{ badge ->
                val (badgeResId,detail) = badgeIdToData(badge.id)
                BadgeImage(
                    badgeResId = badgeResId,
                    isAcquired = badge is AcquiredBadge
                )
                PretendardText(text = "${(badge.id + 2) % 3 * 2 + 1}회")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        else{
            val badge = badges[0]
            val (badgeResId,detail) = badgeIdToData(badge.id)
            BadgeImage(
                badgeResId = badgeResId,
                isAcquired = badge is AcquiredBadge
            )
        }
        PretendardText(
            text = "뱃지 획득일 : "+ convertDateFormat((selectedBadge as AcquiredBadge).acquiredTime),
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )
        PretendardText(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = (selectedBadge as AcquiredBadge).description,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

    }

}

@Preview(showBackground = true)
@Composable
fun PrevBottomSheetContent() {
    BadgeBottomSheetContent(
        selectedBadge = AcquiredBadge(1,"","","설명"),
        badges = listOf(
            AcquiredBadge(1,"","",""),
            UnacquiredBadge(1,""),
            AcquiredBadge(2,"","",""),
        ),
    )
}
