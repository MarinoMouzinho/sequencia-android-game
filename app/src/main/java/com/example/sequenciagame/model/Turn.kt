package com.example.sequenciagame.model

import com.example.sequenciagame.player.Player

data class Turn(
    var player: Player,
    var cardPlayed: Card? = null,
    var matched : Boolean = false,
    var timestamp: Long = System.currentTimeMillis()
)