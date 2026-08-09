package com.example.sequenciagame

import com.example.sequenciagame.model.Card
import com.example.sequenciagame.model.CardType
import com.example.sequenciagame.model.Hand
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HandTest {

    private fun numberCard(
        id: Int,
        number: Int
    ): Card {
        return Card(
            id = id,
            cardType = CardType.NUMBER,
            number = number
        )
    }

    private fun wildCard(id: Int): Card {
        return Card(
            id = id,
            cardType = CardType.WILD
        )
    }

    @Test
    fun handFindsSingleNumberPlays() {

        val hand = Hand()

        hand.addCard(numberCard(1, 7))
        hand.addCard(numberCard(2, 9))

        val plays = hand.getPossiblePlays()

        assertTrue(
            plays.any {
                it.cards.size == 1 &&
                        it.cards[0].number == 7
            }
        )

        assertTrue(
            plays.any {
                it.cards.size == 1 &&
                        it.cards[0].number == 9
            }
        )
    }

    @Test
    fun handFindsPairOfEqualNumbers() {

        val hand = Hand()

        val card1 = numberCard(1, 7)
        val card2 = numberCard(2, 7)

        hand.addCard(card1)
        hand.addCard(card2)

        val plays = hand.getPossiblePlays()

        val pair = plays.firstOrNull {
            it.cards.size == 2 &&
                    it.cards.all { card ->
                        card.cardType == CardType.NUMBER &&
                                card.number == 7
                    }
        }

        assertTrue(pair != null)
        assertEquals(14, pair?.getValue())
    }

    @Test
    fun handDoesNotCreatePairWithDifferentNumbers() {

        val hand = Hand()

        hand.addCard(numberCard(1, 7))
        hand.addCard(numberCard(2, 9))

        val plays = hand.getPossiblePlays()

        val invalidPair = plays.any {
            it.cards.size == 2 &&
                    it.cards[0].cardType == CardType.NUMBER &&
                    it.cards[1].cardType == CardType.NUMBER
        }

        assertFalse(invalidPair)
    }

    @Test
    fun handFindsAllWildValues() {

        val hand = Hand()

        hand.addCard(wildCard(1))

        val plays = hand.getPossiblePlays()

        val wildPlays = plays.filter {
            it.cards.size == 1 &&
                    it.cards[0].cardType == CardType.WILD
        }

        assertEquals(12, wildPlays.size)

        for (value in 1..12) {
            assertTrue(
                wildPlays.any {
                    it.wildValue == value
                }
            )
        }
    }

    @Test
    fun handFindsWildPlusNumber() {

        val hand = Hand()

        val wild = wildCard(1)
        val seven = numberCard(2, 7)

        hand.addCard(wild)
        hand.addCard(seven)

        val plays = hand.getPossiblePlays()

        val doublePlay = plays.firstOrNull {
            it.cards.size == 2 &&
                    it.cards.any { card -> card.cardType == CardType.WILD } &&
                    it.cards.any {
                            card ->
                        card.cardType == CardType.NUMBER &&
                                card.number == 7
                    }
        }

        assertTrue(doublePlay != null)
        assertEquals(14, doublePlay?.getValue())
    }

    @Test
    fun handDoesNotAllowThreeCards() {

        val hand = Hand()

        hand.addCard(numberCard(1, 7))
        hand.addCard(numberCard(2, 7))
        hand.addCard(numberCard(3, 7))

        val plays = hand.getPossiblePlays()

        assertTrue(
            plays.none { it.cards.size > 2 }
        )
    }

    @Test
    fun removePlayRemovesBothCards() {

        val hand = Hand()

        val card1 = numberCard(1, 7)
        val card2 = numberCard(2, 7)

        hand.addCard(card1)
        hand.addCard(card2)

        val play = hand.getPossiblePlays().first {
            it.cards.size == 2
        }

        assertTrue(hand.removePlay(play))
        assertEquals(0, hand.size)
    }
}