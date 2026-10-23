package com.hmst.pulsar.core.network.di

import com.hmst.pulsar.core.network.repository.PulsarRepositoryImpl
import com.hmst.pulsar.core.network.repository.PulsarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindPulsarRepository(impl: PulsarRepositoryImpl): PulsarRepository
}
