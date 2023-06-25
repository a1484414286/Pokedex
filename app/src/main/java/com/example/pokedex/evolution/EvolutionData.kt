package com.example.pokedex.evolution

import org.json.JSONObject

data class EvolutionData(
    val id: String,
    val name: String,
    val isBaby: Boolean,
    val minLevel: Int,
    val trigger: String,
    val priority: Int,
    val item: String,
    val gender: String,
    val minHappiness: String,
    val timeOfDay: String,
    // Add more fields as needed
)