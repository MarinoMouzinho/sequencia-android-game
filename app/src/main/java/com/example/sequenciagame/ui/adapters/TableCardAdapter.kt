package com.example.sequenciagame.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sequenciagame.R
import com.example.sequenciagame.model.Card
import com.example.sequenciagame.model.CardType

class TableCardAdapter(
    private var cards: List<Card> = emptyList()
) : RecyclerView.Adapter<TableCardAdapter.CardViewHolder>() {

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val cardValue: TextView =
            view.findViewById(R.id.tvCardValue)

        val cardType: TextView =
            view.findViewById(R.id.tvCardType)

        val cardBackground: LinearLayout =
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

                holder.cardBackground.setBackgroundResource(
                    R.drawable.red_card_bg
                )
            }

            CardType.WILD -> {
                holder.cardValue.text = "★"
                holder.cardType.text = "CORINGA"
                holder.cardBackground.setBackgroundResource(
                    R.drawable.black_card_bg
                )
            }

            CardType.SKIP -> {
                holder.cardValue.text = "⏭"
                holder.cardType.text = "PULAR"
                holder.cardBackground.setBackgroundResource(
                    R.drawable.blue_card_bg
                )
            }

            CardType.REVERSE -> {
                holder.cardValue.text = "↔"
                holder.cardType.text = "INVERTER"
                holder.cardBackground.setBackgroundResource(
                    R.drawable.blue_card_bg
                )
            }

            CardType.PLUS_ONE -> {
                holder.cardValue.text = "+1"
                holder.cardType.text = "MAIS 1"
                holder.cardBackground.setBackgroundResource(
                    R.drawable.blue_card_bg
                )
            }
        }

        /*
         * position 0 = mais antiga das três
         * position 1 = penúltima
         * position 2 = última
         */
        when (position) {

            0 -> {
                holder.itemView.alpha = 0.10f
                holder.itemView.rotation = -12f
                holder.itemView.translationX = -20f
                holder.itemView.translationY = 8f
                holder.itemView.translationZ = 0f
            }

            1 -> {
                holder.itemView.alpha = 0.40f
                holder.itemView.rotation = 10f
                holder.itemView.translationX = 20f
                holder.itemView.translationY = 4f
                holder.itemView.translationZ = 1f
            }

            2 -> {
                holder.itemView.alpha = 1.0f
                holder.itemView.rotation = 0f
                holder.itemView.translationX = 0f
                holder.itemView.translationY = 0f
                holder.itemView.translationZ = 2f
            }
        }
    }

    override fun getItemCount(): Int =
        cards.size

    fun updateCards(newCards: List<Card>) {

        cards = newCards
            .takeLast(3)

        notifyDataSetChanged()
    }
}