package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model

import com.prizma_distribucija.androiddevelopertask.core.util.EntityMapper
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PlanDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.UserResponseDto
import javax.inject.Inject

class UserMapper @Inject constructor(
    private val athleteMapper: AthleteMapper,
    private val planMapper: PlanMapper
) : EntityMapper<UserResponseDto, User> {

    override fun mapFromDto(dto: UserResponseDto): User {
        val athletes = dto.athletes?.map { athleteMapper.mapFromDto(it) }
        val plan = planMapper.mapFromDto(dto.plan ?: PlanDto("type"))

        return User(
            id = dto.id ?: 0,
            name = dto.name ?: "no_name",
            avatar = dto.avatar ?: "no_avatar",
            views = dto.views ?: "0",
            videoPlays = dto.videoPlays ?: "0",
            athletes = athletes ?: emptyList(),
            plan = plan
        )
    }

    override fun mapToDto(domainModel: User): UserResponseDto {
        val athletes = domainModel.athletes.map { athleteMapper.mapToDto(it) }
        val plan = planMapper.mapToDto(domainModel.plan)
        return UserResponseDto(
            id = domainModel.id,
            name = domainModel.name,
            avatar = domainModel.avatar,
            views = domainModel.views,
            videoPlays = domainModel.videoPlays,
            athletes = athletes,
            plan = plan,
        )
    }
}