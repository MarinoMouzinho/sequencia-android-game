package com.example.sequenciagame.model

import com.example.sequenciagame.ai.Difficulty
import com.example.sequenciagame.player.AIPlayer
import com.example.sequenciagame.player.HumanPlayer
import com.example.sequenciagame.player.Player

class SequenciaGame(
    private val rounds: Int = 3,
    private val qttRobots: Int = 3,
    private val initialHandSize: Int = 5,
    private val specialCard: Boolean = true
) {
    val players = mutableListOf<Player>()
    val tableCards = mutableListOf<Card>()

    var tableValue:Int = 0
        private set

    val turnHistory = mutableListOf<Turn>()

    private lateinit var deck: Deck

    private var currentRound: Int = 0

    var currentPlayerIndex: Int = 0
        private set

    private var turnDirection: Int = 1 // 1 - normal | -1 - invertido

    var gameStatus: GameStatus = GameStatus.WAITING
        private set

    fun initialize(humanName: String = "Jogador") {
        players.clear()
        players.add(HumanPlayer(humanName))

        val difficulties = listOf(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD)

        for (i in 0 until qttRobots) {
            val difficulty = difficulties[i.coerceAtMost(difficulties.lastIndex)]
            players.add(AIPlayer(name = "Bot ${i + 1}", difficulty = difficulty))
        }

        currentRound = 0
        players.forEach {
            it.score = 0
            it.hand.clear()
        }

        startNewRound()
    }

    private fun startNewRound() {
        currentRound++
        deck = Deck(specialCard)
        tableCards.clear()
        tableValue = 0
        turnHistory.clear()
        turnDirection = 1
        players.forEach { it.hand.clear() }
        distributeInitialCards()
        currentPlayerIndex = 0
        gameStatus = GameStatus.PLAYING
    }

    private fun distributeInitialCards() {
        repeat(initialHandSize) {
            players.forEach { player ->
                deck.drawCard()?.let { card ->
                    player.hand.addCard(card)
                }
            }
        }
    }

    fun getCurrentPlayer(): Player = players[currentPlayerIndex]

    fun getCurrentRound(): Int = currentRound

    fun getDeckSize(): Int = deck.size()

    fun canPlay(play: Play): Boolean {
        if (gameStatus != GameStatus.PLAYING) return false
        if (play.cards.isEmpty()) return false
        if (play.cards.size > 2) return false
        if (!play.isValid()) return false

        val player = getCurrentPlayer()
        if (!player.hand.containsPlay(play)) return false

        val firstCard = play.cards.first()

        if (
            firstCard.cardType == CardType.SKIP ||
            firstCard.cardType == CardType.REVERSE ||
            firstCard.cardType == CardType.PLUS_ONE
        ) {
            return play.cards.size == 1
        }

        val playValue = play.getValue() ?: return false
        if (tableValue == 0) return true

        return playValue >= tableValue
    }

    fun play(play: Play): Boolean {
        if (!canPlay(play)) return false

        val player = getCurrentPlayer()

        if (!player.hand.removePlay(play)) return false
        tableCards.addAll(play.cards)

        when (play.cards.first().cardType) {
            CardType.SKIP -> {
                recordTurn(player, play)
                refillHand(player)
                nextPlayer()
                nextPlayer()
                return true
            }

            CardType.REVERSE -> {
                recordTurn(player, play)
                refillHand(player)
                turnDirection *= -1
                nextPlayer()
                nextPlayer()
                return true
            }

            CardType.PLUS_ONE -> {
                tableValue = if (tableValue == 0) 1 else tableValue + 1
            }

            CardType.NUMBER,
            CardType.WILD -> {
                val playValue = play.getValue()
                    ?: return false

                tableValue = if (
                    tableValue > 0 &&
                    playValue == tableValue
                ) {
                    tableValue * 2
                } else {
                    playValue
                }
            }
        }

        recordTurn(player, play)
        refillHand(player)
        nextPlayer()

        return true
    }

    fun surrender(): Boolean {
        if (gameStatus != GameStatus.PLAYING) return false

        val player = getCurrentPlayer()
        player.score += tableValue
        gameStatus = GameStatus.ROUND_END

        return true
    }

    private fun refillHand(player: Player) {
        while (player.hand.size < initialHandSize && !deck.isEmpty()) {
            deck.drawCard()?.let { card ->
                player.hand.addCard(card)
            }
        }
    }

    private fun nextPlayer() {
        if (players.isEmpty()) return
        currentPlayerIndex = (currentPlayerIndex + turnDirection).mod(players.size)
    }

    private fun recordTurn(player: Player, play: Play) {
        turnHistory.add(
            Turn(player=player, cardsPlayed = play.cards)
        )
    }

    fun isRoundOver(): Boolean = gameStatus == GameStatus.ROUND_END

    fun isGameOver(): Boolean = currentRound >= rounds

    fun nextRound() {
        if (!isRoundOver()) return

        if (currentRound >= rounds) {
            endGame()
            return
        }

        startNewRound()
    }

    private fun endGame() {
        gameStatus = GameStatus.GAME_OVER
    }

    fun getWinner(): Player? {
        if (players.isEmpty()) return null

        return players.minByOrNull {
            it.score
        }
    }

    fun getValidPlays(player: Player = getCurrentPlayer()): List<Play> {
        if (gameStatus != GameStatus.PLAYING) {
            return emptyList()
        }

        if (player != getCurrentPlayer()) {
            return emptyList()
        }

        return player.hand.getPossiblePlays()
            .filter { play -> canPlay(play) }
    }
}