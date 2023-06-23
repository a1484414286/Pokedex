package com.example.pokedex.swipes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pokedex.evolution.PokeEvo

class PageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val aboutData: ArrayList<PokeEvo>) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2 // Number of fragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> About(aboutData)
            1 -> Abilities()
            else -> Move()
        }
    }
}

