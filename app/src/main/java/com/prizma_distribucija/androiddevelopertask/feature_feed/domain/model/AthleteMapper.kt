package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model

import com.prizma_distribucija.androiddevelopertask.core.util.EntityMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AthleteDto

class AthleteMapper : EntityMapper<AthleteDto, Athlete> {
    override fun mapFromDto(dto: AthleteDto): Athlete {
        return Athlete(
            id = dto.id,
            name = dto.name,
            avatar = dto.avatar
        )
    }

    override fun mapToDto(domainModel: Athlete): AthleteDto {
        return AthleteDto(
            id = domainModel.id,
            name = domainModel.name,
            avatar = domainModel.avatar
        )
    }
}