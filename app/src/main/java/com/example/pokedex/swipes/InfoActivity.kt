package com.example.pokedex.swipes

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pokedex.R
import com.google.android.material.tabs.TabLayout

class InfoActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager2;
    private lateinit var tabLayout: TabLayout;
    private lateinit var pageAdapter : PageAdapter;
    private lateinit var id : String;
    private lateinit var sprite : String;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveData()
        setContentView(R.layout.status_page)
        var m = findViewById<ImageView>(R.id.pokemonAvatar)
        m.setImageDrawable(getDrawable(R.drawable.p9))
        var button = findViewById<ImageButton>(R.id.genderSwitch)
        button.setImageDrawable(getDrawable(R.drawable.gender_switch))
        tabsContentSwitch()

    }


    private fun receiveData()
    {
        id = intent.getStringExtra("id")!!
        sprite = intent.getStringExtra("sprite")!!
        Toast.makeText(this, "$id $sprite",Toast.LENGTH_SHORT).show()
    }

    private fun tabsContentSwitch()
    {

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