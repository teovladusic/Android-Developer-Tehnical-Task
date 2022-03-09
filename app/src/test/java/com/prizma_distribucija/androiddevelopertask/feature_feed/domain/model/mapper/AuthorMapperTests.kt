package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AthleteDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AuthorDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Athlete
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Author
import org.junit.Before
import org.junit.Test

class AuthorMapperTests {
    lateinit var authorMapper: AuthorMapper

    @Before
    fun setUp() {
        authorMapper = AuthorMapper()
    }

    @Test
    fun mapFromDto_shouldCorrectlyMap() {
        val dto = AuthorDto(1, "name")

        val expectedDomainModel = Author(1, "name")

        val actualDomainModel = authorMapper.mapFromDto(dto)

        assertThat(actualDomainModel).isEqualTo(expectedDomainModel)
    }

    @Test
    fun mapToDto_shouldCorrectlyMap() {
        val domainModel = Author(1, "name")

        val expectedDto = AuthorDto(1, "name")

        val actualDto = authorMapper.mapToDto(domainModel)

        assertThat(actualDto).isEqualTo(expectedDto)
    }
}