package com.example.pokedex


import Pokemon
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class PokedexAdapter (private val context: Context, private val pokemonList : List<Pokemon>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val hashTable : HashMap<String, Int> = HashMap<String, Int>().apply {
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
    };
    private var count = 0;


    companion object{
        private val ITEM_TYPE_ONE = 1;
        private val ITEM_TYPE_TWO = 2;

    }

    class ViewHolderOne(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView
        val animated_sprites: ImageView
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

    private fun adjustRelativeLayoutSize(view: View) {
        val displayMetrics = DisplayMetrics()
        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val itemWidth = (screenWidth / 3) - (2 * 5) // Adjust the left and right margin as needed
        val itemHeight = (screenHeight * 0.4).toInt() // Adjust the percentage as needed
        val relativeLayout = view.findViewById<RelativeLayout>(R.id.singleItemLayout)
        val layoutParams = RelativeLayout.LayoutParams(itemWidth, itemHeight)
        relativeLayout.layoutParams = layoutParams
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = when (viewType) {
            ITEM_TYPE_ONE -> inflater.inflate(R.layout.single_type_pokemon, parent, false)
            ITEM_TYPE_TWO -> inflater.inflate(R.layout.duo_type_pokemon, parent, false)
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
        adjustRelativeLayoutSize(view)
        return when (viewType) {
            ITEM_TYPE_ONE -> ViewHolderOne(view)
            ITEM_TYPE_TWO -> ViewHolderTwo(view)
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType)
        {

            ITEM_TYPE_ONE -> {
                val viewHolderOne = holder as ViewHolderOne
                adjustRelativeLayoutSize(viewHolderOne.itemView)
                viewHolderOne.name.text = pokemonList[position].name;
                viewHolderOne.type1.setImageDrawable(
                    hashTable[pokemonList[position].type1]

                    ?.let { resizeImageToFit(context, it,120,50) })
                Glide.with(viewHolderOne.itemView)

                    .load( context.getDrawable(pokemonList[position].imageSource))
                    .into(viewHolderOne.animated_sprites)
            }

            ITEM_TYPE_TWO -> {
                val viewHolderTwo = holder as ViewHolderTwo
                viewHolderTwo.name.text = pokemonList[position].name
                viewHolderTwo.type1.setImageDrawable(hashTable[pokemonList[position].type1]?.let { resizeImageToFit(context, it, 120, 50) })
                viewHolderTwo.type2.setImageDrawable(hashTable[pokemonList[position].type2]?.let { resizeImageToFit(context, it, 120, 50) })
                Glide.with(viewHolderTwo.itemView)
                    .load(context.getDrawable(pokemonList[position].imageSource))
                    .into(viewHolderTwo.animated_sprites)
                adjustRelativeLayoutSize(viewHolderTwo.itemView)
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

    private fun resizeImageToFit(context: Context, imageResId: Int, targetWidth: Int, targetHeight: Int): BitmapDrawable? {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeResource(context.resources, imageResId, this)
                inSampleSize = calculateInSampleSize(this, targetWidth, targetHeight)
                inJustDecodeBounds = false
            }
            val bitmap = BitmapFactory.decodeResource(context.resources, imageResId, options)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
            BitmapDrawable(context.resources, scaledBitmap)
        } catch (e: Exception) {
            Log.e("ImageResize", "Error resizing image: ${e.message}")
            null
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