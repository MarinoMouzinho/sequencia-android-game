package com.example.sequenciagame.player

import com.example.sequenciagame.model.Card
import com.example.sequenciagame.model.Hand

abstract class Player (
    val name : String,
    val isHuman : Boolean
) {
    var hand = Hand()
    var score : Int = 0

    abstract fun chooseCard(validCards: List<Card>, topCard: Card?): Card?
    abstract fun shouldDraw(validCards: List<Card>): Boolean
}