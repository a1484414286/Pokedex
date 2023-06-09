package com.example.pokedex.adapter_class


import com.example.pokedex.data_class.Pokemon
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Handler
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
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.InfoMainActivity
import com.example.pokedex.R
import com.example.pokedex.data_class.TypeIcons


class PokedexAdapter(private val context: Context, private val pokemonList: List<Pokemon>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val hashTable = TypeIcons().typeTable()
    companion object {
        private const val ITEM_TYPE_ONE = 1
        private const val ITEM_TYPE_TWO = 2

    }

    @SuppressLint("ResourceType")
    class ViewHolderOne(view: View, private val context : Context) : RecyclerView.ViewHolder(view) {
        val id: TextView
        val name: TextView
        val animated_sprites: ImageView
        val type1: ImageButton
        var type1Name : String = ""
        init {
            // Find our RecyclerView item's ImageView for future use
            id = view.findViewById(R.id.Id)
            name = view.findViewById(R.id.pokemonName)
            animated_sprites = view.findViewById(R.id.pokemonImage)
            type1 = view.findViewById(R.id.type1)

            view.setOnClickListener {
                if (view.isClickable) {
                    view.isClickable = false
                }
                Handler().postDelayed({ view.isClickable = true }, 1000)

                val intent = Intent(view.context, InfoMainActivity::class.java)
                intent.putExtra("id", id.text)
                intent.putExtra("name", name.text)
                intent.putExtra("type1", type1Name)

                val transitionOptions = ActivityOptionsCompat.makeCustomAnimation(
                    context as Activity,
                    R.transition.custom_transition,
                    R.transition.custom_transition
                ).toBundle()

                view.context.startActivity(intent,transitionOptions)
            }
        }
    }


    class ViewHolderTwo(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        val id: TextView
        val name: TextView
        val animated_sprites: ImageView
        val type1: ImageButton
        val type2: ImageButton
        var type1Name : String = ""
        var type2Name : String = ""

        init {
            id = view.findViewById(R.id.Id)
            // Find our RecyclerView item's ImageView for future use
            name = view.findViewById(R.id.pokemonName)
            animated_sprites = view.findViewById(R.id.pokemonImage)
            type1 = view.findViewById(R.id.type1)
            type2 = view.findViewById(R.id.type2)


            view.setOnClickListener {
                if(view.isClickable)
                {
                    view.isClickable = false
                }
                Handler().postDelayed({view.isClickable=true},1000)
                val intent = Intent(view.context, InfoMainActivity::class.java)
                intent.putExtra("id", id.text)
                intent.putExtra("name", name.text)
                intent.putExtra("type1", type1Name)
                intent.putExtra("type2", type2Name)
                val transitionOptions = ActivityOptionsCompat.makeCustomAnimation(
                    context as Activity,
                    R.transition.custom_transition,
                    R.transition.custom_transition
                ).toBundle()

                view.context.startActivity(intent,transitionOptions)
            }
        }

    }


    private fun adjustRelativeLayoutSize(view: View, viewType: Int) {
        val displayMetrics = DisplayMetrics()
        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val itemWidth = (screenWidth / 3) - (2* 20) // Adjust the left and right margin as needed
        val itemHeight = (screenHeight * 0.3).toInt() // Adjust the percentage as needed
        if (viewType == ITEM_TYPE_ONE) {
            val relativeLayout = view.findViewById<RelativeLayout>(R.id.singleItemLayout)
            val layoutParams = RelativeLayout.LayoutParams(itemWidth, itemHeight)
            relativeLayout.layoutParams = layoutParams
        } else {
            val relativeLayout = view.findViewById<RelativeLayout>(R.id.doubleItemLayout)
            val layoutParams = RelativeLayout.LayoutParams(itemWidth, itemHeight)
            relativeLayout.layoutParams = layoutParams
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            ITEM_TYPE_ONE -> inflater.inflate(R.layout.single_type_pokemon, parent, false)
            ITEM_TYPE_TWO -> inflater.inflate(R.layout.duo_type_pokemon, parent, false)
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
        adjustRelativeLayoutSize(view, viewType)
        return when (viewType) {
            ITEM_TYPE_ONE -> ViewHolderOne(view,context)
            ITEM_TYPE_TWO -> ViewHolderTwo(view,context)
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {

            ITEM_TYPE_ONE -> {
                val viewHolderOne = holder as ViewHolderOne
                viewHolderOne.id.text = "§ " + pokemonList[position].id.toString()
                viewHolderOne.name.text = pokemonList[position].name
                viewHolderOne.type1Name = pokemonList[position].type1
                viewHolderOne.type1.setImageDrawable(
                    hashTable[pokemonList[position].type1]

                        ?.let { resizeImageToFit(context, it, 120, 50) })
                Glide.with(viewHolderOne.itemView)
                    .load(pokemonList[position].imageSource)
                    .into(viewHolderOne.animated_sprites)
            }

            ITEM_TYPE_TWO -> {
                val viewHolderTwo = holder as ViewHolderTwo
                viewHolderTwo.id.text = "§ " + pokemonList[position].id.toString()
                viewHolderTwo.name.text = pokemonList[position].name
                viewHolderTwo.type1Name = pokemonList[position].type1
                viewHolderTwo.type2Name = pokemonList[position].type2
                viewHolderTwo.type1.setImageDrawable(hashTable[pokemonList[position].type1]?.let {
                    resizeImageToFit(
                        context,
                        it,
                        130,
                        50
                    )
                })
                viewHolderTwo.type2.setImageDrawable(hashTable[pokemonList[position].type2]?.let {
                    resizeImageToFit(
                        context,
                        it,
                        120,
                        50
                    )
                })
                Glide.with(viewHolderTwo.itemView)
                    .load(pokemonList[position].imageSource)
                    .into(viewHolderTwo.animated_sprites)

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

    private fun resizeImageToFit(
        context: Context,
        imageResId: Int,
        targetWidth: Int,
        targetHeight: Int,
    ): Drawable? {
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

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        targetWidth: Int,
        targetHeight: Int,
    ): Int {
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
        return pokemonList.size
    }
}