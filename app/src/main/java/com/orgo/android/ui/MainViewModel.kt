package com.orgo.android.ui

import androidx.lifecycle.ViewModel
import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.model.data.UserAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userAuthRepository : UserAuthRepository
) :ViewModel(){
    val userAuthState: StateFlow<UserAuthState> = userAuthRepository.userAuthState

}