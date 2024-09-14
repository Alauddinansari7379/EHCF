package com.example.ehcf.CreateSlot.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.PaymentMode
import com.example.ehcf.databinding.ActivityBookingSlotBinding
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class Payment : AppCompatActivity() {
    private val context: Context = this@Payment

    private lateinit var binding:ActivityBookingSlotBinding
    var doctorId=""
    var selectedate=""
    var startTime=""
    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBookingSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

         doctorId = intent.getStringExtra("doctorId").toString()
         selectedate = intent.getStringExtra("selecteDate").toString()
         startTime = intent.getStringExtra("startTime").toString()

        binding.tvSlotDate.text=selectedate
        binding.tvSlotTime.text=startTime

        Log.e("doctorId","$doctorId")
        Log.e("selectedate","$selectedate")
        Log.e("startTime","$startTime")

        binding.btnPayment.setOnClickListener {
            val intent = Intent(context as Activity, PaymentMode::class.java)
                .putExtra("doctorId",doctorId)
                .putExtra("selecteDate",selectedate)
                .putExtra("startTime",startTime)
            context.startActivity(intent)
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