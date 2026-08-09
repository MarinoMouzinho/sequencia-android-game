package com.example.sequenciagame.model

data class PlayerProfile(
    val id: Long,
    val name: String = "Me",
    val avatarUrl: String = "",
    val playedGames: Int = 0,
    val victories: Int = 0,
    val volume: Int = 50
)