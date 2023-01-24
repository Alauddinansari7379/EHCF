package com.example.ehcf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityFindYourDoctorBinding

class FindYourDoctor : AppCompatActivity() {
    private lateinit var binding: ActivityFindYourDoctorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindYourDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBook.setOnClickListener {
            startActivity(Intent(this, PaymentMode::class.java))

        }
        binding.btnBook1.setOnClickListener {
            startActivity(Intent(this, PaymentMode::class.java))

        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}