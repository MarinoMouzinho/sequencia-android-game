package com.example.sequenciagame.ai

import com.example.sequenciagame.model.Card

class AIStrategy(
    val difficulty : Difficulty
) {
    enum class Difficulty { EASY, MEDIUM, HARD }

    fun getBestMove(
        validCards : List<Card>,
        topCard: Card?,
        allHandCards : List<Card>
    ) : Card? {
        //TODO: Revisar futuramente melhores estrategias
        if(validCards.isEmpty()) return  null
        return when(difficulty) {
            Difficulty.EASY -> validCards.random()
            Difficulty.MEDIUM -> {
                // Prioriza dobrar pontuação
                val dobros = validCards.filter{ topCard != null && it.isDouble(topCard)}
                if(dobros.isNotEmpty()) dobros.random()
                else validCards.random()
            }

            Difficulty.HARD -> {
                //Prioriza dobrar pontuacao se pilha tiver com mais de 3 cartas,
                //caso contrario, joga carta de maior valor
                //ultimo caso, joga uma com maior num de cartas do mesmo naipe

                val dobros = validCards.filter{ topCard != null && it.isDouble(topCard)}

                if (dobros.isNotEmpty() && topCard != null) {
                    return dobros.maxByOrNull { it.number } ?: dobros.first()
                }

                validCards.maxByOrNull { it.number } ?: validCards.first()
            }
        }
    }
}