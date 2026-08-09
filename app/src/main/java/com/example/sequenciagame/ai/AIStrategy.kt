package com.example.sequenciagame.ai

import com.example.sequenciagame.model.Play

class AIStrategy(
    private val difficulty: Difficulty
) {

    fun getBestMove(
        validPlays: List<Play>
    ): Play? {
        if (validPlays.isEmpty()) return null

        return when (difficulty) {
            Difficulty.EASY -> validPlays.random()
            Difficulty.MEDIUM -> chooseMedium(validPlays)
            Difficulty.HARD -> chooseHard(validPlays)
        }
    }

    private fun chooseMedium(
        validPlays: List<Play>
    ): Play {
        /* prioriza jogadas com duas cartas */
        val pairs = validPlays.filter {
            it.cards.size == 2
        }

        return if (pairs.isNotEmpty())  pairs.random()
        else validPlays.random()
    }

    private fun chooseHard(
        validPlays: List<Play>
    ): Play {

        /*
         * Primeiro tenta jogadas com duas cartas.
         */

        val pairs = validPlays.filter {
            it.cards.size == 2
        }

        if (pairs.isNotEmpty()) {
            return pairs.maxByOrNull {
                it.getValue() ?: 0
            } ?: pairs.first()
        }

        /*
         * Caso não exista par,
         * escolhe a jogada de maior valor.
         */

        return validPlays.maxByOrNull {
            it.getValue() ?: 0
        } ?: validPlays.first()
    }
}