package com.orgo.core.designsystem.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntSize

object OrgoAnimation {
    private const val AnimationDurationMillis: Int = MotionTokens.DurationMedium.toInt()
    val SizeAnimationSpec: FiniteAnimationSpec<IntSize> =
        tween(durationMillis = AnimationDurationMillis, easing = FastOutSlowInEasing)
     val OpacityAnimationSpec: FiniteAnimationSpec<Float> =
        tween(durationMillis = AnimationDurationMillis, easing = FastOutSlowInEasing)

    val MapViewEnterTransition: EnterTransition =
        fadeIn(OpacityAnimationSpec) + expandHorizontally(
            animationSpec = SizeAnimationSpec,
            expandFrom = Alignment.End
        )
    val MapViewExitTransition : ExitTransition =
        fadeOut(OpacityAnimationSpec) + shrinkHorizontally (
            animationSpec = SizeAnimationSpec,
            shrinkTowards = Alignment.End
        )

    val SearchScreenEnterTransition : EnterTransition =
        fadeIn(OpacityAnimationSpec) + expandHorizontally(
            animationSpec = SizeAnimationSpec,
            expandFrom = Alignment.Start
        )
    val SearchScreenExitTransition : ExitTransition =
        fadeOut(OpacityAnimationSpec) + shrinkHorizontally (
            animationSpec = SizeAnimationSpec,
            shrinkTowards = Alignment.Start
        )

    val BottomSheetEnterTransition : EnterTransition =
        fadeIn(OpacityAnimationSpec) + expandVertically(
            animationSpec = SizeAnimationSpec,
        )
    val BottomSheetExitTransition : ExitTransition =
        fadeOut(OpacityAnimationSpec) + shrinkVertically (
            animationSpec = SizeAnimationSpec,
        )
}
object MotionTokens{
    const val DurationMedium = 300.0
}