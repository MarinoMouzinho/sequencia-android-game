package com.example.sequenciagame

import com.example.sequenciagame.model.Card
import com.example.sequenciagame.model.CardType
import com.example.sequenciagame.model.Play
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayTest {

    private val card7 = Card(
        id = 1,
        cardType = CardType.NUMBER,
        number = 7
    )

    private val card9 = Card(
        id = 2,
        cardType = CardType.NUMBER,
        number = 9
    )

    private val wild = Card(
        id = 3,
        cardType = CardType.WILD
    )

    @Test
    fun singleNumberCard_isValid() {

        val play = Play(
            cards = listOf(card7)
        )

        assertTrue(play.isValid())
        assertEquals(7, play.getValue())
    }

    @Test
    fun pairOfSameNumbers_isValidAndDoubles() {

        val play = Play(
            cards = listOf(card7, card9.copy(id = 4, number = 7))
        )

        assertTrue(play.isValid())
        assertTrue(play.isValidPair())
        assertEquals(14, play.getValue())
    }

    @Test
    fun differentNumbers_areInvalid() {

        val play = Play(
            cards = listOf(card7, card9)
        )

        assertFalse(play.isValid())
        assertEquals(null, play.getValue())
    }

    @Test
    fun moreThanTwoCards_areInvalid() {

        val play = Play(
            cards = listOf(
                card7,
                card7.copy(id = 4),
                card7.copy(id = 5)
            )
        )

        assertFalse(play.isValid())
    }

    @Test
    fun wildCanRepresentNumber() {

        val play = Play(
            cards = listOf(wild),
            wildValue = 7
        )

        assertTrue(play.isValid())
        assertEquals(7, play.getValue())
    }

    @Test
    fun wildAndNumberCanDouble() {

        val play = Play(
            cards = listOf(wild, card7),
            wildValue = 7
        )

        assertTrue(play.isValid())
        assertEquals(14, play.getValue())
    }

    @Test
    fun wildWithDifferentNumberIsInvalid() {

        val play = Play(
            cards = listOf(wild, card7),
            wildValue = 5
        )

        assertFalse(play.isValid())
        assertEquals(null, play.getValue())
    }
}