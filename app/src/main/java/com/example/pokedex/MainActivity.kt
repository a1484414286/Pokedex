package com.example.pokedex

import Pokemon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler

import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList : MutableList<Pokemon>;
    private lateinit var recyclerView: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pokemon = Pokemon(R.drawable.ani_bw_006,"Charizard","Fire")
        pokemonList = ArrayList();
        pokemonList.add(pokemon);

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this);

        val adapter = PokedexAdapter(this, pokemonList);
        recyclerView.adapter = adapter;
    }



}