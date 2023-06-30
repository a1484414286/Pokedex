package com.example.pokedex.adapter_class

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.data_class.PokeEvo

class EvolutionAdapter(private var pokemonList: MutableList<PokeEvo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Create a ViewHolder class
    class ViewHolderBegin(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Get references to the views in the item layout
        private val imageView: ImageView = itemView.findViewById(R.id.evolutionImage)
        fun bind(pokemon: PokeEvo) {
            // Bind the data to the views
            Glide.with(itemView)
                .load(pokemon.imageId)
                .into(imageView)
        }
    }

    class ViewHolderEnd(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
//        private val levelTextView: TextView = itemView.findViewById(R.id.evolveReq)
        private val imageView: ImageView = itemView.findViewById(R.id.evolutionImage)
        private val lvlReq : TextView = itemView.findViewById(R.id.evolveReq)
        fun bind(pokemon: PokeEvo)
        {
            lvlReq.text = pokemon.level.toString()
            lvlReq.gravity = Gravity.CENTER
            Glide.with(itemView)
                .load(pokemon.imageId)
                .into(imageView)
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
                val view = inflater.inflate(R.layout.evolution_pokemon_begin, parent, false)
                ViewHolderBegin(view)
            }
            VIEW_TYPE_END -> {
                val view = inflater.inflate(R.layout.evolution_pokemon_end, parent, false)
                ViewHolderEnd(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        when (holder) {
            is ViewHolderBegin -> pokemon.let(holder::bind)
            is ViewHolderEnd -> pokemon.let(holder::bind)
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Determine the view type based on the position
        return if(pokemonList.size == 1) {
            VIEW_TYPE_BEGIN
        } else {
            if(position == 0 ) VIEW_TYPE_BEGIN else VIEW_TYPE_END
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

}
