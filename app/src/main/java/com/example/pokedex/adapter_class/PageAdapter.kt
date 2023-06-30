package com.example.pokedex.adapter_class

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pokedex.data_class.Move
import com.example.pokedex.swipes.AboutFrag
import com.example.pokedex.swipes.MoveFrag
import com.example.pokedex.swipes.StatsFrag

class PageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val aboutFragMap: HashMap<String, Any>,
    private val statsFragMap: HashMap<String, HashMap<String, Long>>,
    private val movesFragMap: ArrayList<Move>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3 // Number of fragments
    }

    override fun createFragment(position: Int): Fragment
    {
        return when (position) {
            0 -> AboutFrag(aboutFragMap)
            1 -> StatsFrag(statsFragMap)
            else -> MoveFrag(movesFragMap)
        }
    }
}

