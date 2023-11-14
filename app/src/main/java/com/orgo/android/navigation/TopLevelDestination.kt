package com.orgo.android.navigation

import com.orgo.core.designsystem.icon.IconType
import com.orgo.core.designsystem.icon.IconType.DrawableResourceIcon
import com.orgo.core.designsystem.icon.OrgoIcon

enum class TopLevelDestination (
    val selectedIconType : IconType,
    val unselectedIconType : IconType,
){
    MAP(
        selectedIconType = DrawableResourceIcon(OrgoIcon.Home),
        unselectedIconType = DrawableResourceIcon(OrgoIcon.HomeBorder),
    ),
    BADGE(
        selectedIconType = DrawableResourceIcon(OrgoIcon.Badge),
        unselectedIconType = DrawableResourceIcon(OrgoIcon.BadgeBorder),
    ),
    MYPAGE(
        selectedIconType = DrawableResourceIcon(OrgoIcon.Mypage),
        unselectedIconType = DrawableResourceIcon(OrgoIcon.MypageBorder),
    ),
}