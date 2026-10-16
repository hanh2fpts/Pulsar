package com.hmst.pulsar.feature.priceboard.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PriceBoardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PriceBoardUiState())
    val uiState: StateFlow<PriceBoardUiState> = _uiState.asStateFlow()
}
