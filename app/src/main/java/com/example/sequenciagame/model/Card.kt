package com.example.sequenciagame.model

data class Card(
    val id : Int,
    val cardType : CardType,
    val number : Int? = null,
) {
    fun isNumber() : Boolean = cardType == CardType.NUMBER

    fun isSpecial() : Boolean = cardType != CardType.NUMBER

    fun canRepresentANumber(): Boolean = cardType == CardType.NUMBER || cardType == CardType.WILD

    fun getBaseNumber():Int? = if(cardType == CardType.NUMBER) number else null

}