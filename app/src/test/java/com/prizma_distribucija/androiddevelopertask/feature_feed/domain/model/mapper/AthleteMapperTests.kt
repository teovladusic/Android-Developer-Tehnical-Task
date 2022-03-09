package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AthleteDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Athlete
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper.AthleteMapper
import org.junit.Before
import org.junit.Test

class AthleteMapperTests {

    lateinit var athleteMapper: AthleteMapper

    @Before
    fun setUp() {
        athleteMapper = AthleteMapper()
    }

    @Test
    fun mapFromDto_shouldCorrectlyMap() {
        val dto = AthleteDto(1, "name", "avatar")

        val expectedDomainModel = Athlete(1, "name", "avatar")

        val actualDomainModel = athleteMapper.mapFromDto(dto)

        assertThat(actualDomainModel).isEqualTo(expectedDomainModel)
    }

    @Test
    fun mapToDto_shouldCorrectlyMap() {
        val domainModel = Athlete(1, "name", "avatar")

        val expectedDto = AthleteDto(1, "name", "avatar")

        val actualDto = athleteMapper.mapToDto(domainModel)

        assertThat(actualDto).isEqualTo(expectedDto)
    }
}