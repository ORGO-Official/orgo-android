package com.orgo.feature.map.ui.search

import android.app.Activity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.designsystem.theme.Pretendard
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.ui.component.BasicDivider
import com.orgo.core.ui.component.PretendardText
import timber.log.Timber


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchedMountains: List<Mountain>,
    searchText: String,
    onResultClicked: (String) -> Unit
) {
    val context = LocalContext.current
    val isNoResult = searchedMountains.isEmpty()

    Crossfade(
        targetState = isNoResult,
        modifier = modifier.fillMaxSize(),
    ) { noResult ->
        if (noResult) {
            NoResultBox()
        } else {
            SearchResult(
                modifier = modifier.imePadding(),
                searchText = searchText,
                mountains = searchedMountains,
                onResultClicked = onResultClicked
            )

        }
    }
}


@Composable
fun SearchResult(
    modifier: Modifier = Modifier,
    searchText: String,
    mountains: List<Mountain>,
    onResultClicked: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(72.dp))
        LazyColumn(
            modifier = modifier
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin)),
        ) {
            items(mountains) { mountain ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable() {
                            onResultClicked(mountain.name)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        painter = painterResource(id = OrgoIcon.Location),
                        contentDescription = null,
                        tint = colorResource(id = OrgoColor.Gray)
                    )
                    Column(modifier = Modifier.fillMaxWidth()) {
                        HighlightCommonText(
                            searchText = searchText,
                            resultText = mountain.name
                        )
                        PretendardText(
                            text = mountain.address,
                            color = colorResource(id = OrgoColor.Gray),
                        )
                    }
                }
                BasicDivider()
            }
        }
    }
}

@Composable
fun HighlightCommonText(
    searchText: String,
    resultText: String,
) {
    val commonText = findCommonSubstring(searchText, resultText)
    val remainText = resultText.replace(commonText, "")
    Text(
        // 한 글자씩 처리해주기 위함
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorResource(id = R.color.ios_blue))) {
                append(commonText)
            }
            if (remainText.isNotEmpty()) {
                append(remainText)
            }
        },
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    )
}

fun findCommonSubstring(string1: String, string2: String): String {
    val minLength = minOf(string1.length, string2.length)
    var commonSubstring = ""

    for (i in 0 until minLength) {
        if (string1[i] == string2[i]) {
            commonSubstring += string1[i]
        } else {
            break
        }
    }

    return commonSubstring
}

@Composable
fun NoResultBox(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = OrgoIcon.NoResult),
            contentDescription = null,
            tint = colorResource(id = R.color.light_gray)
        )
        PretendardText(
            modifier = modifier
                .padding(vertical = dimensionResource(id = R.dimen.vertical_margin)),
            text = "검색 결과가 없습니다.",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
        PretendardText(
            text = "단어의 철자가 정확한지 확인해 주세요.",
            color = colorResource(id = OrgoColor.Gray),
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
        PretendardText(
            text = "다른 검색어로 검색해보세요.",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}