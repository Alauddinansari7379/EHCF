package com.example.ehcf.Diagnostic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityDiagnosticBinding

class Diagnostic : AppCompatActivity() {
    private val binding by lazy {
        ActivityDiagnosticBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }

            cardBook.setOnClickListener {
                startActivity(Intent(this@Diagnostic,TestList::class.java))
            }
        }
    }
}