package com.example.ehcf.FAQ

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityFaqcategoriesBinding

class FAQCategories : AppCompatActivity() {
    private lateinit var binding: ActivityFaqcategoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFaqcategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}