package com.example.pokedex.adapter_class

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.data_class.Move

class MoveAdapter(private val moveList: Map<Int, Move>) : RecyclerView.Adapter<MoveAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_move, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val move = moveList[position]
        holder.nameTextView.text = move!!.name
        holder.typeTextView.text = move.type
        holder.levelTextView.text = move.lvl.toString()
        holder.categoryTextView.text = move.category
        holder.ppTextView.text = move.pp.toString()
        holder.powerTextView.text = move.power.toString()
        holder.accuracyTextView.text = move.accuracy.toString()

    }

    override fun getItemCount(): Int {
        return moveList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.moveNameCol)
        val levelTextView : TextView = itemView.findViewById(R.id.levelCol)
        val typeTextView: TextView = itemView.findViewById(R.id.typeCol)
        val categoryTextView : TextView = itemView.findViewById(R.id.categoryCol)
        val ppTextView : TextView = itemView.findViewById(R.id.ppCol)
        val powerTextView : TextView = itemView.findViewById(R.id.powerCol)
        val accuracyTextView : TextView = itemView.findViewById(R.id.accuracyCol)
    }
}