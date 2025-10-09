package com.nb.coininfo.ui.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.let

data class SplashUiState(
    val isLoading: Boolean = true,
)

@HiltViewModel
class SplashViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        loadSplash()
    }

    fun loadSplash() {
        viewModelScope.launch {
            delay(5_000)
            _uiState.update {
                it.copy(false)
            }
        }
    }

}