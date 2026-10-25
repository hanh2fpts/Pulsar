package com.hmst.pulsar.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Shared HTTP stack. Each feature builds its own Retrofit from [provideRetrofitBuilder]
 * with its own base URL, so one app can talk to many APIs.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    /** Not a singleton: Retrofit.Builder is mutable, every caller gets a fresh one */
    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient, json: Json): Retrofit.Builder =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
}
