package com.prizma_distribucija.androiddevelopertask.feature_feed.domain

import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AthleteDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PlanDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.UserResponseDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.*
import org.junit.Before
import org.junit.Test

class UserMapperTests {

    lateinit var userMapper: UserMapper

    @Before
    fun setUp() {
        val planMapper = PlanMapper()
        val athleteMapper = AthleteMapper()

        userMapper = UserMapper(athleteMapper, planMapper)
    }

    @Test
    fun mapFromDto_shouldCorrectlyMap() {
        val plan = Plan("type")
        val planDto = PlanDto("type")

        val athletesDto = listOf(AthleteDto(1, "name", "avatar"))
        val athletes = listOf(Athlete(1, "name", "avatar"))

        val dto = UserResponseDto(
            id = 1,
            name = "name",
            avatar = "avatar",
            views = "views",
            videoPlays = "videoPlays",
            plan = planDto,
            athletes = athletesDto
        )

        val expectedDomainModel = User(
            id = 1,
            name = "name",
            avatar = "avatar",
            views = "views",
            videoPlays = "videoPlays",
            plan = plan,
            athletes = athletes
        )

        val actualDomainModel = userMapper.mapFromDto(dto)

        assertThat(actualDomainModel).isEqualTo(expectedDomainModel)
    }

    @Test
    fun mapToDto_shouldCorrectlyMap() {
        val plan = Plan("type")
        val planDto = PlanDto("type")

        val athletesDto = listOf(AthleteDto(1, "name", "avatar"))
        val athletes = listOf(Athlete(1, "name", "avatar"))

        val domainModel = User(
            id = 1,
            name = "name",
            avatar = "avatar",
            views = "views",
            videoPlays = "videoPlays",
            plan = plan,
            athletes = athletes
        )

        val expectedDto = UserResponseDto(
            id = 1,
            name = "name",
            avatar = "avatar",
            views = "views",
            videoPlays = "videoPlays",
            plan = planDto,
            athletes = athletesDto
        )

        val actualDto = userMapper.mapToDto(domainModel)

        assertThat(actualDto).isEqualTo(expectedDto)
    }
}