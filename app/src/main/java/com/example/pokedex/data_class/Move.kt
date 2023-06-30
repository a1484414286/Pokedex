package com.example.pokedex.data_class

data class Move(
    val lvl: Long,
    val name: String,
    val type: String,
    val category: String,
    val power: Long,
    val accuracy: Long,
    val pp: Long
)
