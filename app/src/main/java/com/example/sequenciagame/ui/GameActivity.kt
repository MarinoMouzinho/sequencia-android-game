package com.example.sequenciagame.ui

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sequenciagame.R
import com.example.sequenciagame.model.Card
import com.example.sequenciagame.model.CardType
import com.example.sequenciagame.model.GameStatus
import com.example.sequenciagame.model.Play
import com.example.sequenciagame.ui.adapters.CardAdapter
import com.example.sequenciagame.ui.adapters.TableCardAdapter
import com.example.sequenciagame.viewmodel.GameViewModel

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    private lateinit var cardAdapter: CardAdapter
    private lateinit var tableCardAdapter: TableCardAdapter
    private lateinit var tableCardStack: FrameLayout
    private lateinit var tvTableValue: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnPlay: Button
    private lateinit var btnSurrender: Button

    private val selectedCards = mutableListOf<Card>()
    private var possiblePlays = emptyList<Play>()
    private var roundEndDialog: Dialog? = null
    private var selectedWildValue: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupCards()
        //setupTableCards()
        observeViewModel()
        updatePlayButton()

        viewModel.startNewGame()
    }

    private fun initializeViews() {

        tvTableValue = findViewById(R.id.tvTableValue)
        tvMessage = findViewById(R.id.tvMessage)

        btnSurrender = findViewById(R.id.btnSurrender)
        btnPlay = findViewById(R.id.btnPlay)

        tableCardStack = findViewById(R.id.tableCardStack)

        btnPlay.setOnClickListener {
            executeSelectedPlay()
        }

        btnSurrender.setOnClickListener {
            selectedCards.clear()
            viewModel.onSurrender()
        }
    }

    private fun setupCards() {
        val recyclerView =
            findViewById<RecyclerView>(R.id.rvPlayerHand)

        cardAdapter = CardAdapter(
            cards = emptyList(),
            onCardClicked = { card ->
                onCardClicked(card)
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@GameActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = cardAdapter
        }
    }

    private fun observeViewModel(){
        viewModel.playerHand.observe(this) { cards ->
            cardAdapter.updateCards(cards)
        }

        viewModel.possiblePlays.observe(this) { plays ->
            possiblePlays = plays
        }

        viewModel.tableValue.observe(this) { value ->
            tvTableValue.text = value.toString()
        }

        //TODO: Alterar para string com placeholder
        viewModel.tableCards.observe(this) { cards ->
//            findViewById<TextView>(
//                R.id.tvTableCards
//            ).text = "Monte: ${cards.size} cartas"

            //tableCardAdapter.updateCards(cards)
            updateTableCardStack(cards)
        }

        viewModel.message.observe(this) { message ->
            tvMessage.text = message
        }

        viewModel.currentPlayer.observe(this) { player ->
            findViewById<TextView>(
                R.id.tvTurn
            ).text = player.name
        }

        viewModel.scores.observe(this) { scores ->
            val humanScore =
                scores.entries.firstOrNull()?.value ?: 0

            findViewById<TextView>(
                R.id.tvScore
            ).text = "$humanScore pontos" //TODO: Alterar para string com placeholder
        }

        viewModel.gameStatus.observe(this) { status ->
            if (
                status == GameStatus.ROUND_END ||
                status == GameStatus.GAME_OVER
            ) {
                showRoundEndDialog()
            }
        }

        viewModel.currentRound.observe(this) { round ->
            findViewById<TextView>(R.id.tvRound).text =
                resources.getString(R.string.rounds, round, 3)
        }
    }

    private fun onCardClicked(card: Card) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card)

            if (card.cardType == CardType.WILD) {
                selectedWildValue = null

                cardAdapter.setWildValue(card, null)
            }

            cardAdapter.setSelectedCards(selectedCards)
            updatePlayButton()

            return
        }

        if (selectedCards.size >= 2) {
            clearSelection()
        }

        if (card.cardType == CardType.WILD) {
            showWildValueDialog(card)
            updatePlayButton()
            return
        }

        selectedCards.add(card)
        cardAdapter.setSelectedCards(selectedCards)
        updatePlayButton()
    }

    private fun executeSelectedPlay() {

        if (selectedCards.isEmpty()) {
            tvMessage.text = "Selecione uma carta."
            return
        }

        val play = findMatchingPlay(selectedCards)

        if (play == null) {
            tvMessage.text =
                "Essa combinação não é uma jogada válida."
            return
        }

        selectedCards.clear()
        selectedWildValue = null

        cardAdapter.clearSelection()
        clearSelection()
        viewModel.onPlaySelected(play)
    }

    private fun findMatchingPlay(
        cards: List<Card>
    ): Play? {
        return possiblePlays.firstOrNull { play ->
            if (play.cards.size != cards.size) return@firstOrNull false
            if (!play.cards.containsAll(cards)) return@firstOrNull false

            if (play.containsWild()) {
                if (selectedWildValue == null) return@firstOrNull false

                val numberCard = cards.firstOrNull {
                    it.cardType == CardType.NUMBER
                }

                if (numberCard != null) {
                    return@firstOrNull (
                            selectedWildValue == numberCard.number &&
                                    play.wildValue == selectedWildValue
                            )
                }

                return@firstOrNull (play.wildValue == selectedWildValue)
            }
            true
        }
    }

    private fun showWildValueDialog(wildCard: Card) {

        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wild_value)

        dialog.window?.setBackgroundDrawableResource(
            android.R.color.transparent
        )

        val buttons = listOf(
            R.id.btnWild1 to 1,
            R.id.btnWild2 to 2,
            R.id.btnWild3 to 3,
            R.id.btnWild4 to 4,
            R.id.btnWild5 to 5,
            R.id.btnWild6 to 6,
            R.id.btnWild7 to 7,
            R.id.btnWild8 to 8,
            R.id.btnWild9 to 9,
            R.id.btnWild10 to 10,
            R.id.btnWild11 to 11,
            R.id.btnWild12 to 12
        )

        buttons.forEach { (buttonId, value) ->
            dialog.findViewById<Button>(buttonId)
                .setOnClickListener {
                    selectedWildValue = value
                    if (!selectedCards.contains(wildCard)) {
                        selectedCards.add(wildCard)
                    }
                    cardAdapter.setWildValue(wildCard, selectedWildValue)
                    cardAdapter.setSelectedCards(selectedCards)
                    updatePlayButton()
                    dialog.dismiss()
                }
        }

        dialog.findViewById<Button>(R.id.btnCancelWild)
            .setOnClickListener {
                selectedWildValue = null
                selectedCards.remove(wildCard)
                cardAdapter.setWildValue(wildCard, selectedWildValue)
                cardAdapter.setSelectedCards(selectedCards)
                updatePlayButton()
                dialog.dismiss()
            }

        dialog.show()
    }

    private fun showRoundEndDialog() {

        if (roundEndDialog?.isShowing == true) return

        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_round_end)

        dialog.window?.setBackgroundDrawableResource(
            android.R.color.transparent
        )

        val tvTitle =
            dialog.findViewById<TextView>(R.id.tvRoundEndTitle)

        val tvMessage =
            dialog.findViewById<TextView>(R.id.tvRoundEndMessage)

        val tvScore =
            dialog.findViewById<TextView>(R.id.tvRoundEndScore)

        val btnNextRound =
            dialog.findViewById<Button>(R.id.btnNextRound)

        val btnNewGame =
            dialog.findViewById<Button>(R.id.btnNewGame)

        val round = viewModel.currentRound.value ?: 1
        val status = viewModel.gameStatus.value

        if (status == GameStatus.GAME_OVER) {

            tvTitle.text = "FIM DE JOGO"

            tvMessage.text =
                viewModel.message.value ?: "Fim de jogo!"

            tvScore.text = ""

            btnNextRound.visibility = Button.GONE
            btnNewGame.visibility = Button.VISIBLE

            btnNewGame.setOnClickListener {
                dialog.dismiss()
                viewModel.startNewGame()
            }

        } else {

            tvTitle.text = "FIM DA RODADA"

            tvMessage.text =
                "Rodada $round terminou!"

            val score =
                viewModel.scores.value?.entries
                    ?.firstOrNull()
                    ?.value ?: 0

            tvScore.text =
                "Sua pontuação: $score"

            btnNextRound.visibility = Button.VISIBLE
            btnNewGame.visibility = Button.GONE

            btnNextRound.setOnClickListener {
                dialog.dismiss()
                viewModel.startNextRound()
            }
        }

        roundEndDialog = dialog

        dialog.setOnDismissListener {
            roundEndDialog = null
        }

        dialog.show()
    }

    private fun clearSelection() {
        selectedCards.clear()
        selectedWildValue = null
        cardAdapter.setSelectedCards(emptyList())
        updatePlayButton()
    }

    private fun updatePlayButton() {
        if (selectedCards.isEmpty()) {
            btnPlay.isEnabled = false
            btnPlay.backgroundTintList =
                getColorStateList(R.color.mediumgray)
        } else {
            btnPlay.isEnabled = true
            btnPlay.backgroundTintList =
                getColorStateList(R.color.green)
        }
    }

    private fun updateTableCardStack(cards: List<Card>) {
        tableCardStack.removeAllViews()

        if (cards.isEmpty()) return
        val visibleCards = cards.takeLast(3)
        visibleCards.forEachIndexed { index, card ->
            val cardView = layoutInflater.inflate(
                R.layout.item_card,
                tableCardStack,
                false
            )

            val cardValue =
                cardView.findViewById<TextView>(R.id.tvCardValue)
            val cardType =
                cardView.findViewById<TextView>(R.id.tvCardType)
            val cardBackground =
                cardView.findViewById<LinearLayout>(
                    R.id.llCardBackground
                )

            when (card.cardType) {
                CardType.NUMBER -> {
                    cardValue.text =
                        card.number?.toString() ?: "?"
                    cardType.text = "NÚMERO"
                    cardBackground.setBackgroundResource(
                        R.drawable.red_card_bg
                    )
                }

                CardType.WILD -> {
                    cardValue.text =
                        card.number?.toString() ?: "★"
                    cardType.text = "CORINGA"
                    cardBackground.setBackgroundResource(
                        R.drawable.black_card_bg
                    )
                }

                CardType.SKIP -> {
                    cardValue.text = "⏭"
                    cardType.text = "PULAR"

                    cardBackground.setBackgroundResource(
                        R.drawable.blue_card_bg
                    )
                }

                CardType.REVERSE -> {
                    cardValue.text = "↔"
                    cardType.text = "INVERTER"
                    cardBackground.setBackgroundResource(
                        R.drawable.blue_card_bg
                    )
                }

                CardType.PLUS_ONE -> {
                    cardValue.text = "+1"
                    cardType.text = "MAIS 1"
                    cardBackground.setBackgroundResource(
                        R.drawable.blue_card_bg
                    )
                }
            }

            val params = FrameLayout.LayoutParams(
                dpToPx(72),
                dpToPx(108)
            )

            params.gravity = Gravity.CENTER

            when (index) {
                0 -> {
                    cardView.alpha = 0.10f
                    cardView.rotation = -10f
                    cardView.translationX = dpToPx(-8).toFloat()
                    cardView.translationY = dpToPx(4).toFloat()
                }

                1 -> {
                    cardView.alpha = 0.40f
                    cardView.rotation = 8f
                    cardView.translationX = dpToPx(6).toFloat()
                    cardView.translationY = dpToPx(2).toFloat()
                }

                2 -> {
                    cardView.alpha = 1.0f
                    cardView.rotation = 0f
                    cardView.translationX = 0f
                    cardView.translationY = 0f
                }
            }

            tableCardStack.addView(cardView, params)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}