package com.example.pokedex

import Pokemon
import android.annotation.SuppressLint
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList : MutableList<Pokemon>
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.verticalRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        pokemonList = ArrayList()
        val adapter = PokedexAdapter(this, pokemonList)
        recyclerView.adapter = adapter
        onclickAdd();
    }

    private fun onclickAdd()
    {
        val pokemon2 = Pokemon(R.drawable.ani_bw_006, "Charizard", "Fire")
        val pokemon3 = Pokemon(R.drawable.ani_bw_006, "Charizard", "Fire", "Flying")
        pokemonList.add(pokemon3)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        pokemonList.add(pokemon2)
        recyclerView.adapter?.notifyDataSetChanged()
    }

}