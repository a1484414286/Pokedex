package com.example.pokedex.swipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.evolution.EvolutionAdapter
import com.example.pokedex.evolution.PokeEvo
import com.example.pokedex.main.Ability
import org.w3c.dom.Text
import java.util.PriorityQueue

/**
 * A simple [Fragment] subclass.
 * Use the [About.newInstance] factory method to
 * create an instance of this fragment.
 */
class About(var evolutionTree: Any, var abilitiesTree:Any?) : Fragment() {
    private lateinit var evolutionRecyclerView: RecyclerView
    private lateinit var evolutionAdapter: EvolutionAdapter
    private lateinit var evolutionList: PriorityQueue<PokeEvo>

    private lateinit var abilitiesList : MutableList<Ability>
    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_about, container, false)
        evolutionRecyclerView = rootView.findViewById(R.id.recyclerViewEvolution)
        evolutionRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        evolutionList = evolutionTree as PriorityQueue<PokeEvo>


        for(i in evolutionList)
        {
            i.imageId = fetchResourceID(i.imageId.toString())
        }
        evolutionAdapter = EvolutionAdapter(evolutionList)
        evolutionRecyclerView.adapter = evolutionAdapter

        abilitiesList = abilitiesTree as MutableList<Ability>

        when (abilitiesList.size) {
            1 -> {
                rootView.findViewById<TextView>(R.id.ability2).text = abilitiesList[0].name
                rootView.findViewById<TextView>(R.id.ability1).visibility = View.GONE
                rootView.findViewById<TextView>(R.id.ability3).visibility = View.GONE
            }
            2 -> {
                rootView.findViewById<TextView>(R.id.ability1).text = abilitiesList[0].name
                rootView.findViewById<TextView>(R.id.ability3).text = abilitiesList[1].name
                rootView.findViewById<TextView>(R.id.ability2).visibility = View.GONE
            }
            else -> {
                rootView.findViewById<TextView>(R.id.ability1).text = abilitiesList[0].name
                rootView.findViewById<TextView>(R.id.ability2).text = abilitiesList[1].name
                rootView.findViewById<TextView>(R.id.ability3).text = abilitiesList[2].name

            }
        }

        return rootView
    }
    @SuppressLint("DiscouragedApi")
    private fun fetchResourceID( drawableName : String): Int {
        val resources = requireContext().resources
        val packageName = requireContext().packageName
        var drawableResourceId = resources.getIdentifier("p$drawableName", "drawable", packageName)
        if (drawableResourceId == 0) {
            val name = "p${drawableName}_f"
            drawableResourceId =
                resources.getIdentifier(name, "drawable", packageName)
        }
        return drawableResourceId
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val abilitiesTextView = view.findViewById<TextView>(R.id.abilities)
        abilitiesTextView.text = "Abilities"
    }
}


