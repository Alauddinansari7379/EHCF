package com.example.ehcf.Appointments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Appointments.Cancelled.activity.CancelledFragment
import com.example.ehcf.Appointments.Consulted.activity.ConsultedFragment
import com.example.ehcf.Appointments.UpComing.activity.UpComingFragment
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityAppointmentsBinding
import com.example.ehcf.login.activity.SignIn
import com.example.ehcf.sharedpreferences.SessionManager
import com.google.android.material.tabs.TabLayout
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class Appointments : AppCompatActivity() {
    private val context:Context=this@Appointments
    private lateinit var sessionManager:SessionManager
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
        sessionManager= SessionManager(this)
        binding.imgLogout.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to logout?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    sessionManager.logout()
                    val intent = Intent(applicationContext, SignIn::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    startActivity(intent)
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
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
    override fun onStart() {
        super.onStart()
        if (isOnline(this)){
            //  myToast(requireActivity(), "Connected")
        }else{
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