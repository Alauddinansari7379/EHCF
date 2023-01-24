package com.example.ehcf.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityReportBinding
import com.example.ehcf.databinding.ActivityReportViewBinding

class Report : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}