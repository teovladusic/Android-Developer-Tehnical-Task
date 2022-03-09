package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.prizma_distribucija.androiddevelopertask.core.util.EntityMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PostDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post

class PostMapper : EntityMapper<PostDto, Post> {
    override fun mapFromDto(dto: PostDto): Post {
        return Post(dto.id)
    }

    override fun mapToDto(domainModel: Post): PostDto {
        return PostDto(domainModel.id)
    }
}