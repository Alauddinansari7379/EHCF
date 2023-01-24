package com.example.ehcf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivitySpecialitiesBinding

class Specialities : AppCompatActivity() {
    private lateinit var binding: ActivitySpecialitiesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecialitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardDermatology.setOnClickListener {
            startActivity(Intent(this, DateForConsultation::class.java))
        }

        binding.cardGenralPhysician.setOnClickListener {
            startActivity(Intent(this, DateForConsultation::class.java))
        }
        binding.cardSexologist.setOnClickListener {
            startActivity(Intent(this, DateForConsultation::class.java))
        }
        binding.cardPsychologist.setOnClickListener {
            startActivity(Intent(this, DateForConsultation::class.java))
        }
        binding.cardOnlineConsultant.setOnClickListener {
            startActivity(Intent(this, FindYourDoctor::class.java))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}