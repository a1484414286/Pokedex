package com.example.pokedex.evolution

data class EvolutionData(
    val id: String,
    val name: String,
    val isBaby: Boolean,
    val minLevel: Int,
    val trigger: String
    // Add more fields as needed
)