package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.prizma_distribucija.androiddevelopertask.core.util.EntityMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.FeedDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import javax.inject.Inject

class FeedMapper @Inject constructor(
    private val authorMapper: AuthorMapper,
    private val videoMapper: VideoMapper
) : EntityMapper<FeedDto, Feed> {

    override fun mapFromDto(dto: FeedDto): Feed {
        val author = authorMapper.mapFromDto(dto.author)
        val video = videoMapper.mapFromDto(dto.video)

        return Feed(
            id = dto.id,
            author = author,
            video = video,
            description = dto.description
        )
    }

    override fun mapToDto(domainModel: Feed): FeedDto {
        val authorDto = authorMapper.mapToDto(domainModel.author)
        val videoDto = videoMapper.mapToDto(domainModel.video)

        return FeedDto(
            id = domainModel.id,
            author = authorDto,
            video = videoDto,
            description = domainModel.description
        )
    }
}