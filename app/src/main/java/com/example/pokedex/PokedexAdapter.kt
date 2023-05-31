package com.example.pokedex


import Pokemon
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import java.io.ByteArrayOutputStream


class PokedexAdapter (private val context: Context, private val pokemonList : List<Pokemon>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var hashTable : HashMap<String, Int> = HashMap<String, Int>().apply {
        R.drawable.normal?.let { put("Normal", it) };
        R.drawable.fire?.let{put("Fire",it)}
        R.drawable.flying?.let { put("Flying", it) };
        R.drawable.psychic?.let { put("Psychic", it) };
        R.drawable.water?.let { put("Water", it) };
        R.drawable.bug?.let { put("Bug", it) };
        R.drawable.grass?.let { put("Grass", it) };
        R.drawable.rock?.let { put("Rock", it) };
        R.drawable.electric?.let { put("Electric", it) };
        R.drawable.ghost?.let { put("Ghost", it) };
        R.drawable.ice?.let { put("Ice", it) };
        R.drawable.dark?.let { put("Dark", it) };
        R.drawable.fighting?.let { put("Fighting", it) };
        R.drawable.dragon?.let { put("Dragon", it) };
        R.drawable.poison?.let { put("Poison", it) };
        R.drawable.steel?.let { put("Steel", it) };
        R.drawable.ground?.let { put("Ground", it) };
        R.drawable.fairy?.let { put("Fairy", it) };
//        resizeImageToFit(context, R.drawable.normal, 125, 50)?.let { put("Normal", it) }
//        resizeImageToFit(context, R.drawable.flying, 125, 50)?.let { put("Flying", it) }
//        resizeImageToFit(context, R.drawable.fire, 125, 50)?.let { put("Fire", it) }
//        resizeImageToFit(context, R.drawable.psychic, 130, 50)?.let { put("Psychic", it) }
//        resizeImageToFit(context, R.drawable.water, 130, 50)?.let { put("Water", it) }
//        resizeImageToFit(context, R.drawable.bug, 130, 50)?.let { put("Bug", it) }
//        resizeImageToFit(context, R.drawable.grass, 80, 40)?.let { put("Grass", it) }
//        resizeImageToFit(context, R.drawable.rock, 80, 40)?.let { put("Rock", it) }
//        resizeImageToFit(context, R.drawable.electric, 80, 40)?.let { put("Electric", it) }
//        resizeImageToFit(context, R.drawable.ghost, 80, 40)?.let { put("Ghost", it) }
//        resizeImageToFit(context, R.drawable.ice, 80, 40)?.let { put("Ice", it) }
//        resizeImageToFit(context, R.drawable.dark, 80, 40)?.let { put("Dark", it) }
//        resizeImageToFit(context, R.drawable.fighting, 80, 40)?.let { put("Fighting", it) }
//        resizeImageToFit(context, R.drawable.dragon, 80, 40)?.let { put("Dragon", it) }
//        resizeImageToFit(context, R.drawable.poison, 80, 40)?.let { put("Poison", it) }
//        resizeImageToFit(context, R.drawable.steel, 80, 40)?.let { put("Steel", it) }
//        resizeImageToFit(context, R.drawable.ground, 80, 40)?.let { put("Ground", it) }
//        resizeImageToFit(context, R.drawable.fairy, 80, 40)?.let { put("Fairy", it) }
    };



    companion object{
        private val ITEM_TYPE_ONE = 1;
        private val ITEM_TYPE_TWO = 2;

    }

    class ViewHolderOne(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView
        internal val animated_sprites: ImageView
        val type1 : ImageButton

        init {
            // Find our RecyclerView item's ImageView for future use
            name = view.findViewById(R.id.pokemonName)
            animated_sprites = view.findViewById(R.id.pokemonImage)
            type1 = view.findViewById(R.id.type1)
        }
    }


    class ViewHolderTwo(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView
        val animated_sprites: ImageView
        val type1 : ImageButton
        val type2 : ImageButton

        init {
            // Find our RecyclerView item's ImageView for future use
            name = view.findViewById(R.id.pokemonName)
            animated_sprites = view.findViewById(R.id.pokemonImage)
            type1 = view.findViewById(R.id.type1)
            type2 = view.findViewById(R.id.type2)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_ONE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.single_type_pokemon, parent, false)
                ViewHolderOne(view)
            }
            ITEM_TYPE_TWO -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.duo_type_pokemon, parent, false)
                ViewHolderTwo(view)
            }
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType)
        {
            ITEM_TYPE_ONE -> {
                val viewHolderOne = holder as ViewHolderOne
                viewHolderOne.name.text = pokemonList[position].name;
//                viewHolderOne.type1.setImageDrawable(hashTable.get(pokemonList[position].type1)
//                    ?.let { resizeImageToFit(context, it,120,50) })

                Glide.with(viewHolderOne.itemView)
                    .load(context.getDrawable(R.drawable.ani_bw_006))
                    .into(viewHolderOne.animated_sprites)
                Log.d("success","success")
            }



                ITEM_TYPE_TWO -> {
//                val viewHolder = holder as ViewHolderTwo
//                viewHolder.name.text = pokemonList[position].name;
//                viewHolder.type1.setImageDrawable(hashTable[pokemonList[position].type1])
//                viewHolder.type2.setImageDrawable(hashTable[pokemonList[position].type2])

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val pokemon = pokemonList[position]

        return if (pokemon.type2.isNotEmpty()) {
            ITEM_TYPE_TWO
        } else {
            ITEM_TYPE_ONE
        }
    }

    fun resizeImageToFit(context: Context, imageResId: Int, targetWidth: Int, targetHeight: Int): BitmapDrawable? {
        try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeResource(context.resources, imageResId, this)
                inSampleSize = calculateInSampleSize(this, targetWidth, targetHeight)
                inJustDecodeBounds = false
            }
            val bitmap = BitmapFactory.decodeResource(context.resources, imageResId, options)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
            return BitmapDrawable(context.resources, scaledBitmap)
        } catch (e: Exception) {
            Log.e("ImageResize", "Error resizing image: ${e.message}")
            return null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, targetWidth: Int, targetHeight: Int): Int {
        val width = options.outWidth
        val height = options.outHeight
        var inSampleSize = 1

        if (height > targetHeight || width > targetWidth) {
            val halfWidth = width / 2
            val halfHeight = height / 2
            while ((halfHeight / inSampleSize) >= targetHeight && (halfWidth / inSampleSize) >= targetWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    override fun getItemCount(): Int {
       return pokemonList.size;
    }
}