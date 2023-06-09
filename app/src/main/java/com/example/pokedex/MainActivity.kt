package com.example.pokedex

import Pokemon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList: MutableList<Pokemon>
    private lateinit var recyclerView: RecyclerView
    private lateinit var list : ArrayList<String>
    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.verticalRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        pokemonList = ArrayList()
        val adapter = PokedexAdapter(this, pokemonList)
        recyclerView.adapter = adapter
        val map = fetchData()
        Log.e("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",map.toString())

    }


    private fun hashMapSetUp(): HashMap<String, Any> {
        var map = HashMap<String, Any>();
        map["id"] = 0;
        map["base_exp"] = 0
        map["abilities"] = HashMap<String,Boolean>();
        map["types"] = ArrayList<String>();
        map["stats"] = HashMap<String,Int>();
        map["moves"] = HashMap<Any,Any>();
        map["effort"] = HashMap<String,Int>();
        map["moves"] = HashMap<String,HashMap<String,Any>>();
        return map;
    }

    private fun fetchData(): HashMap<String, Any> {
        val client = AsyncHttpClient()
//        val params = RequestParams()
        var map = hashMapSetUp();
//        params["limit"] = "5"
//        params["page"] = "0"
        val randomValue = (0..1009).random()

        client.run {

//            get(/* url = */"https://pokeapi.co/api/v2/evolution-chain/1",
//                object : JsonHttpResponseHandler()
//                {
//                    override fun onFailure(
//                        statusCode: Int,
//                        headers: Headers?,
//                        response: String?,
//                        throwable: Throwable?
//                    ) {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
//                        val jsonList = json.jsonObject;
//                        for(i in jsonList.keys())
//                        {
//                            Log.e("aaa", i)
//                        }
//                        val chain : JSONObject = jsonList.get("chain") as JSONObject
//                        val evolvesTo : JSONArray = chain.get("evolves_to") as JSONArray
//                        val evolutionDetails = evolvesTo.get(0) as JSONObject
//                        Log.e("AA", evolutionDetails.get("species").toString())
//                        Log.e("AA", evolvesTo.toString())
//
//                    }
//
//                }
//            )

            get("https://pokeapi.co/api/v2/pokemon/1", object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                    val jsonList = json.jsonObject
                    val myRef = database.getReference(jsonList.get("id").toString())
                    val arr = ArrayList<String>()
                    //moves, species

                    val jsonArr: JSONArray = jsonList.get("abilities") as JSONArray
                    var abilitiesMap = map["abilities"] as HashMap<String, Boolean>;
                    for (i in 0 until jsonArr.length()) {
                        val abilities = jsonArr.get(i) as JSONObject
                        val ability = abilities.get("ability") as JSONObject
                        val hidden = abilities.get("is_hidden") as Boolean
                        val name = ability.get("name") as String
                        abilitiesMap[name] = hidden
                    }

                    val types = jsonList.get("types") as JSONArray;
                    var typesArr = map["types"] as ArrayList<String>;
                    for (i in 0 until types.length()) {
                        val typeSlots = types.get(i) as JSONObject
                        val type = typeSlots.get("type") as JSONObject
                        val typeName = type.get("name") as String
                        typesArr.add(typeName)
                    }

                    map["base_exp"] = jsonList.get("base_experience")
                    map["name"] = jsonList.get("name")

                    val statsArr = jsonList.get("stats") as JSONArray;
                    val stats: HashMap<String, Int> = map["stats"] as HashMap<String, Int>
                    val effort: HashMap<String, Int> = map["effort"] as HashMap<String, Int>;
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
                    val movesMap = map["moves"] as HashMap<String, Any>
                    val totalMoves = moves.length()
                    var completedMoves = 0

                    for (i in 0 until moves.length()) {
                        val move = moves.getJSONObject(i).getJSONObject("move")
                        val moveName = move.getString("name")
                        val versionGroupDetails = moves.getJSONObject(i).getJSONArray("version_group_details")

                        for (j in 0 until versionGroupDetails.length()) {
                            val moveSetTypes = versionGroupDetails.getJSONObject(j)
                            val moveLearnMethod = moveSetTypes.getJSONObject("move_learn_method")
                            val moveLearnName = moveLearnMethod.getString("name")

                            if (moveLearnName == "level-up") {
                                // Create a new HashMap for moveStatsMap
                                val moveStatsMap = HashMap<String, Any>()
                                moveStatsMap["accuracy"] = 0
                                moveStatsMap["power"] = 0
                                moveStatsMap["type"] = 0
                                moveStatsMap["pp"] = 0
                                moveStatsMap["lvl"] = 0

                                // Add moveStatsMap to movesMap
                                movesMap[moveName] = moveStatsMap

                                // Make the HTTP request
                                val url = move.getString("url").toString()
                                get(move.getString("url"), object : JsonHttpResponseHandler() {
                                    override fun onFailure(
                                        statusCode: Int,
                                        headers: Headers?,
                                        response: String?,
                                        throwable: Throwable?
                                    ) {
                                        // Handle the failure

                                        // Update the completion counter
                                        synchronized(this) {
                                            completedMoves++
                                            checkCompletion(totalMoves, completedMoves,movesMap)
                                        }
                                    }

                                    override fun onSuccess(
                                        statusCode: Int,
                                        headers: Headers?,
                                        json: JsonHttpResponseHandler.JSON
                                    ) {
                                        val jsonList = json.jsonObject
                                        val accuracy = jsonList.get("accuracy")

                                        // Update moveStatsMap
                                        moveStatsMap["accuracy"] = accuracy

                                        // Update the completion counter
                                        synchronized(this) {
                                            completedMoves++
                                            checkCompletion(totalMoves, completedMoves,movesMap)
                                        }
                                    }
                                })
                            }
                        }

                        Log.e("PONISSAN",movesMap.toString())
//                    myRef.setValue(map)
                    }
                }

                override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                    Log.d("ID not found Error", randomValue.toString())
                }
            })

        }


        return map;
    }

    fun checkCompletion(totalMoves: Int, completedMoves: Int, movesMap: HashMap<String, Any>) {
        if (completedMoves == totalMoves) {
            // All requests are completed, you can now access the movesMap safely
            Log.e("BBB", movesMap.toString())
        }
    }

}
