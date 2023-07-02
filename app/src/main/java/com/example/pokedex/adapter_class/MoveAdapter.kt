package com.example.pokedex.adapter_class

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.data_class.Move

class MoveAdapter(private val moveList: ArrayList<Move>, private val typesChart: HashMap<String, Int>) : RecyclerView.Adapter<MoveAdapter.ViewHolder>(){
    private lateinit var categoryChart : HashMap<String, Int>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_move, parent, false)
        categoryMapSetup()
        return ViewHolder(view)
    }

    private fun categoryMapSetup()
    {
        categoryChart = HashMap()
        categoryChart["physical"] = R.drawable.move_physical
        categoryChart["special"] = R.drawable.move_special
        categoryChart["status"] = R.drawable.move_status
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val move = moveList[position]
        holder.nameTextView.text = move.name
        Glide
            .with(holder.itemView.context)
            .load(typesChart[move.type])
            .into(holder.typeTextView)

        Glide
            .with(holder.itemView.context)
            .load(categoryChart[move.category])
            .into(holder.categoryTextView)

        holder.levelTextView.text = move.lvl.toString()
        holder.ppTextView.text = move.pp.toString()
        holder.powerTextView.text = move.power.toString()
        holder.accuracyTextView.text = move.accuracy.toString()

    }

    override fun getItemCount(): Int {
        return moveList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.moveNameFill)
        val levelTextView : TextView = itemView.findViewById(R.id.levelFill)
        val typeTextView: ImageView = itemView.findViewById(R.id.typeFill)
        val categoryTextView : ImageView = itemView.findViewById(R.id.categoryFill)
        val ppTextView : TextView = itemView.findViewById(R.id.ppFill)
        val powerTextView : TextView = itemView.findViewById(R.id.powerFill)
        val accuracyTextView : TextView = itemView.findViewById(R.id.accuracyFill)
    }
}