package com.patriciafiona.marioworld.ui.character_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.patriciafiona.marioworld.R
import com.patriciafiona.marioworld.databinding.ActivityDetailBinding
import com.patriciafiona.marioworld.databinding.ActivityProfileBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}