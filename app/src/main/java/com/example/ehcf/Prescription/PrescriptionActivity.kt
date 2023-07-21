package com.example.ehcf.Prescription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.activity.ViewPagerAdapter
import com.example.ehcf.databinding.ActivityPrescriptionBinding
import com.giphy.sdk.analytics.GiphyPingbacks.context
import com.google.android.material.tabs.TabLayout
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class PrescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrescriptionBinding
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar
    private lateinit var btnAddReport: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        pager = binding.viewPager
        tab = binding.tabs


        val adapter = ViewPagerAdapter(supportFragmentManager)
        pager.adapter = adapter

        tab.setupWithViewPager(pager)

        adapter.addFragment(PrescriptionPendingFragment(), "Prescription Pending")
        adapter.addFragment(PrescribedFragment(), "Prescribed")
        pager.adapter = adapter
        tab.setupWithViewPager(pager)


    }

    override fun onStart() {
        super.onStart()
        if (isOnline(this@PrescriptionActivity)) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
//        CheckInternet().check { connected ->
//            if (connected) {
//             //    myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
    }


}