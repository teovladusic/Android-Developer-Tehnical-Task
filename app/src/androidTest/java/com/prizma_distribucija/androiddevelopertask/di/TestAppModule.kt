package com.prizma_distribucija.androiddevelopertask.di

import com.prizma_distribucija.androiddevelopertask.core.di.AppModule
import com.prizma_distribucija.androiddevelopertask.core.util.AndroidTestDispatchers
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.fakes.DataStoreManagerFakeImpl
import com.prizma_distribucija.androiddevelopertask.fakes.LoginRepositoryFakeImpl
import com.prizma_distribucija.androiddevelopertask.fakes.ProfileRepositoryFakeImpl
import com.prizma_distribucija.androiddevelopertask.fakes.SignUpRepositoryFakeImpl
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.FeedApiService
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.repository.ProfileRepositoryImpl
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.*
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.LoginUserUseCase
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton
import kotlin.math.sign

@ExperimentalCoroutinesApi
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(): DataStoreManager = DataStoreManagerFakeImpl()

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = AndroidTestDispatchers()

    @Provides
    @Singleton
    fun provideAuthenticationHelper(
        dataStoreManager: DataStoreManager,
        dispatcherProvider: DispatcherProvider
    ): AuthenticationHelper = AuthenticationHelperImpl(dataStoreManager, dispatcherProvider)

    @Provides
    @Singleton
    fun provideSignUpRepository(): SignUpRepository = SignUpRepositoryFakeImpl()

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(signUpRepository: SignUpRepository) =
        RegisterUserUseCase(signUpRepository)

    @Provides
    @Singleton
    fun provideLoginRepository(): LoginRepository = LoginRepositoryFakeImpl()

    @Provides
    @Singleton
    fun provideLoginUserUseCase(loginRepository: LoginRepository) =
        LoginUserUseCase(loginRepository)

    @Provides
    @Singleton
    fun provideProfileRepository(): ProfileRepository
        = ProfileRepositoryFakeImpl()

}