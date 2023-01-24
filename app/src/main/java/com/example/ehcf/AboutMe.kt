package com.example.ehcf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.Fragment.MainActivity
import com.example.ehcf.databinding.ActivityAboutMeBinding

class AboutMe : AppCompatActivity() {
    private lateinit var binding: ActivityAboutMeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutMeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNext.setOnClickListener {
            ///startActivity(Intent(this, MainActivity::class.java))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}