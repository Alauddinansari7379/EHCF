package com.example.ehcf.Medicine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityBrowseMedicineBinding
import com.example.ehcf.sharedpreferences.SessionManager

class BrowseMedicine : AppCompatActivity() {
    var context=this@BrowseMedicine
    val binding by lazy {
        ActivityBrowseMedicineBinding.inflate(layoutInflater)
    }
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(context)

        with(binding){
            layoutProduct.setOnClickListener {
                startActivity(Intent(context,ProductListing::class.java))
            }


        }
    }
}