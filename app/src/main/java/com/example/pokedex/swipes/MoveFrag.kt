package com.example.pokedex.swipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.adapter_class.MoveAdapter
import com.example.pokedex.data_class.Move

/**
 * A simple [Fragment] subclass.
 * Use the [MoveFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoveFrag(var movesFragMap: ArrayList<Move>) : Fragment() {
    private lateinit var moveAdapter : MoveAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_move, container, false)
        recyclerView = rootView.findViewById(R.id.moveRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        movesFragMap.sortBy { it.lvl }
        moveAdapter = MoveAdapter(movesFragMap)
        recyclerView.adapter = moveAdapter

        // Inflate the layout for this fragment
        return rootView
    }

}