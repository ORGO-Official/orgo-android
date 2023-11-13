package com.orgo.feature.map.ui.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orgo.core.designsystem.theme.OrgoColor
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.icon.OrgoIcon
import com.orgo.core.designsystem.icon.Icon
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.ui.component.PretendardText
import com.orgo.core.ui.custom_modifier.customRoundedCornerShadow


@ExperimentalMaterial3Api
@Composable
fun MapSearchBar(
    modifier: Modifier = Modifier,
    active : Boolean,
    onSearchBarClicked : () -> Unit,
    onQueryChange : (String) -> Unit,
    onSearch: (String) -> Unit,
    onBackClicked : () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val customTextSelectionColors = TextSelectionColors(
        handleColor = colorResource(id = OrgoColor.OrgoGreen),
        backgroundColor = colorResource(id = OrgoColor.OrgoGreen).copy(alpha = 0.4f)
    )
    Row(
        modifier  = modifier
            .height(72.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
    ){
        Surface(
            modifier = modifier
                .customRoundedCornerShadow(
                    color = colorResource(id = OrgoColor.Black).copy(.2f),
                    roundedCornerSize = dimensionResource(id = R.dimen.searchBar_roundedCorner),
                    blurRadius = if(active) 0.dp else 7.dp
                )
            ,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.searchBar_roundedCorner)),
            border = if(active) BorderStroke(1.dp, Color.Gray) else null
        ) {
            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                DockedSearchBar(
                    modifier = modifier,
                    query = query,
                    onQueryChange = {
                        query = it
                        onQueryChange(it)
                    },
                    onSearch = onSearch,
                    active = false,
                    onActiveChange = {
                        if(it){
                            onSearchBarClicked()
                        }
                    },
                    enabled = true,
                    placeholder = {
                        PretendardText (
                            modifier = Modifier,
                            text = if(active) "산 이름 검색" else "계양산",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = if(active) R.color.light_gray else R.color.gray)
                        )
                    },
                    leadingIcon = {
                        IconButton(
                            enabled = active,
                            onClick = onBackClicked
                        ) {
                            Icon(
                                iconType = IconType.DrawableResourceIcon(
                                    if(active) OrgoIcon.ArrowBack else OrgoIcon.Search
                                ),
                                contentDescription = null,
                                tint = colorResource(id = R.color.black)
                            )
                        }
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            Icon(
                                modifier = Modifier.clickable {
                                    query = ""
                                    onQueryChange(query)
                                },
                                imageVector = Icons.Default.Close, contentDescription = null
                            )
                        }
                    },
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.White,
                        dividerColor = Color.Transparent,
                        inputFieldColors = TextFieldDefaults.textFieldColors(
                            cursorColor = colorResource(id = R.color.ios_blue),
                            selectionColors = customTextSelectionColors,
                            placeholderColor = colorResource(id = R.color.light_gray),
                            disabledPlaceholderColor = colorResource(id = R.color.light_gray),
                            focusedLeadingIconColor = colorResource(id = R.color.black),
                            unfocusedLeadingIconColor = colorResource(id = R.color.black),
                        )
                    ),
                ) {}
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrevSearchBar() {
    Box() {
        MapSearchBar(
            active = true,
            onSearchBarClicked = {},
            onQueryChange = {},
            onSearch = {},
            onBackClicked = {}
        )
    }
}