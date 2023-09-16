package com.patriciafiona.marioworld.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.patriciafiona.marioworld.R
import com.patriciafiona.marioworld.data.entities.Character
import com.patriciafiona.marioworld.data.resource.DataSource
import com.patriciafiona.marioworld.databinding.ActivityMainBinding
import com.patriciafiona.marioworld.ui.main.adapter.ListCharactersAdapter
import com.patriciafiona.marioworld.ui.main.adapter.ListNewsAdapter
import com.patriciafiona.marioworld.utils.MediaPlayerManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPreferences

    private lateinit var bgSoundManager: MediaPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bgSoundManager = MediaPlayerManager(applicationContext)

        sharedPrefs = getSharedPreferences("com.patriciafiona.marioworld", MODE_PRIVATE)
        setSoundStatus()

        initView()
    }

    private fun initView(){
        with(binding){
            btnSound.setOnClickListener {
                setSoundStatus(isChangeStatus = true)
            }

            rvNews.setHasFixedSize(true)
            showNewsRecyclerList()

            rvCharacters.setHasFixedSize(true)
            showCharactersRecyclerList()
        }
    }

    private fun setSoundStatus(isChangeStatus: Boolean = false){
        var currentStatus = sharedPrefs.getBoolean("isMute", false)
        if(isChangeStatus) {
            sharedPrefs.edit()
                .putBoolean("isMute", !currentStatus)
                .apply()
        }

        currentStatus = sharedPrefs.getBoolean("isMute", false)
        if(currentStatus){
            binding.btnSound.setImageResource(R.drawable.ic_volume_off)
        }else{
            binding.btnSound.setImageResource(R.drawable.ic_volume_up)
        }

        isPlayBgSound()
    }

    private fun isPlayBgSound(){
        val isMute = sharedPrefs.getBoolean("isMute", false)

        if(!isMute) {
            //set bg sound
            bgSoundManager.startSound(R.raw.bgm_overworld, true)
        }else{
            bgSoundManager.stopSound()
        }
    }

    private fun showNewsRecyclerList() {
        with(binding) {
            rvNews.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            val listNewsAdapter = ListNewsAdapter(DataSource.news())
            rvNews.adapter = listNewsAdapter

            listNewsAdapter.setOnItemClickCallback(object : ListNewsAdapter.OnItemClickCallback {
                override fun onItemClicked(link: String) {
                    //Open link in the browser
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(browserIntent)
                }
            })
        }
    }

    private fun showCharactersRecyclerList() {
        with(binding) {
            rvCharacters.layoutManager = LinearLayoutManager(this@MainActivity)
            val listCharactersAdapter = ListCharactersAdapter(DataSource.characters())
            rvCharacters.adapter = listCharactersAdapter

            listCharactersAdapter.setOnItemClickCallback(object : ListCharactersAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Character) {
                    runBlocking {
                        //play click sound
                        bgSoundManager.stopSound()

                        val mpManager = MediaPlayerManager(applicationContext)
                        mpManager.startSound(R.raw.continue_sound, false)

                        delay(100)

                        //Go to Detail Page
                    }
                }
            })
        }
    }
}