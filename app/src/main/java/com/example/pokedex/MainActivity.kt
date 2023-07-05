package com.example.pokedex

import com.example.pokedex.data_class.Pokemon
import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.pokedex.data_class.EvolutionData
import com.example.pokedex.adapter_class.PokedexAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList: MutableList<Pokemon>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchResult: MutableList<Pokemon>
    private lateinit var searchBar: SearchView
    private val maxIndex = 807

    private val database = Firebase.database

    override  fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        fetchData();
//        deleteData()
        pokemonRecyclerViewSetup()
        searchBarQuery()
        searchBarIconFocusOff()
//        runBlocking {
//            val response = generateChatResponse("What is the weather like today?")
//            if (response != null) {
//                Log.e("RUNTIME<STRRRR>",response)
//            }
//        }
    }

//    suspend fun generateChatResponse(message: String): String? = withContext(Dispatchers.IO) {
//        val url = "https://api.openai.com/v1/chat/completions"
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val apiKey = "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"
//        val model = "gpt-3.5-turbo"
//        val json = """
//        {
//            "model": "$model",
//            "messages": [
//                {"role": "system", "content": "You are ChatGPT"},
//                {"role": "user", "content": "$message"}
//            ]
//        }
//    """.trimIndent()
//
//        val requestBody = json.toRequestBody(mediaType)
//        val httpRequest: Request = Request.Builder()
//            .url(url)
//            .addHeader("Content-Type", "application/json")
//            .addHeader("Authorization", "Bearer $apiKey")
//            .post(requestBody)
//            .build()
//
//        val client = OkHttpClient()
//        try {
//            val response: Response = client.newCall(httpRequest).execute()
//            response.body?.string()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }

    fun extractNumberFromString(string: String): String? {
        val pattern = Regex("\\d+")
        val matchResult = pattern.find(string)
        return matchResult?.value
    }
    private fun searchBarQuery()
    {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val searchable = extractNumberFromString(query)?.toInt()
                if((searchable != null && searchable < maxIndex ) && !searchResult.contains(pokemonList[searchable.toInt()-1]))
                {
                    searchResult.add(pokemonList[searchable.toInt()-1])
                }
//                else
//                {
//                    val foundObj = pokemonList.find { it.name == query }
//                    searchResult.add(foundObj!!)
//                }
                recyclerView.adapter = PokedexAdapter(recyclerView.context,searchResult)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchResult = ArrayList()
                recyclerView.adapter = PokedexAdapter(recyclerView.context,pokemonList)
                return true
            }

        })
        searchBar.suggestionsAdapter = null
    }

    private fun searchBarIconFocusOff() {
        searchBar.setOnClickListener {
            searchBar.isIconified = false // Expand the search view
        }

        searchBar.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchBar.isIconified = false // Expand the search view
            }
        }
    }

    @SuppressLint("ServiceCast")
    override fun onBackPressed() {
        if (!searchBar.isIconified) {
            searchBar.isIconified = true // Collapse the search view
            searchBar.clearFocus() // Clear the focus from the search view
            // Hide the keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchBar.windowToken, 0)
        } else {
            super.onBackPressed() // Perform the default back button behavior
        }
    }
    private fun fetchResourceId(index : Int): Int {
        var drawableName = "p${index}"
        var drawableResourceId = resources.getIdentifier(drawableName, "drawable", packageName)

        if (drawableResourceId == 0) {
            drawableName = "p${index}_f"
            drawableResourceId = resources.getIdentifier(drawableName, "drawable", packageName)

        }
        return drawableResourceId
    }

    private fun pokemonRecyclerViewSetup()
    {
        recyclerView = findViewById(R.id.verticalRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        pokemonList = ArrayList()
        searchResult = ArrayList()
        searchBar = findViewById(R.id.searchBar)
        addData()
        val adapter = PokedexAdapter(this, pokemonList)
        recyclerView.adapter = adapter
        val ai_gif = findViewById<ImageView>(R.id.ai_gif)
        Glide.with(this)
            .load(R.drawable.ai_mode)
            .into(ai_gif)
    }

    private fun test(index: Int, callback: (name: String?, types: List<Any>?) -> Unit) {
        val myRef = database.getReference("$index")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value =
                    dataSnapshot.value as HashMap<*, *>? // Retrieve the value from the snapshot
                val name = value?.get("name") as? String
                val types = value?.get("types") as? List<Any>
                callback(name, types)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    @SuppressLint("DiscouragedApi", "NotifyDataSetChanged")
    private fun addData() {
        val scope = CoroutineScope(Dispatchers.Main)
        val sortedMap = ConcurrentHashMap<Int, Pokemon>()
        val deferred = CompletableDeferred<Unit>()

        val startTime = System.currentTimeMillis()

        for (index in 1..maxIndex) {
            scope.launch {
                val drawableName = "p${index}"
                val drawableResourceId = fetchResourceId(index)
                val drawable = ContextCompat.getDrawable(this@MainActivity, drawableResourceId)

                test(index) { name, types ->
                    val p = Pokemon(
                        drawableName.removePrefix("p").removeSuffix("_f").toInt(),
                        drawable,
                        name,
                        types?.get(0).toString(),
                        types?.getOrNull(1)?.toString() ?: ""
                    )
                    synchronized(sortedMap) {
                        sortedMap[index] = p

                        if (sortedMap.size == maxIndex) {
                            deferred.complete(Unit)
                        }
                    }
                }
            }
        }

        scope.launch {
            deferred.await()

            pokemonList.addAll(sortedMap.values)
            recyclerView.adapter?.notifyDataSetChanged()

            val endTime = System.currentTimeMillis()
            Log.e("RUNTIME_ELAPSED", (endTime - startTime).toString())
        }
    }


//    @SuppressLint("DiscouragedApi")
//    private fun addData() {
//        val maxIndex = 807
//        val scope = CoroutineScope(Dispatchers.Main)
//        val sortedMap: SortedMap<Int, com.example.pokedex.data_class.Pokemon> = Collections.synchronizedSortedMap(TreeMap())
//
//        for (index in 1..maxIndex) {
//            scope.launch {
//                var drawableName = "p${index}"
//                val drawableResourceId = resources.getIdentifier(drawableName, "drawable", packageName)
//
//                if (drawableResourceId == 0) {
//                    drawableName = "p${index}_f"
//                    val drawableResourceId =
//                        resources.getIdentifier(drawableName, "drawable", packageName)
//                    val drawable = ContextCompat.getDrawable(this@MainActivity, drawableResourceId)
//                    test(index) { name, types ->
//                        val p = com.example.pokedex.data_class.Pokemon(
//                            drawableName.removePrefix("p").removeSuffix("_f").toInt(),
//                            drawable,
//                            name,
//                            types?.get(0).toString(),
//                            types?.getOrNull(1)?.toString() ?: ""
//                        )
//                        sortedMap[index] = p
//                        if (sortedMap.size == maxIndex) {
//                            // All items have been fetched, add them to the pokemonList
//                            pokemonList.addAll(sortedMap.values)
//                            recyclerView.adapter?.notifyDataSetChanged()
//                        }
//                    }
//                } else {
//                    val drawable = ContextCompat.getDrawable(this@MainActivity, drawableResourceId)
//                    test(index) { name, types ->
//                        val p = com.example.pokedex.data_class.Pokemon(
//                            index,
//                            drawable,
//                            name,
//                            types?.get(0).toString(),
//                            types?.getOrNull(1)?.toString() ?: ""
//                        )
//                        sortedMap[index] = p
//                        if (sortedMap.size == maxIndex) {
//                            // All items have been fetched, add them to the pokemonList
//                            pokemonList.addAll(sortedMap.values)
//                            recyclerView.adapter?.notifyDataSetChanged()
//                        }
//                    }
//                }
//            }
//        }
//    }


    private fun deleteData() {
        var index = 1000
        while (index != 0) {
            database.getReference(index.toString()).removeValue()
            index--
        }
    }

    private fun hashMapSetUp(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
//        map["id"] = 0;
//        map["base_exp"] = 0
//        map["weight"] = 0
//        map["height"] = 0
//        map["abilities"] = HashMap<String, Boolean>()
//        map["types"] = ArrayList<String>()
//        map["stats"] = HashMap<String, Int>()
//        map["effort"] = HashMap<String, Int>()
//        map["moves"] = HashMap<String, HashMap<String, Any>>()
        map["evolution"] = HashMap<Any,Any>()
        return map

    }
    fun linkPokemonEvolutions(evolutionDataList: List<EvolutionData>, map: HashMap<String, Any>) {
        for (element in evolutionDataList) {
            val pokemonId = element.id

            // Create a new child reference for the com.example.pokedex.data_class.Pokemon ID
            val pokemonChildRef = database.getReference(pokemonId)

            // Create a map for the com.example.pokedex.data_class.Pokemon's data
            val pokemonData = hashMapOf<String, Any>()

            // Add the evolution data for each com.example.pokedex.data_class.Pokemon ID
            for (evolutionData in evolutionDataList) {
                pokemonData[evolutionData.id] = hashMapOf(
                    "name" to evolutionData.name,
                    "isBaby" to evolutionData.isBaby,
                    "minLevel" to evolutionData.minLevel,
                    "trigger" to evolutionData.trigger,
                    "priority" to evolutionData.priority,
                    "item" to evolutionData.item,
                    "gender" to evolutionData.gender,
                    "minHappiness" to evolutionData.minHappiness,
                    "timeOfDay" to evolutionData.timeOfDay,
                    // Add more fields as needed
                )
            }
            map["evolution"] = pokemonData

            // Set the data for the com.example.pokedex.data_class.Pokemon
            pokemonChildRef.updateChildren(map)
        }
    }
    private fun fetchData(){
        val client = AsyncHttpClient()
//        val params = RequestParams()
        val evolutionFlag = true
        val movesFlag = false
        val statsFlag = false
//        params["limit"] = "5"
//        params["page"] = "0"
////        val randomValue = (0..1009).random()
        var index = 0
        while(index != 539) {
            val map = hashMapSetUp()

            client.run {

                if(evolutionFlag){
                get(/* url = */"https://pokeapi.co/api/v2/evolution-chain/$index",
                    object : JsonHttpResponseHandler()
                    {
                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e("ERROR", "SOMETHING WENT WRONG {$index}")
                        }

                        override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
                            var priority = 0
                            val jsonList = json.jsonObject
                            val chain = jsonList.getJSONObject("chain")
                            var tempArr: JSONObject? = chain

                            val pattern = Pattern.compile("/(\\d+)/$")
                            val matcher = pattern.matcher("")
                            val evolutionDataList = mutableListOf<EvolutionData>()

                            while (tempArr != null) {
                                val species = tempArr.getJSONObject("species")
                                val speciesUrl = species.getString("url")
                                matcher.reset(speciesUrl)
                                val speciesId = if (matcher.find()) matcher.group(1) else ""
                                val speciesName = species.getString("name")
                                val isBaby = tempArr.getBoolean("is_baby")
                                val evolutionDetails = tempArr.getJSONArray("evolution_details")

                                if (evolutionDetails.length() > 0) {
                                    val firstEvolution = evolutionDetails.getJSONObject(0)
                                    val itemName = firstEvolution.optJSONObject("item")?.optString("name", "").toString()
                                    val minLevel = firstEvolution.optInt("min_level", 0)
                                    val trigger = firstEvolution.getJSONObject("trigger").getString("name")
                                    val gender = if(firstEvolution.isNull("gender"))
                                    {
                                        ""
                                    }else{
                                        if(firstEvolution.get("gender") == 0)
                                        {
                                            "Male"
                                        }
                                        else
                                        {
                                            "Female"
                                        }
                                    }

                                    val time = firstEvolution.optString("time_of_day").toString()
                                    val minHappiness = firstEvolution.optInt("min_happiness")
                                    val evolutionData = EvolutionData(
                                        id = speciesId,
                                        name = speciesName,
                                        isBaby = isBaby,
                                        minLevel = minLevel,
                                        trigger = trigger,
                                        item = itemName,
                                        priority = priority++,
                                        gender = gender,
                                        minHappiness = minHappiness,
                                        timeOfDay = time
                                    )
                                    evolutionDataList.add(evolutionData)
                                } else {
                                   val evolutionData = EvolutionData(
                                        id = speciesId,
                                        name = speciesName,
                                        isBaby = isBaby,
                                        minLevel = 0,
                                        trigger = "",
                                        item = "",
                                        priority = priority++,
                                        gender = "",
                                        minHappiness = 0,
                                        timeOfDay = "",
                                    )
                                    evolutionDataList.add(evolutionData)
                                }

                                val evolvesTo = tempArr.getJSONArray("evolves_to")

                                if (evolvesTo.length() > 0) {
                                    for (i in 0 until evolvesTo.length()) {
                                        val evolvesToObj = evolvesTo.getJSONObject(i)
                                        // Extract data from evolvesToObj and create EvolutionData instance
                                        // Add the EvolutionData instance to evolutionDataList
                                        val evolvesToSpecies = evolvesToObj.getJSONObject("species")
                                        val evolvesToSpeciesUrl = evolvesToSpecies.getString("url")
                                        matcher.reset(evolvesToSpeciesUrl)
                                        val evolvesToSpeciesId = if (matcher.find()) matcher.group(1) else ""
                                        val evolvesToSpeciesName = evolvesToSpecies.getString("name")
                                        val evolvesToIsBaby = evolvesToObj.getBoolean("is_baby")
                                        val evolvesToEvolutionDetails = evolvesToObj.getJSONArray("evolution_details")


                                        if (evolvesToEvolutionDetails.length() > 0) {
                                            val evolvesToFirstEvolution = evolvesToEvolutionDetails.getJSONObject(0)
                                            val evovles_ItemName = evolvesToFirstEvolution.optJSONObject("item")?.optString("name", "").toString()
                                            val evovles_MinLevel = evolvesToFirstEvolution.optInt("min_level", 0)
                                            val evovles_trigger = evolvesToFirstEvolution.getJSONObject("trigger").getString("name")
                                            val evovels_gender = if(evolvesToFirstEvolution.isNull("gender"))
                                            {
                                                ""
                                            }else{
                                                if(evolvesToFirstEvolution.get("gender") == 0)
                                                {
                                                    "Male"
                                                }
                                                else
                                                {
                                                    "Female"
                                                }
                                            }

                                            val evovles_time = evolvesToFirstEvolution.optString("time_of_day").toString()
                                            val evovles_minHappiness = evolvesToFirstEvolution.optInt("min_happiness")


                                            val evolvesToEvolutionData = EvolutionData(
                                                id = evolvesToSpeciesId,
                                                name = evolvesToSpeciesName,
                                                isBaby = evolvesToIsBaby,
                                                minLevel = evovles_MinLevel,
                                                trigger = evovles_trigger,
                                                item = evovles_ItemName,
                                                priority = priority++,
                                                gender = evovels_gender,
                                                minHappiness = evovles_minHappiness,
                                                timeOfDay = evovles_time,
                                            )
                                            evolutionDataList.add(evolvesToEvolutionData)
                                        } else {
                                            val evolvesToEvolutionData = EvolutionData(
                                                id = evolvesToSpeciesId,
                                                name = evolvesToSpeciesName,
                                                isBaby = evolvesToIsBaby,
                                                minLevel = 0,
                                                trigger = "",
                                                item = "",
                                                priority = priority++,
                                                gender = "",
                                                minHappiness = 0,
                                                timeOfDay = "",
                                            )
                                            evolutionDataList.add(evolvesToEvolutionData)
                                        }
                                    }

                                    tempArr = evolvesTo.getJSONObject(0)
                                } else {
                                    tempArr = null
                                }
                            }

                            linkPokemonEvolutions(evolutionDataList, map)
                        }

                    }
                )
                }

                if(statsFlag)
                {

                        get("https://pokeapi.co/api/v2/pokemon/$index", object : JsonHttpResponseHandler() {
                            override fun onSuccess(
                                statusCode: Int,
                                headers: Headers,
                                json: JsonHttpResponseHandler.JSON
                            ) {

                                var jsonList = json.jsonObject
                                val myRef = database.getReference(jsonList.get("id").toString())
//                                val jsonArr: JSONArray = jsonList.get("abilities") as JSONArray
//                                var abilitiesMap = map["abilities"] as HashMap<String, Boolean>;
//                                for (i in 0 until jsonArr.length()) {
//                                    val abilities = jsonArr.get(i) as JSONObject
//                                    val ability = abilities.get("ability") as JSONObject
//                                    val hidden = abilities.get("is_hidden") as Boolean
//                                    val name = ability.get("name") as String
//                                    abilitiesMap[name] = hidden
//                                }
//
//                                val types = jsonList.get("types") as JSONArray;
//                                var typesArr = map["types"] as ArrayList<String>;
//                                for (i in 0 until types.length()) {
//                                    val typeSlots = types.get(i) as JSONObject
//                                    val type = typeSlots.get("type") as JSONObject
//                                    val typeName = type.get("name") as String
//                                    typesArr.add(typeName)
//                                }
//
//                                val minLevel = if (jsonList.isNull("base_experience")) {
//                                    0 // Default value when "min_level" is null
//                                } else {
//                                    jsonList.get("base_experience")
//                                }
//                                map["base_exp"] = minLevel
//
//                                map["name"] = jsonList.get("name")
//
//                                map["height"] = jsonList.get("height")
//                                map["weight"] = jsonList.get("weight")
//                                val statsArr = jsonList.get("stats") as JSONArray;
//                                val stats: HashMap<String, Int> = map["stats"] as HashMap<String, Int>
//                                val effort: HashMap<String, Int> = map["effort"] as HashMap<String, Int>;
//                                for (i in 0 until statsArr.length()) {
//                                    val genStats = statsArr.get(i) as JSONObject
//                                    val value = genStats.get("base_stat") as Int
//                                    val baseStats = genStats.get("stat") as JSONObject
//                                    val baseStatsName = baseStats.get("name") as String
//                                    stats[baseStatsName] = value
//                                    val effortVal = genStats.get("effort") as Int
//                                    effort[baseStatsName] = effortVal
//                                }
                if(movesFlag)
                        {


                        val moves = jsonList.getJSONArray("moves")
                        val movesMap = map["moves"] as HashMap<String, Any>
                        val totalMoves = moves.length()
                        var completedMoves = 0

                        for (i in 0 until moves.length()) {
                            val move = moves.getJSONObject(i).getJSONObject("move")
                            val moveName = move.getString("name")
                            val versionGroupDetails =
                                moves.getJSONObject(i).getJSONArray("version_group_details")
//                            Log.e("RUNTIME_LENGTH", versionGroupDetails.toString())


                            for (j in 0 until versionGroupDetails.length()) {
                                val moveSetTypes = versionGroupDetails.getJSONObject(j)
                                val levelLearnedAt = moveSetTypes.getInt("level_learned_at")
//                                Log.e("RUNTIME",index.toString())

                                val moveLearnMethod =
                                    moveSetTypes.getJSONObject("move_learn_method")
                                val moveLearnName = moveLearnMethod.getString("name")

                                if (moveLearnName == "level-up" ) {
                                    // Create a new HashMap for moveStatsMap
                                    val moveStatsMap = HashMap<String, Any>()
                                    moveStatsMap["accuracy"] = 0
                                    moveStatsMap["power"] = 0
                                    moveStatsMap["type"] = 0
                                    moveStatsMap["pp"] = 0
                                    moveStatsMap["lvl"] = 0
                                    moveStatsMap["category"] = ""

                                    // Add moveStatsMap to movesMap
                                    movesMap[moveName] = moveStatsMap


                                    // Make the HTTP request
                                    val url = move.getString("url").toString()
                                    get(url, object : JsonHttpResponseHandler() {
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
                                                checkCompletion(
                                                    totalMoves,
                                                    completedMoves,
                                                    movesMap
                                                )
                                            }
                                        }

                                        override fun onSuccess(
                                            statusCode: Int,
                                            headers: Headers?,
                                            json: JsonHttpResponseHandler.JSON
                                        ) {
                                            jsonList = json.jsonObject
                                            var accuracy =
                                                jsonList.get("accuracy")
                                            val dmg_class =
                                                jsonList.get("damage_class") as JSONObject
                                            val dmgType = dmg_class.get("name")
                                            var pp = jsonList.get("pp")
                                            var power = jsonList.get("power")
                                            var category = jsonList.get("type") as JSONObject
                                            var categoryName = category.get("name")
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
                                            moveStatsMap["category"] = categoryName
                                            if(levelLearnedAt != 0 )
                                            {
                                                moveStatsMap["lvl"] = levelLearnedAt
                                            }
                                            myRef.updateChildren(map)

                                            // Update the completion counter
                                            synchronized(this) {
                                                completedMoves++
                                                checkCompletion(
                                                    totalMoves,
                                                    completedMoves,
                                                    movesMap
                                                )
                                            }
                                        }
                                    })
                                }
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

            }

            index++;

            Log.e("RUNTIME_ID",index.toString())
        }
    }

    fun checkCompletion(totalMoves: Int, completedMoves: Int, movesMap: HashMap<String, Any>) {
        if (completedMoves == totalMoves) {

            // All requests are completed, you can now access the movesMap safely
        }
    }

}
