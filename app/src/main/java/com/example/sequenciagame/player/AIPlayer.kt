package com.example.sequenciagame.player

import com.example.sequenciagame.ai.AIStrategy
import com.example.sequenciagame.model.Card

class AIPlayer (
    name : String,
    private val difficulty: AIStrategy.Difficulty = AIStrategy.Difficulty.MEDIUM
) : Player(name, false) {

    private val strategy = AIStrategy(difficulty)

    override fun chooseCard(
        validCards: List<Card>,
        topCard: Card?
    ): Card? = strategy.getBestMove(validCards, topCard, hand.cards)

    override fun shouldDraw(validCards: List<Card>): Boolean = validCards.isEmpty()

}