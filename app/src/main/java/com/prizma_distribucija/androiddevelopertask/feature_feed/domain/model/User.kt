package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model

data class User(
    val id: Int,
    val name: String,
    val avatar: String,
    val views: String,
    val videoPlays: String,
    val athletes: List<Athlete>,
    val plan: Plan
)