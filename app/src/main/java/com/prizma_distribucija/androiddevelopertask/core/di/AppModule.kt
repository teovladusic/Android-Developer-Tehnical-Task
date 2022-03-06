package com.prizma_distribucija.androiddevelopertask.core.di

import android.content.Context
import com.prizma_distribucija.androiddevelopertask.core.util.Constants
import com.prizma_distribucija.androiddevelopertask.core.util.DefaultDispatchers
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.FeedApiService
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.AuthApiService
import com.prizma_distribucija.androiddevelopertask.feature_login.data.repository.LoginRepositoryImpl
import com.prizma_distribucija.androiddevelopertask.feature_login.data.repository.SignUpRepositoryImpl
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.*
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.API_BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideFeedApiService(retrofit: Retrofit): FeedApiService =
        retrofit.create(FeedApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideDataStoreManager(
        @ApplicationContext appContext: Context
    ): DataStoreManager = DataStoreManagerImpl(appContext)

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatchers()

    @Provides
    @Singleton
    fun provideAuthenticationHelper(
        dataStoreManager: DataStoreManager,
        dispatcherProvider: DispatcherProvider
    ): AuthenticationHelper = AuthenticationHelperImpl(dataStoreManager, dispatcherProvider)

    @Provides
    @Singleton
    fun provideSignUpRepository(
        authApiService: AuthApiService,
        dispatcherProvider: DispatcherProvider
    ): SignUpRepository = SignUpRepositoryImpl(authApiService, dispatcherProvider)


    @Provides
    @Singleton
    fun provideRegisterUserUseCase(signUpRepository: SignUpRepository) =
        RegisterUserUseCase(signUpRepository)

    @Provides
    @Singleton
    fun provideLoginRepository(
        authApiService: AuthApiService,
        dispatcherProvider: DispatcherProvider
    ): LoginRepository = LoginRepositoryImpl(authApiService, dispatcherProvider)
}