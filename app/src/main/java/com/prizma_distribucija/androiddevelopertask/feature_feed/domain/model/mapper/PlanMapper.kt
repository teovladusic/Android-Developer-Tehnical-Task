package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper

import com.prizma_distribucija.androiddevelopertask.core.util.EntityMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PlanDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Plan

class PlanMapper : EntityMapper<PlanDto, Plan> {
    override fun mapFromDto(dto: PlanDto): Plan {
        return Plan(
            type = dto.type
        )
    }

    override fun mapToDto(domainModel: Plan): PlanDto {
        return PlanDto(
            type = domainModel.type
        )
    }
}