package com.example.sequenciagame.player

import com.example.sequenciagame.model.Play

class HumanPlayer(
    name: String
) : Player(name, true) {

    override fun choosePlay(
        validPlays: List<Play>
    ): Play? = null

    override fun shouldDraw(
        validPlays: List<Play>
    ): Boolean = validPlays.isEmpty()
}