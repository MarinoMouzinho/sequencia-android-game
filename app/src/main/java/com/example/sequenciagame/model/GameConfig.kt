package com.example.sequenciagame.model

import com.example.sequenciagame.ai.Difficulty

data class GameConfig (
    val opponents: Int = 2,
    val rounds: Int = 3,
    val initialHandSize: Int = 4,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val specialCards: Boolean = true
)