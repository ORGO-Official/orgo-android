package com.orgo.core.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


/**
 * [NavigationBarItem].
 *
 * @param selected Whether this item is selected.
 * @param onClick The callback to be invoked when this item is selected.
 * @param icon The item icon content.
 * @param modifier Modifier to be applied to this item.
 * @param selectedIcon The item icon content when selected.
 * @param enabled controls the enabled state of this item. When `false`, this item will not be
 * clickable and will appear disabled to accessibility services.
 * @param label The item text label content.
 * @param alwaysShowLabel Whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 */

@Composable
fun RowScope.OrgoNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = false,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = OrgoNavigationBarDefaults.navigationSelectedItemColor(),
            unselectedIconColor = OrgoNavigationBarDefaults.navigationItemContentColor(),
            selectedTextColor = OrgoNavigationBarDefaults.navigationSelectedItemColor(),
            unselectedTextColor = OrgoNavigationBarDefaults.navigationItemContentColor(),
            indicatorColor = OrgoNavigationBarDefaults.navigationIndicatorColor(),
        ),
    )
}

object OrgoNavigationBarDefaults {
    @Composable
    fun navigationItemContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = Color.Black

    @Composable
    fun navigationIndicatorColor() = Color.White.copy(.1f)
}
