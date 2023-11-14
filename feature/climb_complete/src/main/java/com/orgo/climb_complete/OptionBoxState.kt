package com.orgo.climb_complete

sealed class OptionBoxState {
    object Closed : OptionBoxState()
    object SelectPicture : OptionBoxState()
    object Share : OptionBoxState()

}