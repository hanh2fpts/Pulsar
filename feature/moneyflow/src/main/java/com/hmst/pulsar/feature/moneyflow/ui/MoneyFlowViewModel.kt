package com.hmst.pulsar.feature.moneyflow.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MoneyFlowViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MoneyFlowUiState())
    val uiState: StateFlow<MoneyFlowUiState> = _uiState.asStateFlow()
}
