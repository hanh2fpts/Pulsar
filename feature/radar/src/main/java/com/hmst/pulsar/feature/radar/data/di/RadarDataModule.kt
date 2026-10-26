package com.hmst.pulsar.feature.radar.data.di

import com.hmst.pulsar.feature.radar.data.repository.TradeAmountHistoryRepositoryImpl
import com.hmst.pulsar.feature.radar.domain.repository.TradeAmountHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RadarDataModule {

    @Binds
    abstract fun bindTradeAmountHistoryRepository(
        impl: TradeAmountHistoryRepositoryImpl,
    ): TradeAmountHistoryRepository
}
