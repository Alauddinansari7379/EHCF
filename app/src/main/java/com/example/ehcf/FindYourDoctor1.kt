package com.example.ehcf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityFindYourDoctor1Binding

class FindYourDoctor1 : AppCompatActivity() {
    private lateinit var binding: ActivityFindYourDoctor1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindYourDoctor1Binding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnConsultNow.setOnClickListener {
            startActivity(Intent(this, PaymentMode::class.java))
        }
        binding.btnConsultNow1.setOnClickListener {
            startActivity(Intent(this, PaymentMode::class.java))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

}