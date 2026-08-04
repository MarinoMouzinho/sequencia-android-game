package com.example.sequenciagame.model

class Hand {

    private val _cards = mutableListOf<Card>()
    val cards : List<Card>
        get() = _cards.toList()
    val size : Int
        get() = _cards.size

    fun addCard(card : Card) = _cards.add(card)

    fun removeCard(card: Card) : Boolean = _cards.remove(card)

    fun hasCard(card : Card) : Boolean {
        return _cards.contains(card)
    }

    fun getPlayableCards(topCard : Card?) : List<Card> {
        return if (topCard == null) _cards.toList()
        else _cards.filter{ it.matches(topCard) }
    }

    fun clear() = _cards.clear()
}