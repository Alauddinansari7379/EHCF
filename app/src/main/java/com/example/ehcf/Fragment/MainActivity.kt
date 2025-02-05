package com.example.ehcf.Fragment


import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.GetLocationDetail
import com.example.ehcf.Appointments.Appointments
import com.example.ehcf.Dashboard.activity.Dashboard
import com.example.ehcf.Diagnostic.TestList
import com.example.ehcf.FamailyMember.activity.AddNewFamily
import com.example.ehcf.FamailyMember.activity.FamilyList
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Pharmacy.activity.BrowseMedicine
import com.example.ehcf.MyDoctor.activity.MyDoctors
import com.example.ehcf.PhoneNumber.Activity.ResetPassword
import com.example.ehcf.Prescription.PrescriptionActivity
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.PrivacyPolicies
import com.example.ehcf.R
import com.example.ehcf.Setting
import com.example.ehcf.Upload.activity.ReportList
import com.example.ehcf.Upload.activity.UploadReportNew
import com.example.ehcf.databinding.ActivityMainBinding
import com.example.ehcf.invoices.Invoice
import com.example.ehcf.login.activity.SignIn
import com.example.ehcf.sharedpreferences.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import me.ibrahimsn.lib.SmoothBottomBar
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver


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
    private val NOTIFICATION_PERMISSION_CODE = 123


    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        PrescriptionDetails.FollowUP == ""
        Log.e("PateintId", "${sessionManager.id}")

        bottomNav = binding.bottomNavigation1
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment)
        val navController = navHostFragment!!.findNavController()
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav_menu)

        requestNotificationPermission()

        if (sessionManager.imageurl!!.isEmpty()){
            sessionManager.imageurl="https://ehcf.in/uploads/"
        }


         when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.e("Notification", "onCreate: PERMISSION GRANTED")
               // sendNotification(this)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView, "Notification blocked", Snackbar.LENGTH_LONG
                ).setAction("Settings") {
                    // Responds to click on the action
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            }
            else -> {
                // The registered ActivityResultCallback gets the result of this request
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }



//        requestPermissionLauncher =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//                // refreshUI()
//                if (it) {
//                    //  myToast(this@MainActivity,"Permission Granted")
//                    //  showDummyNotification()
//                } else {
//                    Snackbar.make(
//                        findViewById<View>(android.R.id.content).rootView, "Please grant Notification permission from App Settings",
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
//            }

        binding.tvTitle.setOnClickListener {
            // startActivity(Intent(this@MainActivity,SignaturaPad::class.java))
        }
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
//            binding.includedrawar1.tvMyReport.setOnClickListener {
//                startActivity(Intent(this, ReportMain::class.java))
//                drawerLayout.closeDrawer(GravityCompat.START)
//            }

            binding.includedrawar1.tvAyuSynk.setOnClickListener {
                startActivity(Intent(this, com.example.ehcf.AyuSynk.MainActivity::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvPrescription.setOnClickListener {
                startActivity(Intent(this, PrescriptionActivity::class.java))
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

            binding.includedrawar1.familyMemberList.setOnClickListener {
                startActivity(Intent(this, FamilyList::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.AddFamilyList.setOnClickListener {
                startActivity(Intent(this, AddNewFamily::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.tvDiagnostic.setOnClickListener {
                startActivity(Intent(this, TestList::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.tvMedicine.setOnClickListener {
                startActivity(Intent(this, BrowseMedicine::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
//            binding.includedrawar1.familyMemberHistory.setOnClickListener {
//                startActivity(Intent(this, FamilyMemberHistory::class.java))
//                drawerLayout.closeDrawer(GravityCompat.START)
//            }

            binding.includedrawar1.orders.setOnClickListener {
                if (binding.includedrawar1.orderLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.orderLayout.visibility = View.GONE
                    binding.includedrawar1.orderLayout.setBackgroundColor(resources.getColor(R.color.white))
                    binding.includedrawar1.orders.setBackgroundColor(resources.getColor(R.color.white))
                    binding.includedrawar1.orderArrow.setImageResource(R.drawable.icn_next_arrow)
                } else {
                    binding.includedrawar1.orderLayout.visibility = View.VISIBLE
                    binding.includedrawar1.orders.setBackgroundColor(resources.getColor(R.color._F8F8F8))
                    binding.includedrawar1.orderLayout.setBackgroundColor(resources.getColor(R.color._F8F8F8))
                    binding.includedrawar1.orderArrow.setImageResource(R.drawable.icn_drop_arrow)
                }
            }
            binding.includedrawar1.Report.setOnClickListener {
                if (binding.includedrawar1.reportLayout.visibility == View.VISIBLE) {
                    binding.includedrawar1.reportLayout.visibility = View.GONE
                    binding.includedrawar1.reportLayout.setBackgroundColor(resources.getColor(R.color.white))
                    binding.includedrawar1.Report.setBackgroundColor(resources.getColor(R.color.white))
                    binding.includedrawar1.reportArrow.setImageResource(R.drawable.icn_next_arrow)
                } else {
                    binding.includedrawar1.reportLayout.visibility = View.VISIBLE
                    binding.includedrawar1.Report.setBackgroundColor(resources.getColor(R.color._F8F8F8))
                    binding.includedrawar1.reportLayout.setBackgroundColor(resources.getColor(R.color._F8F8F8))
                    binding.includedrawar1.reportArrow.setImageResource(R.drawable.icn_drop_arrow)
                }
            }
            binding.includedrawar1.AddReport.setOnClickListener {
                startActivity(Intent(this, UploadReportNew::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.reportList.setOnClickListener {
                startActivity(Intent(this, ReportList::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
//            binding.includedrawar1.tvRefunds.setOnClickListener {
//                startActivity(Intent(this, AddNewFamily::class.java))
//                drawerLayout.closeDrawer(GravityCompat.START)
//            }
            binding.includedrawar1.tvMyDoctor.setOnClickListener {
                startActivity(Intent(this, MyDoctors::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvPasswordChange.setOnClickListener {
                val intent = Intent(context as Activity, ResetPassword::class.java)
                intent.putExtra("id", sessionManager.id.toString())
                context.startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvPrivacyTerms.setOnClickListener {
                startActivity(Intent(this, PrivacyPolicies::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.tvLogOut.setOnClickListener {
                val pDialog =  SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)//CUSTOM_IMAGE_TYPE//ERROR_TYPE //PROGRESS_TYPE
                pDialog .setTitleText("Are you sure want to logout?")
                pDialog .setCancelText("No")
                pDialog .setConfirmText("Yes")
              //  pDialog .setCustomImage(R.drawable.location)
                pDialog .showCancelButton(false)
                pDialog .setConfirmClickListener { sDialog ->
                        sDialog.cancel()
                        sessionManager.logout()
                        val intent = Intent(applicationContext, SignIn::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                    }
                pDialog .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
             //   pDialog.setCancelable(false)
                pDialog.show()
                drawerLayout.closeDrawer(GravityCompat.START)
            }

        }
        drawerLayout = binding.drawerlayout1
        // For Navigation UP
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

    }

    var doubleBackToExitPressedOnce = false

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
                            showSettingDialog()
                        }
                    }
                }
            } else {
                Toast.makeText(applicationContext, "notification permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    var hasNotificationPermissionGranted = false
    private val isNotificationListenerEnabled: Boolean
        private get() {
            val pkgName = packageName
            val cn = ComponentName(pkgName, "$pkgName.NotificationListener")
            val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            return flat != null && flat.contains(cn.flattenToString())
        }
    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    {
            isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
            //  sendNotification(this)
            // myToast(this@MainActivity,"Permission granted")
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED
        ) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            )
        ) {
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
            NOTIFICATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Checking the request code of our request
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            // If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show()
            } else {
                // Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    fun refreshMain() {
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        if (isOnline(this)) {
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

