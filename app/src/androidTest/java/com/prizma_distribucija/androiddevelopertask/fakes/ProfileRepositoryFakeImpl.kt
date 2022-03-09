package com.prizma_distribucija.androiddevelopertask.fakes

import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AthleteDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PlanDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Athlete
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.GetUserResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Plan
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.User
import kotlinx.coroutines.delay

class ProfileRepositoryFakeImpl : ProfileRepository {

    companion object {
        var isSuccessful = false
        var delayTime = 0L
    }

    override suspend fun getUser(id: Int): GetUserResponse {
        delay(delayTime)
        val plan = Plan("type")
        val athletes = listOf(Athlete(1, "name", "avatar"))
        val user = User(
            id = 1,
            name = "name",
            avatar = "avatar",
            views = "views",
            videoPlays = "videoPlays",
            plan = plan,
            athletes = athletes
        )
        return if (isSuccessful) {
            GetUserResponse(
                isSuccessful = true,
                data = user,
                errorMessage = null
            )
        } else {
            GetUserResponse(
                isSuccessful = false,
                data = null,
                errorMessage = "error message"
            )

        }
    }
}