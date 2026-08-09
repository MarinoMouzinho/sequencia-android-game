package com.example.sequenciagame.model

class Deck(
    private val specialCards : Boolean = true
) {
    private val _cards = mutableListOf<Card>()

    val cards: List<Card>
        get() = _cards.toList()

    init {
        reset()
    }

    fun reset() {
        _cards.clear()
        var id = 0

        if(specialCards){
            repeat(2){
                _cards.add(Card(id++, CardType.SKIP))
                _cards.add(Card(id++, CardType.REVERSE))
                _cards.add(Card(id++, CardType.PLUS_ONE))
            }
        }

        repeat(3){
            _cards.add(Card(id++, CardType.NUMBER,9))
            _cards.add(Card(id++, CardType.NUMBER,10))
            _cards.add(Card(id++, CardType.NUMBER,11))
            _cards.add(Card(id++, CardType.NUMBER,12))
            if(specialCards) _cards.add(Card(id++, CardType.WILD))
        }
        repeat(4){
            _cards.add(Card(id++, CardType.NUMBER,8))
        }
        repeat(5){
            _cards.add(Card(id++, CardType.NUMBER,1))
            _cards.add(Card(id++, CardType.NUMBER,2))
            _cards.add(Card(id++, CardType.NUMBER,7))
        }
        repeat(6){
            _cards.add(Card(id++, CardType.NUMBER,3))
            _cards.add(Card(id++, CardType.NUMBER,4))
            _cards.add(Card(id++, CardType.NUMBER,5))
            _cards.add(Card(id++, CardType.NUMBER,6))
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

    fun size(): Int = _cards.size

    fun isEmpty(): Boolean = _cards.isEmpty()
}