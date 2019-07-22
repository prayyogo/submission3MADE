package com.dicoding.prayogo.moviecatalogue

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import com.dicoding.prayogo.moviecatalogue.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        var LANGUAGE_FILM:String?=null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewpager_main.adapter = PagerAdapter(supportFragmentManager,this)
        LANGUAGE_FILM=resources.getString(R.string.language)
        supportActionBar?.elevation= 0F
        tabs_main.setupWithViewPager(viewpager_main)
        tabs_main.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        supportActionBar?.title = resources.getString(R.string.movies)
                    }
                    1 -> {
                        supportActionBar?.title = resources.getString(R.string.tv_shows)
                    }
                }
            }
        })

        supportActionBar?.title = resources.getString(R.string.movies)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
