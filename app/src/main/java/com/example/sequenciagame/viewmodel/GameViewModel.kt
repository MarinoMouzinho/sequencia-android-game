package com.example.sequenciagame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sequenciagame.model.Card
import com.example.sequenciagame.model.GameStatus
import com.example.sequenciagame.model.Play
import com.example.sequenciagame.model.SequenciaGame
import com.example.sequenciagame.player.Player

class GameViewModel : ViewModel() {

    private val game = SequenciaGame()

    private val _playerHand = MutableLiveData<List<Card>>()
    val playerHand: LiveData<List<Card>> = _playerHand

    private val _tableCards = MutableLiveData<List<Card>>()
    val tableCards: LiveData<List<Card>> = _tableCards

    private val _tableValue = MutableLiveData<Int>()
    val tableValue: LiveData<Int> = _tableValue

    private val _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player> = _currentPlayer

    private val _gameStatus = MutableLiveData<GameStatus>()
    val gameStatus: LiveData<GameStatus> = _gameStatus

    private val _scores = MutableLiveData<Map<String, Int>>()
    val scores: LiveData<Map<String, Int>> = _scores

    private val _possiblePlays = MutableLiveData<List<Play>>()
    val possiblePlays: LiveData<List<Play>> = _possiblePlays

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _currentRound = MutableLiveData<Int>()
    val currentRound: LiveData<Int> = _currentRound

    fun startNewGame(humanName: String = "Você") {
        game.initialize(humanName)

        updateUI()
    }

    fun onPlaySelected(play: Play) {

        if (game.gameStatus != GameStatus.PLAYING)  return

        val current = game.getCurrentPlayer()

        if (!current.isHuman)  return

        if (!game.canPlay(play)) {
            _message.value = "Essa jogada não é válida."
            return
        }

        game.play(play)
        updateUI()
        executeAITurnIfNeeded()
    }

    fun onSurrender() {
        if (game.gameStatus != GameStatus.PLAYING) return

        val current = game.getCurrentPlayer()

        if (!current.isHuman) return

        game.surrender()
        updateUI()
    }

    fun startNextRound() {

        if (game.gameStatus != GameStatus.ROUND_END) return

        game.nextRound()
        updateUI()
        executeAITurnIfNeeded()
    }

    private fun executeAITurnIfNeeded() {

        if (game.gameStatus != GameStatus.PLAYING)  return

        if (game.getCurrentPlayer().isHuman) return

        executeAITurn()
    }

    private fun executeAITurn() {

        android.os.Handler(
            android.os.Looper.getMainLooper()
        ).postDelayed({

            if (game.gameStatus != GameStatus.PLAYING) {
                return@postDelayed
            }

            val aiPlayer = game.getCurrentPlayer()

            val validPlays = game.getValidPlays(aiPlayer)

            if (validPlays.isNotEmpty()) {

                val chosenPlay = aiPlayer.choosePlay(validPlays)

                if (chosenPlay != null) {
                    game.play(chosenPlay)
                } else {
                    game.surrender()
                }

            } else {
                game.surrender()
            }

            updateUI()

            if (
                game.gameStatus == GameStatus.PLAYING &&
                _currentPlayer.value?.isHuman == false
            ) {
                executeAITurn()
            }

        }, 800)
    }

    private fun updateUI() {

        val human = game.players.find {
            it.isHuman
        }

        _playerHand.value =
            human?.hand?.cards ?: emptyList()

        _tableCards.value =
            game.tableCards.toList()

        _tableValue.value =
            game.tableValue

        _currentPlayer.value =
            game.getCurrentPlayer()

        _currentRound.value =
            game.getCurrentRound()

        _gameStatus.value =
            game.gameStatus

        _scores.value =
            game.players.associate {
                it.name to it.score
            }

        if (
            human != null &&
            game.gameStatus == GameStatus.PLAYING &&
            game.getCurrentPlayer().isHuman
        ) {

            _possiblePlays.value =
                human.hand
                    .getPossiblePlays()
                    .filter { play ->
                        game.canPlay(play)
                    }

            _message.value =
                "Sua vez! Escolha uma jogada ou desista."

        } else {

            _possiblePlays.value =
                emptyList()

            if (game.gameStatus == GameStatus.PLAYING) {

                _message.value =
                    "Vez de ${game.getCurrentPlayer().name}"
            }
        }

        if (game.gameStatus == GameStatus.ROUND_END) {
            val lastPlayer = game.getCurrentPlayer() //TODO: Mensagem de Jogador ganhou X pontos

            _message.value =
                "Rodada ${game.getCurrentRound()} terminou!"
        }

        if (game.gameStatus == GameStatus.GAME_OVER) {

            val winner =
                game.players.minByOrNull {
                    it.score
                }

            _message.value =
                "Fim de jogo! Vencedor: ${
                    winner?.name ?: "Empate"
                }"
        }
    }
}