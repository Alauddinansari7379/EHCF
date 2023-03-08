package com.example.ehcf.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.databinding.ActivityPrescription2Binding
import com.google.android.material.tabs.TabLayout

class PrescriptionActivity : AppCompatActivity() {
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar

    private lateinit var binding: ActivityPrescription2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescription2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener { 
            onBackPressed()
        }
    }
}