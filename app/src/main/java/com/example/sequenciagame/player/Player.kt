package com.example.sequenciagame.player

import com.example.sequenciagame.model.Hand
import com.example.sequenciagame.model.Play

abstract class Player (
    val name : String,
    val isHuman : Boolean,
    val imageRes : String = ""
) {
    var hand = Hand()
    var score : Int = 0

    abstract fun choosePlay(validPlays: List<Play>): Play?
    abstract fun shouldDraw(validPlays: List<Play>): Boolean
}