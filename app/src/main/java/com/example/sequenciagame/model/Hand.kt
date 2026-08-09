package com.example.sequenciagame.model

class Hand {

    private val _cards = mutableListOf<Card>()
    val cards : List<Card>
        get() = _cards.toList()
    val size : Int
        get() = _cards.size

    fun addCard(card : Card) = _cards.add(card)

    fun addCard(cards : Collection<Card>) = _cards.addAll(cards)

    fun removeCard(card: Card) : Boolean = _cards.remove(card)

    fun removePlay(play: Play): Boolean{
        if(!containsPlay(play)) return false

        play.cards.forEach { card ->
            _cards.remove(card)
        }

        return true
    }

    fun hasCard(card : Card) : Boolean = _cards.contains(card)

    fun containsPlay(play: Play): Boolean {
        val availableCards = _cards.toMutableList()

        for(card in play.cards){
            if(!availableCards.remove(card)) return false
        }

        return true
    }

    fun getPossiblePlays(): List<Play> {
        val plays = mutableListOf<Play>()

        for(card in _cards){
            when (card.cardType) {
                CardType.WILD -> {
                    for (value in 1..12) {
                        plays.add(Play(cards = listOf(card), wildValue = value))
                    }
                }
                else -> plays.add(Play(cards = listOf(card)))
            }
        }

        for (i in _cards.indices) {
            for (j in i + 1 until _cards.size) {
                val first = _cards[i]
                val second = _cards[j]

                if (
                    first.cardType == CardType.NUMBER &&
                    second.cardType == CardType.NUMBER &&
                    first.number == second.number
                ) {
                    plays.add(Play(cards = listOf(first, second)))
                }

                if (
                    first.cardType == CardType.WILD &&
                    second.cardType == CardType.NUMBER
                ) {
                    val number = second.number
                    if (number != null) plays.add( Play(cards = listOf(first, second),wildValue = number))
                }

                if (
                    first.cardType == CardType.NUMBER &&
                    second.cardType == CardType.WILD
                ) {
                    val number = first.number
                    if (number != null) plays.add(Play(cards = listOf(first, second), wildValue = number))
                }
            }
        }

        return plays.filter { it.isValid() }
    }

    fun getNumericPlays(): List<Play> = getPossiblePlays().filter { it.getValue() != null }
    fun clear() = _cards.clear()
}