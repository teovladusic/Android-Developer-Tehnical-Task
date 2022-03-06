package com.prizma_distribucija.androiddevelopertask.feature_feed.domain

import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PlanDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Plan
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.PlanMapper
import org.junit.Before
import org.junit.Test

class PlanMapperTests {

    lateinit var planMapper: PlanMapper

    @Before
    fun setUp() {
        planMapper = PlanMapper()
    }

    @Test
    fun mapFromDto_shouldCorrectlyMap() {
        val dto = PlanDto("type")

        val expectedDomainModel = Plan("type")

        val actualDomainModel = planMapper.mapFromDto(dto)

        assertThat(actualDomainModel).isEqualTo(expectedDomainModel)
    }

    @Test
    fun mapToDto_shouldCorrectlyMap() {
        val domainModel = Plan("type")

        val expectedDto = PlanDto("type")

        val actualDto = planMapper.mapToDto(domainModel)

        assertThat(actualDto).isEqualTo(expectedDto)
    }
}