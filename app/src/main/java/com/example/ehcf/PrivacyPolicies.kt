package com.example.ehcf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityPrivacyPoliciesBinding
import com.giphy.sdk.analytics.GiphyPingbacks.context
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class PrivacyPolicies : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPoliciesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPoliciesBinding.inflate(layoutInflater)
        setContentView(binding.root)
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