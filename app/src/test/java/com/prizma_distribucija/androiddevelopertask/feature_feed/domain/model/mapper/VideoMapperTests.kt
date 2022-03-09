package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AuthorDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.VideoDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Author
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Video
import org.junit.Before
import org.junit.Test

class VideoMapperTests {
    private lateinit var videoMapper: VideoMapper

    @Before
    fun setUp() {
        videoMapper = VideoMapper()
    }

    @Test
    fun mapFromDto_shouldCorrectlyMap() {
        val dto = VideoDto("handler", "url", 1)

        val expectedDomainModel = Video("handler", "url", 1)

        val actualDomainModel = videoMapper.mapFromDto(dto)

        assertThat(actualDomainModel).isEqualTo(expectedDomainModel)
    }

    @Test
    fun mapToDto_shouldCorrectlyMap() {
        val domainModel = Video("handler", "url", 1)

        val expectedDto = VideoDto("handler", "url", 1)

        val actualDto = videoMapper.mapToDto(domainModel)

        assertThat(actualDto).isEqualTo(expectedDto)
    }
}