package com.example.ehcf.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.Prescription.PrescriptionPendingFragment
import com.example.ehcf.Prescription.ViewPagerAdapter1
import com.example.ehcf.databinding.FragmentNewsBinding
import com.example.ehcf.report.ViewReportFragment
import com.google.android.material.tabs.TabLayout
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class PrecriscptionFragment : Fragment() {
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar
    private lateinit var btnAddReport: TextView
    lateinit var binding: FragmentNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentNewsBinding.inflate(inflater)

        pager = binding.viewPager
        tab = binding.tabs


        val adapter = ViewPagerAdapter1(childFragmentManager)

        adapter.addFragment(Prescribed(), "Prescribed")
        adapter.addFragment(ViewReportFragment(), "View Report")
        pager.adapter = adapter
        tab.setupWithViewPager(pager)

        return (binding.root)

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
