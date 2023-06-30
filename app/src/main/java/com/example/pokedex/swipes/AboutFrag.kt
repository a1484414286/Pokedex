package com.example.pokedex.swipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.adapter_class.EvolutionAdapter
import com.example.pokedex.data_class.Ability
import com.example.pokedex.data_class.PokeEvo

/**
 * A simple [Fragment] subclass.
 * Use the [About.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFrag(var aboutFragMap: HashMap<String,Any>) : Fragment() {
    private lateinit var evolutionRecyclerView: RecyclerView
    private lateinit var evolutionAdapter: EvolutionAdapter
    private lateinit var evolutionList: MutableList<PokeEvo>

    private lateinit var abilitiesList : MutableList<Ability>
    private lateinit var aboutStats : HashMap<String,Any>

    private fun initializer()
    {
        evolutionList = aboutFragMap["evolution"] as MutableList<PokeEvo>
        abilitiesList = aboutFragMap["abilities"] as MutableList<Ability>
        aboutStats = aboutFragMap["stats"] as HashMap<String, Any>

    }
    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_about, container, false)
        initializer()
        evolutionRecyclerView = rootView.findViewById(R.id.recyclerViewEvolution)
        evolutionRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        evolutionList = evolutionList.sortedBy { it.priority } as MutableList<PokeEvo>
        evolutionAdapter = EvolutionAdapter(evolutionList)
        evolutionRecyclerView.adapter = evolutionAdapter

        abilityHide(rootView)
        setValue(rootView)
        return rootView
    }

    private fun setValue(rootView : View)
    {
        val heightInMeters = aboutStats["height"].toString().toDouble() / 10
        val weightInKilo = aboutStats["weight"].toString().toDouble() / 10
        rootView.findViewById<TextView>(R.id.baseEXPData).text = aboutStats["base_exp"].toString()
        rootView.findViewById<TextView>(R.id.heightData).text = convertMetersToFeet(heightInMeters)
        rootView.findViewById<TextView>(R.id.weightText).text = convertKiloToPound(weightInKilo)
    }
    @SuppressLint("CutPasteId")
    private fun abilityHide(rootView : View)
    {
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
    }
    private fun convertMetersToFeet(meters: Double): String {
        val feetPerMeter = 3.28084
        val feetTotal = meters * feetPerMeter
        val feet = feetTotal.toInt()
        val inches = ((feetTotal - feet) * 12).toInt()
        Toast.makeText(this.context, meters.toString(), Toast.LENGTH_SHORT).show()

        return "$feet'${String.format("%02d", inches)}\""
    }

    private fun convertKiloToPound(Kilo: Double): String {
        val lbsPerKilo = 2.205
        val lbsTotal = Kilo * lbsPerKilo
        val lbs = lbsTotal.toInt()
        return "$lbs lbs"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val abilitiesTextView = view.findViewById<TextView>(R.id.abilities)
//        abilitiesTextView.text = "Abilities"
    }
}


