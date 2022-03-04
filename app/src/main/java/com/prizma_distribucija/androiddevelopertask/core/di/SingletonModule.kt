package com.prizma_distribucija.androiddevelopertask.core.di

import com.prizma_distribucija.androiddevelopertask.core.util.Constants
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.TechnicalTaskApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.API_BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideTechnicalTaskApiService(retrofit: Retrofit): TechnicalTaskApiService =
        retrofit.create(TechnicalTaskApiService::class.java)
}