package com.example.pokedex.data_class

data class EvolutionData(
    val id: String,
    val name: String,
    val isBaby: Boolean,
    val minLevel: Int,
    val trigger: String,
    val priority: Int,
    val item: String,
    val gender: String,
    val minHappiness: Int,
    val timeOfDay: String,
    // Add more fields as needed
)