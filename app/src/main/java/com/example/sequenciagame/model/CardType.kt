package com.example.sequenciagame.model

enum class CardType{
    NUMBER, // cartas de 1-12
    SKIP, // pula a vez
    REVERSE, // inverte a ordem de jogo (pula a vez)
    PLUS_ONE, // topo do monte +1
    WILD // coringa
}