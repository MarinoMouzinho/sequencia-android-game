package com.example.sequenciagame.model

class Deck {
    private val _cards = mutableListOf<Card>()
    val cards: List<Card>
        get() = _cards.toList()

    init {
        reset()
    }

    fun reset() {
        _cards.clear()
        var id = 0
        for (suit in Suit.entries) {
            /* TODO: Atualizar para cartas reais
                5× 2, 6× 3, 6× 4, 6× 5, 6× 6, 5× 7,
                4× 8, 3× 9, 3× 10, 3× 11, 3× 12,
                3× Wild, 2× Skip, 2× Reverse */
            for (number in 1..13) {
                _cards.add(Card(id++, number, suit))
            }
        }
        shuffle()
    }

    fun shuffle() {
        _cards.shuffle()
    }

    fun drawCard(): Card? {
        return if (_cards.isNotEmpty()) _cards.removeAt(0)
        else null
    }

    fun isEmpty(): Boolean = _cards.isEmpty()
}