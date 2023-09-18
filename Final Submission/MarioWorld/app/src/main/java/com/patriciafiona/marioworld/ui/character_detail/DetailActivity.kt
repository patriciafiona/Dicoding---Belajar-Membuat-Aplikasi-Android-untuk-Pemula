package com.patriciafiona.marioworld.ui.character_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.patriciafiona.marioworld.R
import com.patriciafiona.marioworld.data.entities.Character
import com.patriciafiona.marioworld.databinding.ActivityDetailBinding
import com.patriciafiona.marioworld.utils.MediaPlayerManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var bgSoundManager: MediaPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences("com.patriciafiona.marioworld", MODE_PRIVATE)
        bgSoundManager = MediaPlayerManager(applicationContext)
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        with(binding){
            val data: Character? = intent.extras!!.getParcelable("character")
            if(data != null) {
                val color = Color.rgb(
                    data.backgroundColor[0],
                    data.backgroundColor[1],
                    data.backgroundColor[2]
                )

                btnBack.setOnClickListener {
                    onBackPressed()
                }

                btnSound.setOnClickListener {
                    setSoundStatus(isChangeStatus = true)
                }

                actionShare.setOnClickListener {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                    i.putExtra(Intent.EXTRA_TEXT, "https://mario.nintendo.com/characters/")
                    startActivity(Intent.createChooser(i, "Share Official Website URL"))
                }

                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = color

                ivPhoto.setImageResource(data.imageFull ?: data.imageOpen)

                val characterSoundManager = MediaPlayerManager(applicationContext)
                btnSound1.setColor(color)
                btnSound1.setOnClickListener {
                    characterSoundManager.startSound(data.characterSound[0])
                }

                btnSound2.setColor(color)
                btnSound2.setOnClickListener {
                    characterSoundManager.startSound(data.characterSound[1])
                }

                btnSound3.setColor(color)
                btnSound3.setOnClickListener {
                    characterSoundManager.startSound(data.characterSound[2])
                }

                accelerationRating.rating = (data.ability.acceleration / 2.0).toFloat()
                maxSpeedRating.rating = (data.ability.maxSpeed / 2.0).toFloat()
                techniqueRating.rating = (data.ability.technique / 2.0).toFloat()
                powerRating.rating = (data.ability.power / 2.0).toFloat()
                staminaRating.rating = (data.ability.stamina / 2.0).toFloat()

                val gd = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
                        color,
                        Color.BLACK
                    )
                )
                gd.cornerRadius = 0f
                container.background = gd

                tvName.text = data.name
                tvFullNameAndSpecies.text = "${data.fullName} - ${data.species}"
                tvDescription.text = data.description
                tvDialog.text = data.dialog
            }
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
            bgSoundManager.startSound(R.raw.bgm_super_mario_bos, true)
        }else{
            bgSoundManager.stopSound()
        }
    }

    override fun onResume() {
        super.onResume()

        initView()

        runBlocking {
            delay(1500)

            setSoundStatus()
        }
    }

    override fun onPause() {
        super.onPause()

        bgSoundManager.stopSound()
    }
}