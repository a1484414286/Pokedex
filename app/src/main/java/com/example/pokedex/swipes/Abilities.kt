package com.example.pokedex.swipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokedex.R

/**
 * A simple [Fragment] subclass.
 * Use the [Abilities.newInstance] factory method to
 * create an instance of this fragment.
 */
class Abilities : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abilities, container, false)
    }

}