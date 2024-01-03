package com.example.ehcf.Medicine

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityProductListingBinding

class ProductListing : AppCompatActivity() {
    private val context=this@ProductListing
    val binding by lazy {
        ActivityProductListingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding){
            disPrice.paintFlags = binding.disPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            cardMedicine.setOnClickListener {
                startActivity(Intent(context,ProductDetail::class.java))
            }
        }
    }
}