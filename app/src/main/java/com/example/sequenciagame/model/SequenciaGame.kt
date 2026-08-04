package com.example.sequenciagame.model

import com.example.sequenciagame.ai.AIStrategy
import com.example.sequenciagame.player.AIPlayer
import com.example.sequenciagame.player.HumanPlayer
import com.example.sequenciagame.player.Player

class SequenciaGame(
    private val maxRounds: Int = 3,
    private val qttRobots: Int = 3
) {
    val players = mutableListOf<Player>()
    val tableCards = mutableListOf<Card>()
    val turnHistory = mutableListOf<Turn>()
    private val deck = Deck()
    private var currentRound = 0

    var currentPlayerIndex: Int = 0
        private set
    var gameStatus : GameStatus = GameStatus.WAITING
        private set

    fun initialize(humanName: String = "Jogador") {
        players.clear()
        players.add(HumanPlayer(humanName))

        //TODO:Substituir e implementar quantidade dinamica de AIs (min 1.. max 3)
        players.add(AIPlayer("Bot Fácil", AIStrategy.Difficulty.EASY))
        players.add(AIPlayer("Bot Médio", AIStrategy.Difficulty.MEDIUM))
        players.add(AIPlayer("Bot Difícil", AIStrategy.Difficulty.HARD))

        currentRound = 0
        players.forEach { it.score = 0 }
        startNewRound()
    }

    private fun startNewRound(){
        currentRound++
        deck.reset()
        tableCards.clear()
        players.forEach { it.hand.clear() }

        repeat(5){
            players.forEach { player ->
                deck.drawCard()?.let { player.hand.addCard(it)}
            }
        }

        deck.drawCard()?.let { tableCards.add(it) }

        currentPlayerIndex = 0
        gameStatus = GameStatus.PLAYING
    }

    fun getValidCards(player: Player) : List<Card> {
        val topCard = tableCards.lastOrNull()
        return player.hand.getPlayableCards(topCard)
    }

    fun playCard(player: Player, card: Card) : Boolean{
        if(gameStatus != GameStatus.PLAYING) return false
        if(player != players[currentPlayerIndex]) return false

        val validCards = getValidCards(player)
        if(card !in validCards) return false

        if(!player.hand.removeCard(card)) return false
        tableCards.add(card)

        val matched = if(tableCards.size >= 2) {
            val previousCard = tableCards[tableCards.size - 2]
            card.isDouble(previousCard)
        } else false

        if(matched) {
            val collectedCount = tableCards.size
            player.score += collectedCount
            tableCards.clear()
        }

        turnHistory.add(Turn(player, card, matched))

        if(player.hand.size == 0) endRound()
        else nextTurn()

        return true
    }

    fun passTurn(player : Player) {
        if(gameStatus != GameStatus.PLAYING) return
        if(player != players[currentPlayerIndex]) return

        //TODO: Trocar regra de negocio para formato original
        deck.drawCard()?.let{ player.hand.addCard(it) }
        turnHistory.add(Turn(player, null))
        nextTurn()
    }

    fun endRound() {
        gameStatus = GameStatus.ROUND_END

        players.forEach { player ->
            if(player.hand.size > 0) {
                val penalty = player.hand.cards.sumOf { it.number }
                player.score += penalty
            }
        }

        if(currentRound >= maxRounds) gameStatus = GameStatus.GAME_OVER
    }

    fun nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
    }

    fun startNextRound() {
        if(gameStatus == GameStatus.ROUND_END && currentRound < maxRounds) startNewRound()
    }

    fun getCurrentPlayer() : Player = players[currentPlayerIndex]
    fun isHumanTurn() : Boolean = getCurrentPlayer().isHuman

}