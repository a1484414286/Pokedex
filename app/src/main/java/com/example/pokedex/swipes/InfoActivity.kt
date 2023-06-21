package com.example.pokedex.swipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

@Suppress("UNCHECKED_CAST")
class InfoActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var pageAdapter : PageAdapter

    private lateinit var id : String
    private lateinit var name : String
    private lateinit var type1 : String
    private lateinit var type2 : String

    private val database = Firebase.database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.status_page)
        tabsContentSwitch()
        receiveDataFromPreviousActivity()
    }



    private fun loadDataFromDB(index: Int, callback: (abilities: Map<String,Boolean>, base_exp : Int, effort : Map<String,Long>, moves : Map<String,Map<String, Any>>, stats : Map<String,Long>,
    height : Int, weight : Int)-> Unit) {
        val myRef = database.getReference("$index")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value =
                    dataSnapshot.value as HashMap<*, *>? // Retrieve the value from the snapshot
                val abilities = value?.get("abilities") as? Map<String,Boolean>
                val base_exp = value!!["base_exp"].toString().toInt()
                val effort = value["effort"] as? Map<String, Long>
                val moves = value["moves"] as Map<String, Map<String, Any>>
                val stats = value["stats"] as Map<String, Long>
                val height = (value["height"] as Long).toInt()
                val weight = (value["weight"] as Long).toInt()
                callback(
                    abilities!!, base_exp,
                    effort!! , moves, stats, height, weight
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
    @SuppressLint("SetTextI18n")
    private fun receiveDataFromPreviousActivity()
    {
        id = intent.getStringExtra("id")!!
        name = intent.getStringExtra("name")!!
        type1 = intent.getStringExtra("type1")!!
        type2 = intent.getStringExtra("type2") ?: ""
        id = id.replace("§","").trim()
        val imageView = findViewById<ImageView>(R.id.pokedexAvatar)
        findViewById<TextView>(R.id.pokedexID).text = "§  ${this.id}"
        findViewById<TextView>(R.id.pokedexPokemonName).text = name
        val drawableName = "p${id.toInt()}"
        val drawableResourceId = resources.getIdentifier(drawableName, "drawable", packageName)
        Glide.with(this)
            .load(drawableResourceId)
            .into(imageView)
        loadDataFromDB(id.toInt()) { abilities, _, effort, moves, stats, height, weight->
            // update ui based on data called back from fetch
            abilities.let { abilitiesList ->
                for(ability in abilitiesList) {
                    val name = ability
                    //load into ui
                }
            }

            effort.let { effortMap ->
                for(key in effortMap.keys) {
                    val name = key
                    val value = effortMap[key]?.toInt()
                }
            }

            moves.let { movesMap ->
                for(key in movesMap.keys) {
                    val name = key
                    val move = movesMap[key]
                    val accuracy = move?.get("accuracy")
                    val lvlReq = move?.get("lvl")
                    val power = move?.get("power")
                    val pp = move?.get("pp")
                    val type = move?.get("type")
                }
            }

            stats.let { statsMap ->
                for(key in statsMap.keys) {
                    val name = key
                    val value = statsMap[key]
                }
            }


            // Use other retrieved values (baseExp, effort, moves, stats) as needed
        }
    }



    private fun tabsContentSwitch()
    {

        val button = findViewById<ImageButton>(R.id.genderSwitch)
        button.setImageDrawable(getDrawable(R.drawable.gender_switch))
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        pageAdapter = PageAdapter(supportFragmentManager,lifecycle)
        viewPager.adapter = pageAdapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Called when a tab is unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Called when a tab is reselected (tab is already selected)
            }
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}