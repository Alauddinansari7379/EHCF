package com.example.ehcf.Prescription

import android.Manifest.permission.CALL_PHONE
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.R
import com.example.ehcf.activity.ViewPagerAdapter
import com.example.ehcf.databinding.ActivityMain2Binding
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
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

        binding.btnHelp.setOnClickListener {

            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:123456789")

            if (ContextCompat.checkSelfPermission(
                    this,
                    CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(callIntent)
            } else {
                requestPermissions(arrayOf(CALL_PHONE), 1)
            }
        }

//        if (binding.tabs.isFocusable){
//            binding.tabs.backgroundTintList=RED.toInt().toString()
//        }
        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(PrescriptionPendingFragment(), "Prescription Pending")
        adapter.addFragment(PrescribedFragment(), "Prescribed")

        pager.adapter = adapter


        tab.setupWithViewPager(pager)
    }


}