package com.example.sequenciagame

import com.example.sequenciagame.model.Card
import com.example.sequenciagame.model.CardType
import com.example.sequenciagame.model.GameStatus
import com.example.sequenciagame.model.Play
import com.example.sequenciagame.model.SequenciaGame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SequenciaGameTest {

    /**
     * Cria uma carta numérica para os testes.
     */
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

    /**
     * Cria uma coringa.
     */
    private fun wildCard(id: Int): Card {
        return Card(
            id = id,
            cardType = CardType.WILD
        )
    }


    // =========================================================
    // 1. JOGADA NORMAL
    // =========================================================

    @Test
    fun higherNumberCanBePlayed() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val card = numberCard(100, 7)

        player.hand.addCard(card)

        val play = Play(
            cards = listOf(card)
        )

        assertTrue(
            game.canPlay(play)
        )
    }


    // =========================================================
    // 2. NÚMERO MENOR NÃO PODE SER JOGADO
    // =========================================================

    @Test
    fun lowerNumberCannotBePlayed() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val highCard = numberCard(100, 7)
        val lowCard = numberCard(101, 3)

        player.hand.addCard(highCard)

        val highPlay = Play(
            cards = listOf(highCard)
        )

        assertTrue(game.play(highPlay))

        player.hand.addCard(lowCard)

        val lowPlay = Play(
            cards = listOf(lowCard)
        )

        assertFalse(
            game.canPlay(lowPlay)
        )
    }


    // =========================================================
    // 3. CARTA IGUAL AO TOPO DOBRA
    // =========================================================

    @Test
    fun sameNumberDoublesTableValue() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val first = numberCard(100, 5)

        player.hand.addCard(first)

        game.play(
            Play(
                cards = listOf(first)
            )
        )

        assertEquals(
            5,
            game.tableValue
        )

        /*
         * Como existe apenas um jogador, depois da jogada
         * o turno volta para ele.
         */

        val second = numberCard(101, 5)

        player.hand.addCard(second)

        game.play(
            Play(
                cards = listOf(second)
            )
        )

        assertEquals(
            10,
            game.tableValue
        )
    }


    // =========================================================
    // 4. PAR DE CARTAS IGUAIS
    // =========================================================

    @Test
    fun pairOfEqualCardsCreatesDoubleValue() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val card1 = numberCard(100, 5)
        val card2 = numberCard(101, 5)

        player.hand.addCard(card1)
        player.hand.addCard(card2)

        val play = Play(
            cards = listOf(card1, card2)
        )

        assertEquals(
            10,
            play.getValue()
        )
    }


    // =========================================================
    // 5. PAR PODE DOBRAR O TOPO
    // =========================================================

    @Test
    fun pairCanDoubleTableValue() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        /*
         * Primeiro colocamos 10 na mesa.
         */

        val first = numberCard(100, 10)

        player.hand.addCard(first)

        game.play(
            Play(
                cards = listOf(first)
            )
        )

        assertEquals(
            10,
            game.tableValue
        )

        /*
         * Agora 5 + 5 = 10.
         *
         * Como o valor da Play é igual ao topo,
         * o topo deve dobrar para 20.
         */

        val card1 = numberCard(101, 5)
        val card2 = numberCard(102, 5)

        player.hand.addCard(card1)
        player.hand.addCard(card2)

        val pair = Play(
            cards = listOf(card1, card2)
        )

        assertTrue(
            game.canPlay(pair)
        )

        game.play(pair)

        assertEquals(
            20,
            game.tableValue
        )
    }


    // =========================================================
    // 6. CORINGA PODE REPRESENTAR UM NÚMERO
    // =========================================================

    @Test
    fun wildCanRepresentNumber() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val wild = wildCard(100)

        player.hand.addCard(wild)

        val play = Play(
            cards = listOf(wild),
            wildValue = 7
        )

        assertEquals(
            7,
            play.getValue()
        )

        assertTrue(
            game.canPlay(play)
        )

        game.play(play)

        assertEquals(
            7,
            game.tableValue
        )
    }


    // =========================================================
    // 7. CORINGA PODE DOBRAR
    // =========================================================

    @Test
    fun wildCanDoubleTableValue() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        /*
         * Coloca 7 na mesa.
         */

        val first = numberCard(100, 7)

        player.hand.addCard(first)

        game.play(
            Play(
                cards = listOf(first)
            )
        )

        /*
         * Agora coringa representa 7.
         */

        val wild = wildCard(101)

        player.hand.addCard(wild)

        val play = Play(
            cards = listOf(wild),
            wildValue = 7
        )

        assertTrue(
            game.canPlay(play)
        )

        game.play(play)

        assertEquals(
            14,
            game.tableValue
        )
    }


    // =========================================================
    // 8. CORINGA + CARTA
    // =========================================================

    @Test
    fun wildPlusNumberCreatesDoubleValue() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val wild = wildCard(100)
        val seven = numberCard(101, 7)

        player.hand.addCard(wild)
        player.hand.addCard(seven)

        val play = Play(
            cards = listOf(wild, seven),
            wildValue = 7
        )

        assertEquals(
            14,
            play.getValue()
        )
    }


    // =========================================================
    // 9. +1
    // =========================================================

    @Test
    fun plusOneIncreasesTableValue() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val first = numberCard(100, 5)

        player.hand.addCard(first)

        game.play(
            Play(
                cards = listOf(first)
            )
        )

        assertEquals(
            5,
            game.tableValue
        )

        /*
         * Precisamos localizar uma carta PLUS_ONE
         * já existente na mão ou criar uma para o teste.
         */

        val plusOne = Card(
            id = 101,
            cardType = CardType.PLUS_ONE
        )

        player.hand.addCard(plusOne)

        val play = Play(
            cards = listOf(plusOne)
        )

        assertTrue(
            game.canPlay(play)
        )

        game.play(play)

        assertEquals(
            6,
            game.tableValue
        )
    }


    // =========================================================
    // 10. DESISTÊNCIA ENCERRA A RODADA
    // =========================================================

    @Test
    fun surrenderEndsRoundAndAddsTableValueToScore() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val card = numberCard(100, 8)

        player.hand.addCard(card)

        game.play(
            Play(
                cards = listOf(card)
            )
        )

        assertEquals(
            8,
            game.tableValue
        )

        val scoreBefore = player.score

        assertTrue(
            game.surrender()
        )

        assertEquals(
            scoreBefore + 8,
            player.score
        )

        assertEquals(
            GameStatus.ROUND_END,
            game.gameStatus
        )

        assertTrue(
            game.isRoundOver()
        )
    }


    // =========================================================
    // 11. NÃO PODE JOGAR DEPOIS DA DESISTÊNCIA
    // =========================================================

    @Test
    fun cannotPlayAfterSurrender() {

        val game = SequenciaGame(
            qttRobots = 0
        )

        game.initialize("Jogador")

        val player = game.getCurrentPlayer()

        val first = numberCard(100, 5)

        player.hand.addCard(first)

        game.play(
            Play(
                cards = listOf(first)
            )
        )

        game.surrender()

        val second = numberCard(101, 7)

        player.hand.addCard(second)

        val play = Play(
            cards = listOf(second)
        )

        assertFalse(
            game.canPlay(play)
        )
    }


    // =========================================================
    // 12. PRÓXIMA RODADA
    // =========================================================

    @Test
    fun nextRoundStartsAfterSurrender() {

        val game = SequenciaGame(
            rounds = 3,
            qttRobots = 0
        )

        game.initialize("Jogador")

        assertEquals(
            1,
            game.getCurrentRound()
        )

        game.surrender()

        assertEquals(
            GameStatus.ROUND_END,
            game.gameStatus
        )

        game.nextRound()

        assertEquals(
            2,
            game.getCurrentRound()
        )

        assertEquals(
            GameStatus.PLAYING,
            game.gameStatus
        )

        assertEquals(
            0,
            game.tableValue
        )

        assertTrue(
            game.tableCards.isEmpty()
        )
    }


    // =========================================================
    // 13. ÚLTIMA RODADA TERMINA O JOGO
    // =========================================================

    @Test
    fun lastRoundEndsGame() {

        val game = SequenciaGame(
            rounds = 1,
            qttRobots = 0
        )

        game.initialize("Jogador")

        assertEquals(
            1,
            game.getCurrentRound()
        )

        game.surrender()

        assertEquals(
            GameStatus.ROUND_END,
            game.gameStatus
        )

        game.nextRound()

        assertEquals(
            GameStatus.GAME_OVER,
            game.gameStatus
        )
    }
}