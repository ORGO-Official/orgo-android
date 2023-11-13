package com.orgo.feature.mountain_detail.mountain_detail

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.model.data.Restaurant
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.ui.component.BasicDivider
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.core.ui.component.ErrorScreen
import com.orgo.core.ui.component.MountainDetailInfo
import com.orgo.core.ui.component.PretendardText
import com.orgo.feature.mountain_detail.MountainDetailUiState
import com.orgo.feature.mountain_detail.MountainDetailViewModel

const val dummyMountainImgUrl =
    "https://i.namu.wiki/i/p8BaObNXo0nKfYjI1B4u2_gl6YaDvmZ57hk8o7pUwI2PoXy97PqiD9nI9fjgeiNVhCsAyP1Rc__w-m3Q_ZACJ1J0jjT665hL7CG1OvHW9cg649ANf2nAgZwMqUH24w-AW0pnHn2t53BUO_qTnkw7TQ.webp"

@Composable
internal fun MountainDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: MountainDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onRestaurantClicked: (String) -> Unit
) {
    val uiState by viewModel.mountainDetailUiState.collectAsStateWithLifecycle()

    when (uiState) {
        is MountainDetailUiState.Loading -> {
            CenterCircularProgressIndicator()
        }
        is  MountainDetailUiState.Success -> {
            MountainDetailScreen(
                modifier = modifier,
                uiState = uiState as MountainDetailUiState.Success,
                onBackClicked = onBackClicked,
                onRestaurantClicked = onRestaurantClicked
            )

        }
        is MountainDetailUiState.Failure -> {
            ErrorScreen(
                errorMsg = (uiState as MountainDetailUiState.Failure).message,
                onRefreshClicked = {
                    viewModel.fetchMountainDetail()
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MountainDetailScreen(
    modifier: Modifier = Modifier,
    uiState: MountainDetailUiState.Success,
    onBackClicked: () -> Unit,
    onRestaurantClicked: (String) -> Unit
) {
    val mountainDetailScrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MountainDeatilTopAppBar(
                title = uiState.mountain.name,
                onBackClicked = onBackClicked
            )
        }
    ) { padding ->
        MountainDetailLayout{ mountainImageBoxHeight, backgroundImageHeight ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Box(
                    modifier = modifier.height(mountainImageBoxHeight)
                ) {
                    MountainBackgroundImage(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(backgroundImageHeight),
                        imageUrl = uiState.mountain.backgroundImage
                    )
                    MountainProfileImage(
                        modifier = modifier
                            .align(Alignment.BottomCenter)
                            .size(120.dp),
                        imageUrl = uiState.mountain.mainImage
                    )
                }
                MountainDetailInfo(
                    modifier = modifier.padding(dimensionResource(id = R.dimen.vertical_margin)),
                    mountain = uiState.mountain
                )
                BasicDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin)),
                )
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
                        .weight(1f)
                        .verticalScroll(mountainDetailScrollState)
                ) {
                    MountainFeatureTagRow(
                        modifier = modifier.padding(vertical = dimensionResource( id = R.dimen.vertical_margin)),
                        featureTag = uiState.mountain.featureTag
                    )
                    BasicDivider(
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PretendardText(
                        modifier = Modifier.padding(top= 8.dp),
                        text = "인근 식당"
                    )
                    MountainRestaurantRow(
                        modifier = modifier.weight(1f),
                        isLoading = uiState.isLoadingRestaurants,
                        restaurants = uiState.restaurants,
                        onRestaurantClicked = onRestaurantClicked
                    )
                }
            }
        }
    }
}

@Composable
fun MountainDetailLayout(
    modifier: Modifier = Modifier,
    content : @Composable (Dp,Dp) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val backgroundImageHeight = maxHeight.times(0.35f)
        val backgroundImageBottomPadding = 20.dp
        val mountainImageBoxHeight = backgroundImageHeight + backgroundImageBottomPadding
        content(mountainImageBoxHeight, backgroundImageHeight)
    }
}


@Composable
fun MountainBackgroundImage(
    modifier: Modifier = Modifier,
    imageUrl: String = dummyMountainImgUrl,
) {
    AsyncImage(
        model = imageUrl,
        modifier = modifier,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.placeholder),
        contentScale = ContentScale.FillBounds,
        colorFilter = ColorFilter.tint(
            color = colorResource(OrgoColor.Black).copy(0.08f),
            blendMode = BlendMode.ColorBurn
        )
    )
}

@Composable
fun MountainProfileImage(
    modifier: Modifier = Modifier,
    imageUrl: String = dummyMountainImgUrl,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(48.dp), clip = true),
        placeholder = painterResource(id = R.drawable.placeholder),
        contentScale = ContentScale.Crop
    )
}


@Preview(showBackground = true)
@Composable
fun PrevMountainDetailScreen() {
    MountainDetailScreen(
        uiState = MountainDetailUiState.Success(
            isLoadingRestaurants = false,
            mountain = Mountain.DUMMY_MOUNTAIN,
            restaurants = listOf(
                Restaurant.DUMMY_RESTAURANT,
                Restaurant.DUMMY_RESTAURANT2
            )
        ),
        onBackClicked = { /*TODO*/ },
        onRestaurantClicked ={}
    )
}

@Preview(showBackground = true)
@Composable
fun PrevMountainDetailScreenNoRestaurants() {
    MountainDetailScreen(
        uiState = MountainDetailUiState.Success(
            isLoadingRestaurants = false,
            mountain = Mountain.DUMMY_MOUNTAIN,
            restaurants = listOf()
        ),
        onBackClicked = { },
        onRestaurantClicked ={}
    )
}

@Preview(showBackground = true)
@Composable
fun PrevMountainDetailScreenLoadingRestaurants() {
    MountainDetailScreen(
        uiState = MountainDetailUiState.Success(
            isLoadingRestaurants = true,
            mountain = Mountain.DUMMY_MOUNTAIN,
            restaurants = listOf()
        ),
        onBackClicked = { },
        onRestaurantClicked ={}
    )
}