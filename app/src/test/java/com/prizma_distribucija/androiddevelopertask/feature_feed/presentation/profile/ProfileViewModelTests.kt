package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.profile

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.GetUserResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Plan
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.User
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases.GetProfileUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class ProfileViewModelTests {

    /**
     * test always passes
     */
    /*@Test
    fun `init, should emit getProfile status`() = runBlocking {
        val dispatcherProvider = TestDispatchers()
        val getUserResponse = GetUserResponse(false, null, "error message")

        val profileRepository = mock(ProfileRepository::class.java)
        `when`(profileRepository.getUser(1)).thenReturn(getUserResponse)
        val getProfileUseCase = GetProfileUseCase(profileRepository)

        lateinit var viewModel: ProfileViewModel

        val job = launch {
            viewModel.getUserStatus.test {
                val status = awaitItem()
                assertThat(status).isInstanceOf(Resource.Loading::class.java)

                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel = ProfileViewModel(dispatcherProvider, getProfileUseCase)

        job.join()
        job.cancel()
    }*/

    @Test
    fun onGetUserSuccess_shouldEmitUser() = runTest {
        val dispatcherProvider = TestDispatchers()

        val user = User(
            id = 1,
            name = "name",
            avatar = "avatar",
            views = "0",
            videoPlays = "0",
            athletes = emptyList(),
            plan = Plan("type")
        )

        val getUserResponse = GetUserResponse(true, user, null)

        val profileRepository = mock(ProfileRepository::class.java)
        `when`(profileRepository.getUser(1)).thenReturn(getUserResponse)
        val getProfileUseCase = GetProfileUseCase(profileRepository)

        val viewModel = ProfileViewModel(dispatcherProvider, getProfileUseCase)

        viewModel.onGetUserSuccess(user)

        assertThat(viewModel.user.value).isEqualTo(user)
    }


}