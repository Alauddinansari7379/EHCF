package com.example.ehcf

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.Testing.RazorPay
import com.example.ehcf.databinding.ActivityPaymentModeBinding
import com.giphy.sdk.analytics.GiphyPingbacks.context

class PaymentMode : AppCompatActivity() {
    private val context: Context = this@PaymentMode
    private var doctorId=""
    private lateinit var binding: ActivityPaymentModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        doctorId = intent.getStringExtra("doctorId").toString()

        binding.cardRazorPay.setOnClickListener {
            val intent = Intent(context as Activity, RazorPay::class.java)
                .putExtra("doctorId",doctorId)
            context.startActivity(intent)
        }

    }
}