package com.example.ehcf.invoices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityInvoiceAmtBinding

class InvoiceAmt : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceAmtBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceAmtBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}