package com.example.sequenciagame.model

data class Play(
    val cards: List<Card>,
    val wildValue: Int? = null
){
    fun isSingleCard(): Boolean = cards.size == 1

    fun isDoubleCard(): Boolean = cards.size == 2

    fun containsWild(): Boolean = cards.any{it.cardType == CardType.WILD }

    fun onlyNumbers(): Boolean = cards.all{it.cardType == CardType.NUMBER }

    fun isValid(): Boolean {
        if (cards.isEmpty()) return false
        if (cards.size > 2) return false

        if (cards.size == 1) {
            val card = cards[0]
            return when (card.cardType) {
                CardType.NUMBER -> card.number in 1..12
                CardType.SKIP -> true
                CardType.REVERSE -> true
                CardType.PLUS_ONE -> true
                CardType.WILD -> wildValue in 1..12
            }
        }

        if (cards.size == 2) {
            if (isValidPair()) return true

            val hasWild = cards.any { it.cardType == CardType.WILD }
            val hasNumber = cards.any { it.cardType == CardType.NUMBER }
            if (hasWild && hasNumber) {
                val numberCard = cards.first { it.cardType == CardType.NUMBER }
                return wildValue == numberCard.number
            }
        }

        return false
    }

    fun isValidPair(): Boolean {
        if(cards.size != 2) return false

        val first = cards[0]
        val second = cards[1]

        if(first.cardType != CardType.NUMBER || second.cardType != CardType.NUMBER) return false

        return first.number == second.number
    }

    fun getValue(): Int? {
        if (cards.isEmpty()) return null

        if (cards.size == 1) {
            val card = cards[0]
            return when (card.cardType) {
                CardType.NUMBER -> card.number
                CardType.WILD -> getWildCardValue()
                else -> null
            }
        }

        if (cards.size == 2) {
            val first = cards[0]
            val second = cards[1]
            if (isValidPair()) {
                val number = first.number ?: return null
                return number * 2
            }

            if (first.cardType == CardType.WILD &&
                second.cardType == CardType.NUMBER
            ) {
                val number = second.number ?: return null
                if (wildValue != number) return null
                return number * 2
            }

            if (first.cardType == CardType.NUMBER &&
                second.cardType == CardType.WILD
            ) {
                val number = first.number ?: return null
                if (wildValue != number) return null
                return number * 2
            }
        }

        return null
    }

    fun getWildCardValue() : Int? {
        if(wildValue == null) return null

        return if(wildValue in 1..12) wildValue
        else null
    }
}