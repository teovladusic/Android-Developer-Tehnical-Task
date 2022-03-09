package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AthleteDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PlanDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Athlete
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.GetUserResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Plan
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class GetProfileUseCaseTests {

    @Test
    fun `invoke, response is successful, should emit loading and success with correct data`() =
        runTest {
            val id = 1
            val plan = Plan("type")
            val athletes = listOf(Athlete(1, "name", "avatar"))
            val user = User(
                id = id,
                name = "name",
                avatar = "avatar",
                views = "views",
                videoPlays = "videoPlays",
                plan = plan,
                athletes = athletes
            )

            val getUserResponse = GetUserResponse(true, user, null)
            val profileRepository = mock(ProfileRepository::class.java)
            `when`(profileRepository.getUser(id)).thenReturn(getUserResponse)
            val getProfileUseCase = GetProfileUseCase(profileRepository)

            getProfileUseCase(id).test {
                val firstEmission = awaitItem()
                assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)
                val secondEmission = awaitItem()
                assertThat(secondEmission).isInstanceOf(Resource.Success::class.java)
                assertThat(secondEmission.message).isNull()
                assertThat(secondEmission.data).isEqualTo(user)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `invoke, response is failure, should emit loading and error with correct data`() =
        runTest {
            val id = 1

            val getUserResponse = GetUserResponse(false, null, "error message")
            val profileRepository = mock(ProfileRepository::class.java)
            `when`(profileRepository.getUser(id)).thenReturn(getUserResponse)
            val getProfileUseCase = GetProfileUseCase(profileRepository)

            getProfileUseCase(id).test {
                val firstEmission = awaitItem()
                assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)
                val secondEmission = awaitItem()
                assertThat(secondEmission).isInstanceOf(Resource.Error::class.java)
                assertThat(secondEmission.data).isNull()
                assertThat(secondEmission.message).isEqualTo("error message")

                cancelAndIgnoreRemainingEvents()
            }
        }
}