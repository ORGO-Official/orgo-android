package com.orgo.feature.mountain_detail.mountain_detail

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.theme.Pretendard
import com.orgo.core.model.data.Restaurant
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.core.ui.component.PretendardText


@Composable
fun MountainRestaurantRow(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    restaurants: List<Restaurant> = listOf(Restaurant.DUMMY_RESTAURANT),
    onRestaurantClicked: (String) -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        if (isLoading){
            CenterCircularProgressIndicator()
        } else if (restaurants.isEmpty()){
            PretendardText(text = "주변 식당을 불러올 수 없습니다.")
        }else{
            LazyRow(
                modifier = modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.vertical_margin_half)),
                verticalAlignment = Alignment.Top,
            ) {
                items(restaurants) { restaurant ->
                    Column(
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.restaurant_item_width))
                            .height(intrinsicSize = IntrinsicSize.Max)
                            .clickable(
                                onClick = {
                                    if (restaurant.externalLink.isNotEmpty()) {
                                        onRestaurantClicked(restaurant.externalLink)
                                    } else {
                                        Toast
                                            .makeText(context, "해당 링크가 없습니다", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                },
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = restaurant.imageUrl,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.restaurant_item_width))
                                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.image_roundedCorner))),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            placeholder = painterResource(id = R.drawable.placeholder)
                        )
                        Column(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .height(dimensionResource(id = R.dimen.restaurant_textbox_height)),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PretendardText(
                                text = restaurant.formattedName,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }
                        PretendardText(
                            text = "${restaurant.distanceInKilometers}km",
                            fontSize = 13.sp,
                            color = colorResource(id = R.color.gray)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    heightDp = 200
)
@Composable
fun PrevMountainRestaurantRow() {
    MountainRestaurantRow(
        isLoading = false,
        restaurants = listOf(),
        onRestaurantClicked = {}
    )
}