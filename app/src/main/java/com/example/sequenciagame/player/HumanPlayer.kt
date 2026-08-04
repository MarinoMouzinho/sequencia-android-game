package com.example.sequenciagame.player

import com.example.sequenciagame.model.Card

class HumanPlayer(
    name: String
) : Player(name, true) {

    override fun chooseCard(
        validCards: List<Card>,
        topCard: Card?
    ): Card? = null

    override fun shouldDraw(validCards: List<Card>): Boolean = validCards.isEmpty()
}