package com.example.ehcf.Diagnostic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityCallForTestBinding

class CallForTest : AppCompatActivity() {
    var context=this@CallForTest
    val binding by lazy {
        ActivityCallForTestBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnBook.setOnClickListener {
                startActivity(Intent(context,TestBooking::class.java))
            }
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}