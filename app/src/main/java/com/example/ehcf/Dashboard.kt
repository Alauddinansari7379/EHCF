package com.example.ehcf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityDashboard2Binding

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboard2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboard2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnBookAppointment.setOnClickListener {
            startActivity(Intent(this, FindYourDoctor::class.java))
        }
    }
}