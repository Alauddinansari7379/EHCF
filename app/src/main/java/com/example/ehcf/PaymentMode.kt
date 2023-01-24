package com.example.ehcf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.Testing.RazorPay
import com.example.ehcf.databinding.ActivityPaymentModeBinding

class PaymentMode : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.cardRazorPay.setOnClickListener {
            startActivity(Intent(this, RazorPay::class.java))
        }

    }
}