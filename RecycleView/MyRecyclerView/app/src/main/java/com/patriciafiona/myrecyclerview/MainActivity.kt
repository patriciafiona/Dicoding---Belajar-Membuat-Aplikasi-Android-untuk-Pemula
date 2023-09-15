package com.patriciafiona.myrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patriciafiona.myrecyclerview.data.Hero
import com.patriciafiona.myrecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val list = ArrayList<Hero>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            rvHeroes.setHasFixedSize(true)
        }

        list.addAll(listHeroes)
        showRecyclerList()
    }

    private val listHeroes: ArrayList<Hero>
        get() {
            val dataName = resources.getStringArray(R.array.data_name)
            val dataDescription = resources.getStringArray(R.array.data_description)
            val dataPhoto = resources.getStringArray(R.array.data_photo)
            val listHero = ArrayList<Hero>()
            for (i in dataName.indices) {
                val hero = Hero(dataName[i], dataDescription[i], dataPhoto[i])
                listHero.add(hero)
            }
            return listHero
        }

    private fun showSelectedHero(hero: Hero) {
        Toast.makeText(this, "Kamu memilih " + hero.name, Toast.LENGTH_SHORT).show()
    }

    private fun showRecyclerList() {
        with(binding) {
            rvHeroes.layoutManager = LinearLayoutManager(this@MainActivity)
            val listHeroAdapter = ListHeroAdapter(list)
            rvHeroes.adapter = listHeroAdapter

            listHeroAdapter.setOnItemClickCallback(object : ListHeroAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Hero) {
                    showSelectedHero(data)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        with(binding) {
            when (item.itemId) {
                R.id.action_list -> {
                    rvHeroes.layoutManager = LinearLayoutManager(this@MainActivity)
                }

                R.id.action_grid -> {
                    rvHeroes.layoutManager = GridLayoutManager(this@MainActivity, 2)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}