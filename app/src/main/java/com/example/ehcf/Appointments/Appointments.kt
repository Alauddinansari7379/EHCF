package com.example.ehcf.Appointments

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.Appointments.Cancelled.activity.CancelledFragment
import com.example.ehcf.Appointments.Consulted.activity.ConsultedFragment
import com.example.ehcf.Appointments.UpComing.activity.UpComingFragment
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityAppointmentsBinding
import com.google.android.material.tabs.TabLayout

class Appointments : AppCompatActivity() {
    private lateinit var binding:ActivityAppointmentsBinding
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar    // creating object of ToolBar

    @SuppressLint("UseCompatLoadingForDrawables", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val tabs = findViewById<View>(R.id.tabs) as TabLayout

        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                when(tab.position) {
                    0 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#45369F"))
                    1 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#3A97C5"))
                    2 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF0413"))
                }

//                if (tab.position == 0) {
//                    tabs.setSelectedTabIndicatorColor(Color.parseColor("#45369F"))
//                }
//                else if (tab.position == 1) {
//                    tabs.setSelectedTabIndicatorColor(Color.parseColor("#3A97C5"))
//                }
//                else {
//                    tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF0413"))
//                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        adapter.addFragment(UpComingFragment(), "Upcoming")
        adapter.addFragment(ConsultedFragment(), "Consulted")
        adapter.addFragment(CancelledFragment(), "Cancelled")

        pager.adapter = adapter


        tab.setupWithViewPager(pager)
    }
}