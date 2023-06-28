package com.example.pokedex.swipes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pokedex.evolution.PokeEvo
import com.example.pokedex.main.Ability

class PageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val aboutData: ArrayList<PokeEvo>,
    private val abilitiesList: ArrayList<Ability>,
    private val aboutStats: HashMap<String, Any>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3 // Number of fragments
    }

    override fun createFragment(position: Int): Fragment
    {
        return when (position) {
            0 -> About(aboutData,abilitiesList, aboutStats)
            1 -> AbilitiesFrag()
            else -> MoveFrag()
        }
    }
}

