package com.example.ehcf.report.activity

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.R
import com.example.ehcf.activity.ViewPagerAdapter
import com.example.ehcf.databinding.ActivityMain2Binding
import com.example.ehcf.report.ViewReportFragment
import com.giphy.sdk.analytics.GiphyPingbacks.context
import com.google.android.material.tabs.TabLayout
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class ReportMain : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar    // creating object of ToolBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        prescriptionId = intent.getStringExtra("id").toString()



//        binding.btnHelp.setOnClickListener {
//
//            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.data = Uri.parse("tel:123456789")
//
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    CALL_PHONE
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                startActivity(callIntent)
//            } else {
//                requestPermissions(arrayOf(CALL_PHONE), 1)
//            }
//        }

//        if (binding.tabs.isFocusable){
//            binding.tabs.backgroundTintList=RED.toInt().toString()
//        }
        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(AddReport(), "Add Report")
        adapter.addFragment(ViewReportFragment(), "View Report")

        pager.adapter = adapter


        tab.setupWithViewPager(pager)
    }
    companion object{
       var prescriptionId=""
    }
    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
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