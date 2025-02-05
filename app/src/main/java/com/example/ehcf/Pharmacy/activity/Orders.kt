package com.example.ehcf.Pharmacy.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.Pharmacy.adapter.ViewPagerAdapter
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityOrdersBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.google.android.material.tabs.TabLayout

class Orders : AppCompatActivity() {
    private var context = this@Orders
    val binding by lazy {
        ActivityOrdersBinding.inflate(layoutInflater)
    }
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)
        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabsOrder)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val tabs = findViewById<View>(R.id.tabsOrder) as TabLayout
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                when (tab.position) {
                    0 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#9F367A"))
                    1 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#9F367A"))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        adapter.addFragment(FragmentActive(), "Active")
        adapter.addFragment(FragmentPast(), "Past")
        pager.adapter = adapter
        tab.setupWithViewPager(pager)
    }

}