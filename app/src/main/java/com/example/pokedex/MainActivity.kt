package com.example.pokedex

import Pokemon
import android.annotation.SuppressLint
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList : MutableList<Pokemon>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val pokemon1 = Pokemon(R.drawable.ground, "Pokemon 1", "Water")

        pokemonList = ArrayList()
        pokemonList.add(pokemon1)

        val adapter = PokedexAdapter(this, pokemonList)
        recyclerView.adapter = adapter
        Log.d("before added", "before it's added")
        onclickAdd();
        Log.d("passed","should be rendered")

    }


    private fun onclickAdd()
    {
        val pokemon2 = Pokemon(R.drawable.ani_bw_006, "Charizard", "Fire")
        pokemonList.add(pokemon2)

    }

}