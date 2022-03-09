package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PostDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper.PostMapper
import org.junit.Before
import org.junit.Test

class PostMapperTests {
    lateinit var postMapper: PostMapper

    @Before
    fun setUp() {
        postMapper = PostMapper()
    }

    @Test
    fun mapFromDto_shouldCorrectlyMap() {
        val dto = PostDto(1)

        val expectedDomainModel = Post(1)

        val actualDomainModel = postMapper.mapFromDto(dto)

        assertThat(actualDomainModel).isEqualTo(expectedDomainModel)
    }

    @Test
    fun mapToDto_shouldCorrectlyMap() {
        val domainModel = Post(1)

        val expectedDto = PostDto(1)

        val actualDto = postMapper.mapToDto(domainModel)

        assertThat(actualDto).isEqualTo(expectedDto)
    }
}