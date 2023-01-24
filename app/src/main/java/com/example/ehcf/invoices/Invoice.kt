package com.example.ehcf.invoices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityInvoiceBinding
import com.example.ehcf.databinding.ActivityReportBinding

class Invoice : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutView.setOnClickListener {
            startActivity(Intent(this, InvoiceAmt::class.java))
        }
    }
}