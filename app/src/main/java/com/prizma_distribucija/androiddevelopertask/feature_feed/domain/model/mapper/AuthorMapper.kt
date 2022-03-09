package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.prizma_distribucija.androiddevelopertask.core.util.EntityMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AuthorDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Author

class AuthorMapper : EntityMapper<AuthorDto, Author> {

    override fun mapFromDto(dto: AuthorDto): Author {
        return Author(
            id = dto.id,
            name = dto.name
        )
    }

    override fun mapToDto(domainModel: Author): AuthorDto {
        return AuthorDto(
            id = domainModel.id,
            name = domainModel.name
        )
    }
}