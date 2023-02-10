package com.example.ehcf.CreateSlot.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Testing.RazorPay
import com.example.ehcf.databinding.ActivityBookingSlotBinding

class BookingSlot : AppCompatActivity() {
    private val context: Context = this@BookingSlot

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
            if (binding.edtTitle.text.isEmpty()){
                binding.edtTitle.error="Title Required"
                binding.edtTitle.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtDescription.text.isEmpty()){
                binding.edtDescription.error="Description Required"
                binding.edtDescription.requestFocus()
                return@setOnClickListener
            }
            val title= binding.edtTitle.text.toString().trim()
            val description= binding.edtDescription.text.toString().trim()

            val intent = Intent(context as Activity, RazorPay::class.java)
                .putExtra("doctorId",doctorId)
                .putExtra("selecteDate",selectedate)
                .putExtra("startTime",startTime)
                .putExtra("title",title)
                .putExtra("description",description)
            context.startActivity(intent)
        }



    }
}