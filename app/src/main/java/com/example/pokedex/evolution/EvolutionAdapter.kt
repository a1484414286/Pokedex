package com.example.pokedex.evolution

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R

class EvolutionAdapter(private val pokemonList: List<PokeEvo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Create a ViewHolder class
    inner class ViewHolderBegin(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Get references to the views in the item layout
        private val imageView: ImageView = itemView.findViewById(R.id.evolutionImage)

        @SuppressLint("SetTextI18n")
        fun bind(pokemon: PokeEvo) {
            // Bind the data to the views
            Glide.with(itemView)
                .load(pokemon.imageId)
                .into(this.imageView)
        }
    }

    inner class ViewHolderEnd(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        private val levelTextView: TextView = itemView.findViewById(R.id.evolveReq)
        private val imageView: ImageView = itemView.findViewById(R.id.evolutionImage)

        fun bind(pokemon: PokeEvo)
        {
            levelTextView.text = "Lvl ${pokemon.level}"
            Glide.with(itemView)
                .load(pokemon.imageId)
                .into(this.imageView)
        }
    }

    private companion object {
        const val VIEW_TYPE_BEGIN = 0
        const val VIEW_TYPE_END = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_BEGIN -> {
                val view = inflater.inflate(R.layout.evolution_pokemon_end, parent, false)
                ViewHolderBegin(view)
            }
            VIEW_TYPE_END -> {
                val view = inflater.inflate(R.layout.evolution_pokemon_begin, parent, false)
                ViewHolderEnd(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        when (holder) {
            is ViewHolderBegin -> holder.bind(pokemon)
            is ViewHolderEnd -> holder.bind(pokemon)
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Determine the view type based on the position
        return if (position == 0) VIEW_TYPE_BEGIN else VIEW_TYPE_END
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

}
