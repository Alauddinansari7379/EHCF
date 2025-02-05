package com.example.ehcf.Diagnostic

import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.databinding.ActivityTestBookingBinding
import com.example.ehcf.sharedpreferences.SessionManager


class TestBooking : AppCompatActivity() {
    private var context=this@TestBooking
    val binding by lazy {
        ActivityTestBookingBinding.inflate(layoutInflater)
    }
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding){
            read.paintFlags = read.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }

    }
}