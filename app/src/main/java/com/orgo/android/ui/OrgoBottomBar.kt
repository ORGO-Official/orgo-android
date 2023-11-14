package com.orgo.android.ui

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.orgo.android.R
import com.orgo.android.navigation.TopLevelDestination
import com.orgo.core.ui.component.OrgoNavigationBarItem
import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.ui.custom_modifier.customRectShadow


@Composable
fun OrgoBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = Modifier
            .height(72.dp)
            .customRectShadow(
                color = colorResource(id = R.color.black).copy(.1f),
                offsetY = (-4).dp,
                blurRadius = 50.dp
            ),
        containerColor = colorResource(id = R.color.white),
        contentColor = colorResource(id = R.color.white)
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            OrgoNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIconType
                    } else {
                        destination.unselectedIconType
                    }
                    when(icon){
                        is IconType.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )
                        is IconType.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        }
    }

}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

