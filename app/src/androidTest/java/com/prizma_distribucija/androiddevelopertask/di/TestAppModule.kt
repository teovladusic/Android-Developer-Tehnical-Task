package com.prizma_distribucija.androiddevelopertask.di

import com.prizma_distribucija.androiddevelopertask.core.di.AppModule
import com.prizma_distribucija.androiddevelopertask.core.util.AndroidTestDispatchers
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.fakes.*
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.CreatePostRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.FeedRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper.AuthorMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper.FeedMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper.VideoMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases.CreatePostUseCase
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases.GetFeedUseCase
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.*
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.LoginUserUseCase
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

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
    fun provideProfileRepository(): ProfileRepository = ProfileRepositoryFakeImpl()

    @Provides
    @Singleton
    fun provideCreatePostRepository(): CreatePostRepository = CreatePostRepositoryFakeImpl()

    @Provides
    @Singleton
    fun provideCreatePostUseCase(createPostRepository: CreatePostRepository) =
        CreatePostUseCase(createPostRepository)

    @Provides
    @Singleton
    fun provideVideoMapper() = VideoMapper()

    @Provides
    @Singleton
    fun provideAuthorMapper() = AuthorMapper()

    @Provides
    @Singleton
    fun provideFeedMapper(authorMapper: AuthorMapper, videoMapper: VideoMapper) =
        FeedMapper(authorMapper, videoMapper)

    @Provides
    @Singleton
    fun provideFeedRepository(): FeedRepository = FeedRepositoryFakeImpl()

    @Provides
    @Singleton
    fun provideGetFeedUseCase(feedRepository: FeedRepository) = GetFeedUseCase(feedRepository)

}