package com.hmst.pulsar.core.network.di

import com.hmst.pulsar.core.network.BuildConfig
import com.hmst.pulsar.core.network.dnse.protocol.DnseSignatureInterceptor
import com.hmst.pulsar.core.network.dnse.rest.DnseRestApi
import com.hmst.pulsar.core.network.util.DnseUtil.DNSE_REST_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DnseModule {
    @Provides
    @Singleton
    fun provideDnseRestApi(
        client: OkHttpClient, builder:
        Retrofit.Builder
    ): DnseRestApi =
        builder
            .baseUrl(DNSE_REST_URL)
            // client riêng có interceptor ký, các API khác không bị ảnh hưởng
            .client(
                client.newBuilder()
                    .addInterceptor(
                        DnseSignatureInterceptor(
                            BuildConfig.DNSE_API_KEY,
                            BuildConfig.DNSE_API_SECRET
                        )
                    )
                    .build()
            )
            .build()
            .create()
}