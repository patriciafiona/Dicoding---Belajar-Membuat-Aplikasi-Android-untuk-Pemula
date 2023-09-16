package com.patriciafiona.marioworld.ui.onboarding

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ncorti.slidetoact.SlideToActView
import com.patriciafiona.marioworld.R
import com.patriciafiona.marioworld.databinding.ActivitySplashBinding
import com.patriciafiona.marioworld.ui.main.MainActivity
import com.patriciafiona.marioworld.utils.MediaPlayerManager
import kotlinx.coroutines.runBlocking


class OnBoardingActivity : AppCompatActivity(), SlideToActView.OnSlideToActAnimationEventListener {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPrefs: SharedPreferences

    private lateinit var bgSoundManager: MediaPlayerManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bgSoundManager = MediaPlayerManager(applicationContext)

        sharedPrefs = getSharedPreferences("com.patriciafiona.marioworld", MODE_PRIVATE)
        if(!sharedPrefs.contains("isMute")){
            //default sound is not mute
            val ed = sharedPrefs.edit()
            ed.putBoolean("isMute", false)
            ed.apply()
        }
        setSoundStatus()

        initView()
    }

    private fun initView(){
        with(binding){
            btnSlider.onSlideToActAnimationEventListener = this@OnBoardingActivity

            btnSound.setOnClickListener {
                setSoundStatus(isChangeStatus = true)
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
            bgSoundManager.startSound(R.raw.bgm_opening, true)
        }else{
            bgSoundManager.stopSound()
        }
    }

    override fun onSlideCompleteAnimationEnded(view: SlideToActView) {
        val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSlideCompleteAnimationStarted(view: SlideToActView, threshold: Float) {
        runBlocking {
            bgSoundManager.stopSound()

            val mpManager = MediaPlayerManager(applicationContext)
            mpManager.startSound(R.raw.continue_sound, false)
        }
    }

    override fun onSlideResetAnimationEnded(view: SlideToActView) { }

    override fun onSlideResetAnimationStarted(view: SlideToActView) { }
}