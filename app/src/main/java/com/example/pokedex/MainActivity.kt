package com.example.pokedex

import Pokemon
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList: MutableList<Pokemon>
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<String>
    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
//        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.status_page)
        var m = findViewById<ImageView>(R.id.pokemonAvatar)
        m.setImageDrawable(getDrawable(R.drawable.p9))
        var button = findViewById<ImageButton>(R.id.genderSwitch)
        button.setImageDrawable(getDrawable(R.drawable.gender_switch))
//        recyclerView = findViewById(R.id.verticalRecyclerView)
//        recyclerView.layoutManager = GridLayoutManager(this, 3)
//        pokemonList = ArrayList()
//        addData()
//        val adapter = PokedexAdapter(this, pokemonList)
//        recyclerView.adapter = adapter
//        fetchData();
//        deleteData()
    }

    private fun test(index: Int, callback: (name: String?, types: List<Any>?) -> Unit) {
        val myRef = database.getReference("$index")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value =
                    dataSnapshot.value as HashMap<Any, Any>? // Retrieve the value from the snapshot
                val name = value?.get("name") as? String
                val types = value?.get("types") as? List<Any>
                callback(name, types)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun addData() {
        val maxIndex = 807
        val scope = CoroutineScope(Dispatchers.Main)
        val sortedMap: SortedMap<Int, Pokemon> = Collections.synchronizedSortedMap(TreeMap())

        for (index in 1..maxIndex) {
            scope.launch {
                var drawableName = "p${index}"
                val drawableResourceId = resources.getIdentifier(drawableName, "drawable", packageName)

                if (drawableResourceId == 0) {
                    drawableName = "p${index}_f"
                    val drawableResourceId =
                        resources.getIdentifier(drawableName, "drawable", packageName)
                    val drawable = ContextCompat.getDrawable(this@MainActivity, drawableResourceId)
                    test(index) { name, types ->
                        val p = Pokemon(
                            drawableName.removePrefix("p").removeSuffix("_f").toInt(),
                            drawable,
                            name,
                            types?.get(0).toString(),
                            types?.getOrNull(1)?.toString() ?: ""
                        )
                        sortedMap[index] = p
                        if (sortedMap.size == maxIndex) {
                            // All items have been fetched, add them to the pokemonList
                            pokemonList.addAll(sortedMap.values)
                            recyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                } else {
                    val drawable = ContextCompat.getDrawable(this@MainActivity, drawableResourceId)
                    test(index) { name, types ->
                        val p = Pokemon(
                            index,
                            drawable,
                            name,
                            types?.get(0).toString(),
                            types?.getOrNull(1)?.toString() ?: ""
                        )
                        sortedMap[index] = p
                        if (sortedMap.size == maxIndex) {
                            // All items have been fetched, add them to the pokemonList
                            pokemonList.addAll(sortedMap.values)
                            recyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }


    private fun deleteData() {
        var index = 1000;
        while (index != 0) {
            database.getReference(index.toString()).removeValue()
            index--;
        }
    }

    private fun hashMapSetUp(): HashMap<String, Any> {
        var map = HashMap<String, Any>();
        map["id"] = 0;
        map["base_exp"] = 0
        map["abilities"] = HashMap<String, Boolean>();
        map["types"] = ArrayList<String>();
        map["stats"] = HashMap<String, Int>();
        map["moves"] = HashMap<Any, Any>();
        map["effort"] = HashMap<String, Int>();
        map["moves"] = HashMap<String, HashMap<String, Any>>();
        return map;
    }

    private fun fetchData() {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["limit"] = "5"
        params["page"] = "0"
        var index = 899;

        while (index != 900) {
            val map = hashMapSetUp();

            client.run {

                get(
                    "https://pokeapi.co/api/v2/pokemon/$index",
                    object : JsonHttpResponseHandler() {
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Headers,
                            json: JsonHttpResponseHandler.JSON
                        ) {


                            val jsonList = json.jsonObject
                            val myRef = database.getReference(jsonList.get("id").toString())
                            val jsonArr: JSONArray = jsonList.get("abilities") as JSONArray
                            map["abilities"] = HashMap<String, Boolean>()
                            var abilitiesMap = map["abilities"] as HashMap<String, Boolean>;
                            for (i in 0 until jsonArr.length()) {
                                val abilities = jsonArr.get(i) as JSONObject
                                val ability = abilities.get("ability") as JSONObject
                                val hidden = abilities.get("is_hidden") as Boolean
                                val name = ability.get("name") as String
                                abilitiesMap[name] = hidden
                            }


                            val types = jsonList.get("types") as JSONArray;
                            map["types"] = ArrayList<String>()
                            var typesArr = map["types"] as ArrayList<String>;
                            for (i in 0 until types.length()) {
                                val typeSlots = types.get(i) as JSONObject
                                val type = typeSlots.get("type") as JSONObject
                                val typeName = type.get("name") as String
                                typesArr.add(typeName)
                            }

                            map["base_exp"] = jsonList.get("base_experience")
                            map["name"] = jsonList.get("name")
                            Log.e("name", jsonList.get("name").toString())

                            val statsArr = jsonList.get("stats") as JSONArray;
                            map["stats"] = HashMap<String, Int>()
                            map["effort"] = HashMap<String, Int>();
                            val stats: HashMap<String, Int> =
                                map["stats"] as HashMap<String, Int>
                            val effort: HashMap<String, Int> =
                                map["effort"] as HashMap<String, Int>;
                            for (i in 0 until statsArr.length()) {
                                val genStats = statsArr.get(i) as JSONObject
                                val value = genStats.get("base_stat") as Int
                                val baseStats = genStats.get("stat") as JSONObject
                                val baseStatsName = baseStats.get("name") as String

                                stats[baseStatsName] = value
                                val effortVal = genStats.get("effort") as Int

                                effort[baseStatsName] = effortVal
                            }


                            val moves = jsonList.getJSONArray("moves")
                            map["moves"] = HashMap<String, Any>()
                            val movesMap = map["moves"] as HashMap<String, Any>

                            for (i in 0 until moves.length()) {
                                val move = moves.getJSONObject(i).getJSONObject("move")
                                val moveName = move.getString("name")
                                val versionGroupDetails =
                                    moves.getJSONObject(i)
                                        .getJSONArray("version_group_details")

                                for (j in 0 until versionGroupDetails.length()) {
                                    val moveSetTypes = versionGroupDetails.getJSONObject(j)
                                    val moveLearnMethod =
                                        moveSetTypes.getJSONObject("move_learn_method")
                                    val moveLearnName = moveLearnMethod.getString("name")

                                    if (moveLearnName == "level-up") {
                                        // Create a new HashMap for moveStatsMap
                                        val moveStatsMap = HashMap<String, Any>()
                                        moveStatsMap["accuracy"] = 0
                                        moveStatsMap["power"] = 0
                                        moveStatsMap["type"] = 0
                                        moveStatsMap["pp"] = 0
                                        moveStatsMap["lvl"] =
                                            moveSetTypes.get("level_learned_at") as Int
                                        // Add moveStatsMap to movesMap
                                        movesMap[moveName] = moveStatsMap

                                        // Make the HTTP request
                                        get(
                                            move.getString("url"),
                                            object : JsonHttpResponseHandler() {
                                                override fun onFailure(
                                                    statusCode: Int,
                                                    headers: Headers?,
                                                    response: String?,
                                                    throwable: Throwable?
                                                ) {

                                                }

                                                override fun onSuccess(
                                                    statusCode: Int,
                                                    headers: Headers?,
                                                    json: JsonHttpResponseHandler.JSON
                                                ) {
                                                    val jsonList = json.jsonObject
                                                    try {
                                                        var accuracy =
                                                            jsonList.get("accuracy")
                                                        val dmg_class =
                                                            jsonList.get("damage_class") as JSONObject
                                                        val dmgType = dmg_class.get("name")
                                                        var pp = jsonList.get("pp")
                                                        var power = jsonList.get("power")
                                                        // Update moveStatsMap
                                                        if (accuracy is JSONObject || accuracy.equals(
                                                                null
                                                            )
                                                        ) {
                                                            accuracy = 100
                                                        }
                                                        if (power is JSONObject || power.equals(
                                                                null
                                                            )
                                                        ) {
                                                            power = 0;
                                                        }
                                                        if (pp is JSONObject || pp.equals(
                                                                null
                                                            )
                                                        ) {
                                                            pp = 0;
                                                        }
                                                        moveStatsMap["accuracy"] = accuracy
                                                        moveStatsMap["type"] = dmgType
                                                        moveStatsMap["pp"] = pp
                                                        moveStatsMap["power"] = power
                                                        myRef.setValue(map)
                                                    } catch (e: com.google.firebase.database.DatabaseException) {
                                                        var accuracy =
                                                            jsonList.get("accuracy")
                                                        val dmg_class =
                                                            jsonList.get("damage_class") as JSONObject
                                                        val dmgType = dmg_class.get("name")
                                                        var pp = jsonList.get("pp")
                                                        var power = jsonList.get("power")
                                                        Log.e(
                                                            "accuracy",
                                                            (accuracy is JSONObject).toString()
                                                        )
                                                        Log.e(
                                                            "dmgType",
                                                            (dmgType.equals(null)).toString()
                                                        )
                                                        Log.e(
                                                            "pp",
                                                            (pp.equals(null)).toString()
                                                        )
                                                        Log.e(
                                                            "power",
                                                            (power.equals(null)).toString()
                                                        )
                                                    }

                                                }


                                            })
                                    }
                                }
                            }
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            errorResponse: String,
                            throwable: Throwable?
                        ) {
                            Log.d("ID not found Error", index.toString())
                        }
                    })
            }

            index++;
        }

    }
}

