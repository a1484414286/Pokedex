package com.example.pokedex.swipes

import Pokemon
import android.graphics.drawable.Drawable
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pokedex.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InfoActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager2;
    private lateinit var tabLayout: TabLayout;
    private lateinit var pageAdapter : PageAdapter;

    private lateinit var id : String;
    private lateinit var sprite : String;
    private lateinit var type1 : String;
    private lateinit var type2 : String;

    private lateinit var database : FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.status_page)
        tabsContentSwitch()
        receiveDataFromPreviousActivity()
    }



    private fun loadDataFromDB(index: Int, callback: (abilities: List<Any>?, base_exp : Int, effort : Map<String,Integer>, moves : Map<String,Map<String, Integer>>, stats : Map<String,Integer>)-> Unit) {
        val myRef = database.getReference("$index")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value =
                    dataSnapshot.value as HashMap<Any, Any>? // Retrieve the value from the snapshot
                val abilities = value?.get("abilities") as? List<Any>
                val base_exp = value?.get("base_exp") as? Int
                val effort = value?.get("effort") as? Map<String,Integer>
                val moves = value?.get("moves") as Map<String,Map<String,Integer>>
                val stats = value?.get("stats") as Map<String, Integer>
                callback(abilities,base_exp!!, effort!!, moves,stats)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
    private fun receiveDataFromPreviousActivity()
    {
        id = intent.getStringExtra("id")!!
        sprite = intent.getStringExtra("sprite")!!
        type1 = intent.getStringExtra("type1")!!
        type2 = intent.getStringExtra("type2")!!

        loadDataFromDB(id.toInt()) { abilities, baseExp, effort, moves, stats ->
            // update ui based on data called back from fetch

            abilities?.let {
                abilitiesList ->
                for(ability in abilitiesList)
                {
                    if(ability != null)
                    {
                        //load into ui
                    }
                }
            }

            effort?.let {
                effortMap ->
                for(keys in effortMap.keys)
                {
                    val value = effortMap[keys]
                }
            }

            moves?.let {
                movesMap ->
                for(key in movesMap.keys)
                {
                    val move = movesMap[key]
                    val accuracy = move?.get("accuracy")
                    val lvlReq = move?.get("lvl")
                    val power = move?.get("power")
                    val pp = move?.get("pp")
                    val type = move?.get("type")
                }
            }

            stats?.let {
                statsMap ->
                for(key in statsMap.keys)
                {
                    if(key != null)
                    {
                        val value = statsMap.get(key)
                    }
                }
            }

            // Use other retrieved values (baseExp, effort, moves, stats) as needed
        }
        Toast.makeText(this, "$id $sprite",Toast.LENGTH_SHORT).show()
    }

    private fun tabsContentSwitch()
    {

        var m = findViewById<ImageView>(R.id.pokemonAvatar)
        m.setImageDrawable(getDrawable(R.drawable.p9))
        var button = findViewById<ImageButton>(R.id.genderSwitch)
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