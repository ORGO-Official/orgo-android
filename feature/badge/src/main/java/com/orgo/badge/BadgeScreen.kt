package com.orgo.badge

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.orgo.badge.util.badgeIdToData
import com.orgo.core.common.util.getTestLocation
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.model.data.badge.AcquiredBadge
import com.orgo.core.model.data.badge.Badge
import com.orgo.core.model.data.badge.UnacquiredBadge
import com.orgo.core.ui.component.BasicDivider
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.core.ui.component.ErrorScreen


import com.orgo.core.ui.component.PretendardText
import kotlinx.coroutines.launch
import kotlin.math.min

val default_month_badges = listOf(
    UnacquiredBadge(38,"2023년 1월에 완등 성공하기"),
    UnacquiredBadge(39,"2023년 2월에 완등 성공하기"),
    UnacquiredBadge(40,"2023년 3월에 완등 성공하기"),
    UnacquiredBadge(41,"2023년 4월에 완등 성공하기"),
    UnacquiredBadge(42,"2023년 5월에 완등 성공하기"),
    UnacquiredBadge(43,"2023년 6월에 완등 성공하기"),
    UnacquiredBadge(44,"2023년 7월에 완등 성공하기"),
    UnacquiredBadge(45,"2023년 8월에 완등 성공하기"),
)

val earlyBirdBadge = AcquiredBadge(99,"초기 회원","2023-10-11T12:25:27.069836","출시 1개월 내에 회원가입 시 얻을 수 있는 뱃지 입니다.")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BadgeRoute(
    modifier: Modifier = Modifier,
    viewModel: BadgeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.badgeUiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val bottomSheetState = rememberSheetState(true)

    val selectedBadge =
        if (uiState is BadgeUiState.Success) (uiState as BadgeUiState.Success).selectedBadge else null
    LaunchedEffect(uiState) {
        if (selectedBadge != null) {
            bottomSheetState.show()
        }
    }

    BackHandler(uiState is BadgeUiState.Success) {
        if (selectedBadge != null) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
            viewModel.updateSelectedBadge(null)
        }
    }

    when(uiState){
        is BadgeUiState.Success -> {
            if (bottomSheetState.isVisible && selectedBadge != null) {
                BadgeModalBottomSheet(
                    sheetState = bottomSheetState,
                    selectedBadge =  selectedBadge,
                    totalBadges = (uiState as BadgeUiState.Success).badges+ default_month_badges + earlyBirdBadge,
                    onDismissRequest = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                        viewModel.updateSelectedBadge(null)
                    }
                )
            }
            BadgeScreen(
                badges = (uiState as BadgeUiState.Success).badges,
                scrollState = scrollState,
                onAcquiredClicked = { badge ->
                    viewModel.updateSelectedBadge(badge)
                }
            )
        }
        is BadgeUiState.Loading -> {
            CenterCircularProgressIndicator()
        }

        is BadgeUiState.Unauthenticated -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                PretendardText(
                    text = "로그인이 필요합니다.",
                    color = colorResource(id = OrgoColor.Gray),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        else -> {
            ErrorScreen(
                errorMsg = (uiState as BadgeUiState.Failure).message,
                onRefreshClicked = {
                    viewModel.fetchBadges()
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeScreen(
    badges : List<Badge>,
    scrollState : ScrollState,
    onAcquiredClicked : (Badge) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = colorResource(id = OrgoColor.OrgoGreen),
    ) {
        Surface(
            modifier = Modifier
                .padding(vertical = 30.dp)
                .padding(horizontal = 16.dp)
            ,
            color = colorResource(id = OrgoColor.Black).copy(0.45f),
            shape = RoundedCornerShape(30.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
                    .padding(16.dp)
                    ,
            ) {
                BadgeDivider(text = "나는야 얼리버드")
                BadgeRows(
                    badges = listOf(earlyBirdBadge),
                    onAcquiredClicked = onAcquiredClicked
                )

                BadgeDivider(text = "진정한 등산왕")
                BadgeRows(
                    badges = badges.filter{it.id in 31..33}.sortedBy { it.id },
                    onAcquiredClicked = onAcquiredClicked
                )
                BadgeDivider(text = "꾸준함이 생명")
                BadgeRows(
                    badges = default_month_badges + badges.filter{it.id in 34..37}.sortedBy { it.id },
                    onAcquiredClicked = onAcquiredClicked
                )
                BadgeDivider(text = "이곳저곳 오르GO")
                val acquiredBadges = badges.filterIsInstance<AcquiredBadge>()
                val unacquiredBadges = badges.filterIsInstance<UnacquiredBadge>()

                val mountain_badges = if (acquiredBadges.isNotEmpty()) {
                    val maxAcquiredId = acquiredBadges.maxByOrNull { it.id }!!.id
                    badges.filter { it.id == maxAcquiredId }
                } else if (unacquiredBadges.isNotEmpty()) {
                    val minUnacquiredId = unacquiredBadges.minByOrNull { it.id }!!.id
                    badges.filter { it.id == minUnacquiredId }
                } else {
                    emptyList()
                }

                BadgeRows(
                    badges = (1..30 step 3).map { maxId ->
                        val group = badges.filter { it.id in maxId until maxId + 3 }
                        val acquiredBadges = group.filterIsInstance<AcquiredBadge>()
                        val unacquiredBadges = group.filterIsInstance<UnacquiredBadge>()

                        val mountain_badges = if (acquiredBadges.isNotEmpty()) {
                            val maxAcquiredId = acquiredBadges.maxByOrNull { it.id }!!.id
                            group.filter { it.id == maxAcquiredId }
                        } else if (unacquiredBadges.isNotEmpty()) {
                            val minUnacquiredId = unacquiredBadges.minByOrNull { it.id }!!.id
                            group.filter { it.id == minUnacquiredId }
                        } else {
                            emptyList()
                        }
                        mountain_badges[0]
                    }.sortedBy { it.id },
                    onAcquiredClicked = onAcquiredClicked
                )
            }
        }

    }
}

@Composable
fun BadgeRows(
    badges: List<Badge>,
    onAcquiredClicked : (Badge) -> Unit,
) {
    val itemsPerRow = 3
    val totalRows = (badges.size + itemsPerRow - 1) / itemsPerRow
    repeat(totalRows) { rowIndex ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in rowIndex * itemsPerRow until min((rowIndex + 1) * itemsPerRow, badges.size)) {
                BadgeBox(badge = badges[i]){
                    onAcquiredClicked(it)
                }
            }
        }
    }
}

@Composable
fun BadgeBox(
    modifier : Modifier = Modifier,
    badge: Badge,
    onAcquiredClicked : (Badge) -> Unit,
){
    val context = LocalContext.current
    val (badgeResId,detail) = badgeIdToData(badge.id)
    val isAcquired = badge is AcquiredBadge
    Column(
        modifier = modifier.clickable(
            onClick = {
                if(!isAcquired){
                    Toast.makeText(context, "획득조건 : ${badge.objective}",Toast.LENGTH_SHORT).show()
                }else onAcquiredClicked(badge)
            },
            interactionSource =  MutableInteractionSource(),
            indication = null
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BadgeImage(
            badgeResId = badgeResId,
            isAcquired = isAcquired
        )

        PretendardText(
            text = detail,
            color = colorResource(OrgoColor.White)
        )
    }
}

@Composable
fun BadgeImage(
    badgeResId : Int,
    isAcquired : Boolean
) {
    Image(
        modifier = Modifier.size(90.dp),
        painter = painterResource(badgeResId),
        colorFilter = if(isAcquired) null else ColorFilter.tint(
            colorResource(id = OrgoColor.Black).copy(.8f),
            blendMode = BlendMode.SrcAtop
        ),
        contentDescription = null
    )
}


@Composable
fun BadgeDivider(
    modifier: Modifier = Modifier,
    text: String
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicDivider(
            modifier = modifier.weight(1f),
            color = colorResource(id = OrgoColor.White)
        )
        PretendardText(
            text = text,
            modifier = modifier.padding(horizontal = 2.dp),
            color = colorResource(id = OrgoColor.White),
        )
        BasicDivider(
            modifier = modifier.weight(1f),
            color = colorResource(id = OrgoColor.White)
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrevBadgeScreen() {
    val scrollState = rememberScrollState()
    val bottomSheetState = rememberSheetState()
    BadgeScreen(
        badges = listOf(
            AcquiredBadge(1,"","",""),
            UnacquiredBadge(1,""),
            AcquiredBadge(2,"","",""),
        ),
        scrollState = scrollState,
        onAcquiredClicked = {}
    )
}