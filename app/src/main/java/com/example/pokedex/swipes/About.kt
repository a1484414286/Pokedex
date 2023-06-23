package com.example.pokedex.swipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.evolution.EvolutionAdapter
import com.example.pokedex.evolution.PokeEvo

/**
 * A simple [Fragment] subclass.
 * Use the [About.newInstance] factory method to
 * create an instance of this fragment.
 */
class About (var data : Any): Fragment() {
    private lateinit var evolutionRecyclerView: RecyclerView
    private lateinit var evolutionAdapter: EvolutionAdapter
    private lateinit var evolutionList : MutableList<PokeEvo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_about, container, false)
        evolutionRecyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerViewEvolution)
        evolutionRecyclerView.layoutManager = LinearLayoutManager(inflater.context)
        evolutionList = data as MutableList<PokeEvo>;
        evolutionAdapter = EvolutionAdapter(evolutionList)
        evolutionRecyclerView.adapter = evolutionAdapter
        Log.e("RUNTIME_DATA",data.toString())
        return rootView
    }


    fun updateAbilitiesText(newText: String) {
        val abilitiesTextView = requireView().findViewById<TextView>(R.id.ability1)
        abilitiesTextView.text = newText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val abilitiesTextView = view.findViewById<TextView>(R.id.abilities)
        abilitiesTextView.text = "New Value"

        // You can access other views and perform any necessary operations here
    }
}


