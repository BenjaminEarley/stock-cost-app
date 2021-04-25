package com.benjaminearley.stockcost.repository.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EngineModule {
    @Singleton
    @Provides
    fun provideEngine(): OkHttpClient = OkHttpClient.Builder().build()
}