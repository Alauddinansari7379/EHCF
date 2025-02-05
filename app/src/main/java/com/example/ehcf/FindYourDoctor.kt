package com.example.ehcf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityFindYourDoctorBinding
import com.giphy.sdk.analytics.GiphyPingbacks.context
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class FindYourDoctor : AppCompatActivity() {
    private lateinit var binding: ActivityFindYourDoctorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindYourDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBook.setOnClickListener {
            startActivity(Intent(this, PaymentMode::class.java))

        }
        binding.btnBook1.setOnClickListener {
            startActivity(Intent(this, PaymentMode::class.java))

        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onStart() {
        super.onStart()
        CheckInternet().check { connected ->
            if (connected) {

                // myToast(requireActivity(),"Connected")
            }
            else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }

}