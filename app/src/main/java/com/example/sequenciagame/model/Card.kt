package com.example.sequenciagame.model

data class Card(
    val id : Int,
    val number : Int,
    val suit : Suit,
    val imageRes : Int = 0
) {

    fun matches(other : Card) : Boolean = (this.number >= other.number)

    fun isDouble(other : Card) : Boolean = (number == other.number)

}