package com.example.pokedex.data_class

import com.example.pokedex.R

class TypeIcons {
    private val typesTable: HashMap<String, Int> = HashMap<String, Int>().apply {
        R.drawable.normal.let { put("normal", it) }
        R.drawable.fire.let { put("fire", it) }
        R.drawable.flying.let { put("flying", it) }
        R.drawable.psychic.let { put("psychic", it) }
        R.drawable.water.let { put("water", it) }
        R.drawable.bug.let { put("bug", it) }
        R.drawable.grass.let { put("grass", it) }
        R.drawable.rock.let { put("rock", it) }
        R.drawable.electric.let { put("electric", it) }
        R.drawable.ghost.let { put("ghost", it) }
        R.drawable.ice.let { put("ice", it) }
        R.drawable.dark.let { put("dark", it) }
        R.drawable.fighting.let { put("fighting", it) }
        R.drawable.dragon.let { put("dragon", it) }
        R.drawable.poison.let { put("poison", it) }
        R.drawable.steel.let { put("steel", it) }
        R.drawable.ground.let { put("ground", it) }
        R.drawable.fairy.let { put("fairy", it) }
    }

    private val cateTable: HashMap<String, Int> = HashMap<String, Int>().apply {
        R.drawable.normal.let { put("status", it) }
        R.drawable.fire.let { put("physical", it) }
        R.drawable.flying.let { put("magical", it) }
    }

    fun typeTable(): HashMap<String, Int> {
        return typesTable
    }
    fun cateTable(): HashMap<String, Int> {
        return typesTable
    }

}