package com.hmst.pulsar.feature.radar.ui

import androidx.lifecycle.ViewModel
import com.hmst.pulsar.core.network.usecase.ObserveIndexQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveStreamStatusUseCase
import com.hmst.pulsar.core.network.usecase.StartQuoteStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RadarViewModel @Inject constructor(
    observeQuotesUC: ObserveQuotesUseCase,
    observeStreamStatusUC: ObserveStreamStatusUseCase,
    observeIndexQuotesUC: ObserveIndexQuotesUseCase,
    startStreamUC: StartQuoteStreamUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RadarUiState())
    val uiState: StateFlow<RadarUiState> = _uiState.asStateFlow()

    init {
        startStreamUC.invoke()
    }
}
