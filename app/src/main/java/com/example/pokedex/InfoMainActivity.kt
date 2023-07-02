package com.example.pokedex


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.pokedex.adapter_class.PageAdapter
import com.example.pokedex.data_class.Ability
import com.example.pokedex.data_class.Move
import com.example.pokedex.data_class.PokeEvo
import com.example.pokedex.data_class.TypeIcons
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pl.droidsonroids.gif.GifDrawable
import java.util.concurrent.ScheduledExecutorService


@Suppress("UNCHECKED_CAST")
class InfoMainActivity() : AppCompatActivity() {
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var pageAdapter : PageAdapter
    private lateinit var types : HashMap<String, Int>

    private lateinit var genderImage : HashMap<String,Int>
    private lateinit var evolutionList : ArrayList<PokeEvo>
    private lateinit var abilitiesList : ArrayList<Ability>
    private lateinit var aboutStats : HashMap<String,Any>

    private lateinit var aboutFragMap : HashMap<String, Any>
    private lateinit var statsFragMap : HashMap<String, HashMap<String,Long>>
    private lateinit var movesFragMap : ArrayList<Move>

    private lateinit var id : String
    private lateinit var name : String
    private lateinit var type1 : String
    private lateinit var type2 : String


    private lateinit var imageView : ImageView
    private lateinit var idView : TextView
    private lateinit var nameView : TextView
    private lateinit var typeView : ImageView
    private lateinit var typeView2 : ImageView
    private lateinit var genderView : ImageView
    private lateinit var gifDrawable : GifDrawable
    private val database = Firebase.database
    private var flag: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.status_page)
        init()
        viewSet()
        receiveDataFromPreviousActivity()
        tabsContentSwitch()
    }

    private fun init()
    {
        evolutionList = ArrayList()
        abilitiesList = ArrayList()
        aboutStats = HashMap()
        aboutFragMap = HashMap()
        statsFragMap = HashMap()
        movesFragMap = ArrayList()
        genderImage = HashMap()
        types = TypeIcons().typeTable()

        id = intent.getStringExtra("id")!!
        id = id.replace("§","").trim()
        name = intent.getStringExtra("name")!!
        type1 = intent.getStringExtra("type1")!!
        type2 = intent.getStringExtra("type2") ?: ""


        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        pageAdapter = PageAdapter(supportFragmentManager,lifecycle,aboutFragMap,statsFragMap,movesFragMap)
        viewPager.adapter = pageAdapter
        genderView = findViewById(R.id.genderSwitch)
        imageView = findViewById(R.id.pokedexAvatar)
        idView = findViewById(R.id.pokedexID)
        nameView = findViewById(R.id.pokedexPokemonName)
        typeView = findViewById(R.id.type1)
        typeView2 = findViewById(R.id.type2)!!

    }

    private fun viewSet()
    {
        val stringBuilder = StringBuilder()
        stringBuilder.append("§ ").append(id)
        val convertedString = stringBuilder.toString()
        idView.text = convertedString
        nameView.text = name
        gifDrawable = GifDrawable(resources, R.drawable.gender_switch)

        Glide.with(this)
            .load(gifDrawable)
            .into(genderView)
        genderObserver()
        typeView.setImageDrawable(getDrawable(type1))

        if(type2 != "")
        {
            typeView2.setImageDrawable(getDrawable(type2))
        }
        else
        {
            typeView2.visibility = View.GONE
        }
    }

    private fun genderObserver()
    {
        gifDrawable.setSpeed(1.5f)

        val viewTreeObserver = genderView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                genderView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (gifDrawable.isRunning) {
                    gifDrawable.pause()
                }
                if(gifDrawable.currentFrameIndex > 100)
                {
                    gifDrawable.reset()
                }

            }
        })

        genderView.setOnClickListener {
            gifDrawable.start()
            Handler().postDelayed({
                gifDrawable.pause()
                if(flag)
                {
                    if(genderImage["male"] != null)
                    {
                        Glide.with(this)
                            .load(genderImage["male"])
                            .into(imageView)
                    }
                    else
                    {
                        val resourceID = fetchResourceIDMale(id)
                        Glide.with(this)
                            .load(resourceID)
                            .into(imageView)
                        genderImage["male"] = resourceID
                    }

                }
                else
                {
                    Glide.with(this)
                        .load(genderImage["female"])
                        .into(imageView)
                }
            },1300)
            flag = !flag
        }
    }

    private fun getDrawable(type : String): Drawable? {
        val resourceId = resources.getIdentifier(type, "drawable", packageName)
        val drawable = ContextCompat.getDrawable(this, resourceId)
        return drawable
    }
    private fun loadDataFromDB(
        index: Int,
        callback: (
            abilities: Map<String, Boolean>, base_exp: Int, effort: Map<String, Long>, moves: Map<String, Map<String, Any>>, stats: Map<String, Long>,
            height: Int, weight: Int, evolutionMap: Any,
        ) -> Unit,
    ) {
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
                val evolutionMap : Any
                evolutionMap = if(index < 4) {
                    value["evolution"] as ArrayList<Any>

                } else {
                    value["evolution"] as HashMap<Int,Any>
                }

                callback(
                    abilities!!, base_exp,
                    effort!! , moves, stats, height, weight, evolutionMap
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    @SuppressLint("DiscouragedApi")
    private fun fetchResourceID(drawableName : String): Int {
        var drawableResourceId = resources.getIdentifier("p$drawableName", "drawable", packageName)
        if (drawableResourceId == 0) {
            val name = "p${drawableName}_f"
            drawableResourceId =
                resources.getIdentifier(name, "drawable", packageName)
        }
        return drawableResourceId
    }
    private fun fetchResourceIDMale(drawableName : String): Int {
        var drawableResourceId = resources.getIdentifier("p$drawableName", "drawable", packageName)
        if (drawableResourceId == 0) {
            val name = "p${drawableName}_m"
            drawableResourceId =
                resources.getIdentifier(name, "drawable", packageName)
        }
        return drawableResourceId
    }
    @SuppressLint("SetTextI18n")
    private fun receiveDataFromPreviousActivity()
    {
        val drawableResourceId= fetchResourceID(id)
        genderImage["female"] = drawableResourceId;
        Glide.with(this)
            .load(drawableResourceId)
            .into(imageView)

        loadDataFromDB(id.toInt()) { abilities, XP, effort, moves, stats, height, weight, evolutionMap->
            // update ui based on data called back from fetch

            aboutStats["base_exp"] = XP
            aboutStats["height"] = height
            aboutStats["weight"] = weight

            statsFragMap["base"] = stats as java.util.HashMap<String, Long>
            statsFragMap["effort"] = effort as java.util.HashMap<String, Long>


            abilities.let { list ->
                for(ability in list.keys) {
                    val name = ability
                    val bool = list[ability] as Boolean
                    abilitiesList.add(Ability(name,bool))
                }
            }

            moves.let { movesMap ->
                for(key in movesMap.keys) {
                    val name = key
                    val move = movesMap[key]
                    val accuracy = move?.get("accuracy") as Long
                    val lvlReq = move["lvl"] as Long
                    val power = move["power"] as Long
                    val pp = move["pp"] as Long
                    val type = move["type"] as String
                    val category = move["category"] as String
                    movesFragMap.add(Move(lvlReq,name,category,type,power, accuracy,pp))
                }
            }

            if(evolutionMap is HashMap<*,*>)
            {
                for(key in evolutionMap.keys)
                {
                    val detailedMap = evolutionMap[key] as HashMap<*,*>
                    evolutionList.add(
                        PokeEvo(
                        fetchResourceID(key.toString()), detailedMap["minLevel"] as Long,
                    detailedMap["trigger"] as String, detailedMap["priority"] as Long
                    )
                    )
                }
            }

            else if(evolutionMap is ArrayList<*>)
            {
                for(i in 1 until evolutionMap.size)
                {
                    val detailedMap = evolutionMap[i] as HashMap<*,*>
                    evolutionList.add( PokeEvo(fetchResourceID(i.toString()), detailedMap["minLevel"] as Long,
                    detailedMap["trigger"] as String, detailedMap["priority"] as Long
                )
                    )
                }

            }

            aboutFragMap["evolution"] = evolutionList
            aboutFragMap["abilities"] = abilitiesList
            aboutFragMap["stats"] = aboutStats
        }
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    private fun tabsContentSwitch()
    {



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