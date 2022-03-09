package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.prizma_distribucija.androiddevelopertask.core.util.EntityMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.VideoDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Video

class VideoMapper : EntityMapper<VideoDto, Video> {
    override fun mapFromDto(dto: VideoDto): Video {
        return Video(
            handler = dto.handler,
            url = dto.url,
            length = dto.length
        )
    }

    override fun mapToDto(domainModel: Video): VideoDto {
        return VideoDto(
            handler = domainModel.handler,
            url = domainModel.url,
            length = domainModel.length
        )
    }
}