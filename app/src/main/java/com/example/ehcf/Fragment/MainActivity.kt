package com.example.ehcf.Fragment

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.GetLocationDetail
import com.example.ehcf.*
import com.example.ehcf.Appointments.Appointments
import com.example.ehcf.Dashboard.activity.Dashboard
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.databinding.ActivityMainBinding
import com.example.ehcf.invoices.Invoice
import com.example.ehcf.report.ReportView
import com.example.ehcf.sharedpreferences.SessionManager
import me.ibrahimsn.lib.SmoothBottomBar
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class MainActivity : AppCompatActivity() {
    private val context: Context = this@MainActivity
    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNav: SmoothBottomBar
    lateinit var navController: NavController

    lateinit var easyWayLocation: EasyWayLocation
    lateinit var getLocationDetail: GetLocationDetail
    lateinit var lm: LocationManager
    private lateinit var sessionManager: SessionManager

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        Log.e("PateintId","${sessionManager.id}")
        bottomNav = binding.bottomNavigation1
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment)
        val navController = navHostFragment!!.findNavController()
        val popupMenu = PopupMenu(this, null)

        popupMenu.inflate(R.menu.bottom_nav_menu)

        binding.bottomNavigation1.setupWithNavController(popupMenu.menu, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.tvTitle.text = when (destination.id) {
                R.id.fragment_Home -> "Home"
                R.id.fragment_booking -> "Booking"
                R.id.fragment_prescription -> "Prescription"
                else -> "Profile" 
            }
        }
        binding.drawerClick.setOnClickListener {
            binding.drawerlayout1.openDrawer(GravityCompat.START)
            binding.includedrawar1.tvDashboard.setOnClickListener {
                startActivity(Intent(this, Dashboard::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvProfileSetting.setOnClickListener {
                startActivity(Intent(this, Setting::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvMyReport.setOnClickListener {
                startActivity(Intent(this, ReportView::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvInvoice.setOnClickListener {
                startActivity(Intent(this, Invoice::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvAppointments.setOnClickListener {
                startActivity(Intent(this, Appointments::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvRefunds.setOnClickListener {
                startActivity(Intent(this, AddNewFamily::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        drawerLayout = binding.drawerlayout1
        // For Navigation UP
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

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

