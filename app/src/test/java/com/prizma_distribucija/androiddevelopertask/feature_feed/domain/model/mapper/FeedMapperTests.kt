package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.google.common.truth.Truth
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AuthorDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.FeedDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.VideoDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Author
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Video
import org.junit.Before
import org.junit.Test

class FeedMapperTests {

    private lateinit var feedMapper: FeedMapper

    @Before
    fun setUp() {
        val videoMapper = VideoMapper()
        val authorMapper = AuthorMapper()
        feedMapper = FeedMapper(authorMapper, videoMapper)
    }

    @Test
    fun mapFromDto_shouldCorrectlyMap() {
        val authorDto = AuthorDto(1, "name")
        val videoDto = VideoDto("handler", "url", 1)
        val dto = FeedDto(1, authorDto, videoDto, "description")

        val author = Author(1, "name")
        val video = Video("handler", "url", 1)
        val expectedDomainModel = Feed(1, author, video, "description")

        val actualDomainModel = feedMapper.mapFromDto(dto)

        Truth.assertThat(actualDomainModel).isEqualTo(expectedDomainModel)
    }

    @Test
    fun mapToDto_shouldCorrectlyMap() {
        val author = Author(1, "name")
        val video = Video("handler", "url", 1)
        val domainModel = Feed(1, author, video, "description")

        val authorDto = AuthorDto(1, "name")
        val videoDto = VideoDto("handler", "url", 1)
        val expectedDto = FeedDto(1, authorDto, videoDto, "description")

        val actualDto = feedMapper.mapToDto(domainModel)

        Truth.assertThat(actualDto).isEqualTo(expectedDto)
    }
}