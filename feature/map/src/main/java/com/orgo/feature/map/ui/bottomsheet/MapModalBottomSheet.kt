package com.orgo.feature.map.ui.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.ui.component.BasicRoundedButton
import com.orgo.core.ui.component.ModalBottomSheetDragHandle
import com.orgo.core.ui.component.MountainDetailInfo
import com.orgo.core.ui.component.PretendardText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    mountain: Mountain,
    isCompleteBtnEnabled: Boolean,
    isLocationPermissionGranted : Boolean,
    onDetailClicked: (Int) -> Unit,
    onCompleteBtnClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
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
        MapBottomSheetContent(
            mountain = mountain,
            isCompleteBtnEnabled = isCompleteBtnEnabled,
            isLocationPermissionGranted = isLocationPermissionGranted,
            onDetailClicked = onDetailClicked,
            onCompleteBtnClicked = onCompleteBtnClicked
        )
    }
}

@Composable
fun MapBottomSheetContent(
    modifier: Modifier = Modifier,
    mountain: Mountain,
    isCompleteBtnEnabled: Boolean,
    isLocationPermissionGranted : Boolean,
    onDetailClicked: (Int) -> Unit,
    onCompleteBtnClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
            .padding(bottom = dimensionResource(id = R.dimen.vertical_margin))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PretendardText(
                text = mountain.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            DifficultyIcons(
                modifier = Modifier.padding(start = 8.dp),
                difficulty = mountain.difficulty
            )
            Spacer(modifier = Modifier.weight(1f))
            BasicRoundedButton(
                text = "더보기",
                onClick = { onDetailClicked(mountain.id) }
            )
        }
        Spacer(modifier = modifier.height(8.dp))
        MountainDetailInfo(mountain = mountain)
        Spacer(modifier = modifier.height(8.dp))
        CompleteBtn(
            enabled = isCompleteBtnEnabled && isLocationPermissionGranted,
            onCompleteBtnClicked = onCompleteBtnClicked
        )
        PretendardText(
            text = if(isLocationPermissionGranted){
                if(isCompleteBtnEnabled) "오르GO! 렛츠GO!" else "아직 충분히 가깝지 않아요~"
            }else "위치정보 권한을 설정해주세요",
            modifier = modifier.align(Alignment.CenterHorizontally),
            color = colorResource(id = OrgoColor.Gray),
            fontSize = 13.sp
        )
    }
}


@Composable
fun DifficultyIcons(
    modifier: Modifier = Modifier,
    difficulty: String
) {
    val filledIconCnt = mapDifficultyToValue(difficulty)
    val borderIconCnt = 3 - filledIconCnt
    Row(modifier = modifier) {
        repeat(filledIconCnt) {
            Icon(
                modifier = Modifier.padding(horizontal = 4.dp),
                iconType = IconType.DrawableResourceIcon(OrgoIcon.Difficulty_Filled),
                tint = colorResource(id = R.color.difficulty_icon_color)
            )
        }
        repeat(borderIconCnt) {
            Icon(
                modifier = Modifier.padding(horizontal = 4.dp),
                iconType = IconType.DrawableResourceIcon(OrgoIcon.Difficulty_Border),
                tint = colorResource(id = R.color.difficulty_icon_color)
            )
        }
    }
}

private fun mapDifficultyToValue(difficulty: String): Int {
    return when (difficulty) {
        Difficulty.EASY.value -> 1
        Difficulty.NORMAL.value -> 2
        Difficulty.HARD.value -> 3
        else -> throw IllegalArgumentException("Invalid difficulty")
    }
}


@Composable
fun CompleteBtn(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onCompleteBtnClicked: () -> Unit,
) {
    Button(
        onClick = onCompleteBtnClicked,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_roundedCorner)),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = OrgoColor.OrgoGreen),
            contentColor = Color.White,
            disabledContainerColor = colorResource(id = R.color.light_gray),
            disabledContentColor = colorResource(id = R.color.gray)
        )
    ) {
        PretendardText(
            text = "완등 인증하기",
            color = colorResource(id = if (enabled) R.color.white else R.color.gray),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrevBottomSheetContent() {
    MapBottomSheetContent(
        mountain = Mountain.DUMMY_MOUNTAIN,
        isCompleteBtnEnabled = true,
        isLocationPermissionGranted = true,
        onDetailClicked = {},
        onCompleteBtnClicked = {})
}

@Preview(showBackground = true)
@Composable
fun PrevDifficultyIcons() {
    DifficultyIcons(difficulty = "EASY")

}