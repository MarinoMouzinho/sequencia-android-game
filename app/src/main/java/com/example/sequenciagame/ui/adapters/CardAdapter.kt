package com.example.sequenciagame.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sequenciagame.model.Card
import com.example.sequenciagame.R
import com.example.sequenciagame.model.CardType
import com.google.android.material.card.MaterialCardView

class CardAdapter(
    private var cards: List<Card>,
    private val onCardClicked: (Card) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private val selectedCards = mutableSetOf<Card>()
    private val wildValues = mutableMapOf<Int, Int>()

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var cardView : MaterialCardView =
            view.findViewById(R.id.cardView)
        val cardValue: TextView =
            view.findViewById(R.id.tvCardValue)

        val cardType: TextView =
            view.findViewById(R.id.tvCardType)

        var cardBackground: LinearLayout =
            view.findViewById(R.id.llCardBackground)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_card, parent, false)

        return CardViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CardViewHolder,
        position: Int
    ) {

        val card = cards[position]

        when (card.cardType) {

            CardType.NUMBER -> {
                holder.cardValue.text =
                    card.number?.toString() ?: "?"

                holder.cardType.text = "NÚMERO"

                holder.cardBackground.setBackgroundResource(R.drawable.red_card_bg)
            }

            CardType.WILD -> {
                val wildValue = wildValues[card.id]

                holder.cardValue.text =
                    wildValue?.toString() ?: "★"

                holder.cardType.text =  "CORINGA"
                holder.cardBackground.setBackgroundResource(R.drawable.black_card_bg)
            }

            CardType.SKIP -> {
                holder.cardValue.text = "⏭"
                holder.cardType.text = "PULAR"
                holder.cardBackground.setBackgroundResource(R.drawable.blue_card_bg)
            }

            CardType.REVERSE -> {
                holder.cardValue.text = "↔"
                holder.cardType.text = "INVERTER"
                holder.cardBackground.setBackgroundResource(R.drawable.blue_card_bg)
            }

            CardType.PLUS_ONE -> {
                holder.cardValue.text = "+1"
                holder.cardType.text = "MAIS 1"
                holder.cardBackground.setBackgroundResource(R.drawable.blue_card_bg)
            }
        }

        if (selectedCards.contains(card)) {
            holder.cardBackground.alpha = 0.55f
            holder.cardView.strokeWidth = 4
            holder.cardView.strokeColor = holder.itemView.context.getColor(R.color.white)
        } else {
            holder.cardBackground.alpha = 1.0f
            holder.cardView.strokeWidth = 0
        }

        holder.itemView.setOnClickListener {
            onCardClicked(card)
        }
    }

    override fun getItemCount(): Int =
        cards.size

    fun updateCards(newCards: List<Card>) {
        cards = newCards
        selectedCards.clear()
        wildValues.clear()
        notifyDataSetChanged()
    }

    fun setSelectedCards(cards: List<Card>) {
        selectedCards.clear()
        selectedCards.addAll(cards)
        notifyDataSetChanged()
    }

    fun setWildValue(card: Card, value: Int?) {
        if (value == null) {
            wildValues.remove(card.id)
        } else {
            wildValues[card.id] = value
        }

        val position = cards.indexOfFirst { it.id == card.id }

        if (position != -1) {
            notifyItemChanged(position)
        }
    }

    fun clearSelection() {
        selectedCards.clear()
        notifyDataSetChanged()
    }
}