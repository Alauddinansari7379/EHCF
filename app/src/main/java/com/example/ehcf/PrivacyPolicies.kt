package com.example.ehcf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityPrivacyPoliciesBinding

class PrivacyPolicies : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPoliciesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPoliciesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}