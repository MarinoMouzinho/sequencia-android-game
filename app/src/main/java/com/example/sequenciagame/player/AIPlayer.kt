package com.example.sequenciagame.player

import com.example.sequenciagame.ai.AIStrategy
import com.example.sequenciagame.ai.Difficulty
import com.example.sequenciagame.model.Play

class AIPlayer (
    name : String,
    private val difficulty: Difficulty = Difficulty.MEDIUM
) : Player(name, false) {

    private val strategy = AIStrategy(difficulty)

    override fun choosePlay(
        validPlays: List<Play>
    ): Play? = strategy.getBestMove(validPlays)


    override fun shouldDraw(
        validPlays: List<Play>
    ): Boolean = validPlays.isEmpty()

}