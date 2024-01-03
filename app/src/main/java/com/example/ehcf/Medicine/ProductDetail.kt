package com.example.ehcf.Medicine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityProductDetialBinding

class ProductDetail : AppCompatActivity() {
    val binding by lazy {
        ActivityProductDetialBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){

        }
    }
}