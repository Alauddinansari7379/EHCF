package com.example.ehcf.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.afdhal_fa.imageslider.`interface`.ItemClickListener
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.GetLocationDetail
import com.example.easywaylocation.Listener
import com.example.easywaylocation.LocationData
import com.example.ehcf.Appointments.UpComing.adapter.AdapterAppointments
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingHome
import com.example.ehcf.Appointments.UpComing.model.ResultXXXX
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Dashboard.adapter.AdapterNearByDoctor
import com.example.ehcf.Fragment.Model.ModelNearByDoctor
import com.example.ehcf.Fragment.Model.ResultX
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.RatingAndReviews.Rating
import com.example.ehcf.Specialities.activity.FilteredDoctor
import com.example.ehcf.Specialities.activity.Specialities
import com.example.ehcf.databinding.FragmentHomeBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment(), Listener, LocationData.AddressCallBack,AdapterAppointments.ShowPopUp {
    lateinit var easyWayLocation: EasyWayLocation
    lateinit var getLocationDetail: GetLocationDetail
    var progressDialog: ProgressDialog? = null
    lateinit var lm: LocationManager
    var gps_enabled = false
    private var currentAddress = ""
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var lattitude = ""
    var longitude = ""
    var id = ""
    var city = ""
    private var country = ""
    var address = ""
    var getLocation: Button? = null
    private val REQUEST_CODE = 100
     private lateinit var binding: FragmentHomeBinding
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var mainData = ArrayList<ResultX>()
    var meetingId =""
    private var mainDataNew = ArrayList<ResultXXXX>()
    var ratingPage = false
    var dialog: Dialog? = null
    private var currentTime = ""
    var endTime = ""
    var hours = ""
    var minutes = ""
    var secondsNew = ""
    var countN = 0
    var countN1 = 0
    private var diffTime: Long? = null
    private var diffTimeSeconds: Long? = null
    var bookingId = ""
    var d1: Date? = null
    var d2: Date? = null



    // val container = view?.findViewById(com.example.ehcf.R.id.shimmer) as ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer()


        val refreshListener = SwipeRefreshLayout.OnRefreshListener {

            (activity as MainActivity).refreshMain()
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)


        binding.test.setOnClickListener {
            // startActivity(Intent(requireContext(), com.example.ehcf.phonepesdk.MainActivity::class.java))
        }

//
//        CheckInternet().check { connected ->
//            if (connected) {
//
//       // myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(requireContext())
//                changeReceiver.build()
//              //  myToast(requireActivity(),"Check Internet")
//            }
//        }
//        easyWayLocation.startLocation()

        binding.cardChestPain.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "8")
            (context as Activity).startActivity(intent)
        }

        binding.cardCough.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "12")
            (context as Activity).startActivity(intent)
        }

        binding.cardConstipation.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "7")
            (context as Activity).startActivity(intent)
        }
        binding.cardBackPain.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "7")
            (context as Activity).startActivity(intent)
        }


        binding.cardBloodPressuse.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "5")
            (context as Activity).startActivity(intent)
        }

        binding.cardHeatDisease.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "8")
            (context as Activity).startActivity(intent)
        }

        binding.cardJointPain.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "16")
            (context as Activity).startActivity(intent)
        }

        binding.cardFever.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "5")
            (context as Activity).startActivity(intent)
        }

        binding.cardBerating.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "5")
            (context as Activity).startActivity(intent)
        }

        binding.cardMigrane.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "14")
            (context as Activity).startActivity(intent)
        }

        binding.cardhairFall.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "3")
            (context as Activity).startActivity(intent)
        }

        binding.cardToothAche.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "9")
            (context as Activity).startActivity(intent)
        }

        binding.cardSwelling.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "5")
            (context as Activity).startActivity(intent)
        }

        binding.cardArrhythma.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "8")
            (context as Activity).startActivity(intent)
        }

        binding.cardAlopecia.setOnClickListener {
            sessionManager.bookingType = ""
            AdapterAllDoctor.dashboard = "1"
            val intent = Intent(context as Activity, FilteredDoctor::class.java)
                .putExtra("specialitiesID", "3")
            (context as Activity).startActivity(intent)
        }


        getLocationDetail = GetLocationDetail(this, requireContext())
        easyWayLocation = EasyWayLocation(requireContext(), false, false, this)


        lm =requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        val current = resources.configuration.locale

        sessionManager = SessionManager(requireContext())


        id = sessionManager.id.toString()
        // binding.id.text = id

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("FetchContact89", "fetchContacts: coroutine start")

            apiCallGetConsultationAccepted()

        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        getLastLocation()


//        Handler().postDelayed({
//
//        }, 700)

        val imageList = ArrayList<SlideUIModel>()
        //https://bit.ly/2YoJ77H"
        //https://bit.ly/2BteuF2
        imageList.add(
            SlideUIModel(
                "${sessionManager.imageurl}prescription/dafa68d6f10cc3dba833670e242bafe4.png",
                ""
            )
        )
        imageList.add(
            SlideUIModel(
                "${sessionManager.imageurl}prescription/1e1d20758a5ca79b561a869bc23f90e8.png",
                ""
            )
        )
        imageList.add(
            SlideUIModel(
                "${sessionManager.imageurl}prescription/942fd6aea4e27bed28d1cc06e27baf5b.png",
                ""
            )
        )

        binding.imageSlide.setImageList(imageList)



        binding.imageSlide.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(model: SlideUIModel, position: Int) {

                when (position) {
                    0 -> {
                         sessionManager.bookingType = "1"
                        startActivity(Intent(requireContext(), Specialities::class.java))
                    }

                    1 -> {
                        sessionManager.bookingType = "2"
                        startActivity(Intent(requireContext(), Specialities::class.java))
                    }

                    else -> {
                        sessionManager.bookingType = "3"
                        startActivity(Intent(requireContext(), Specialities::class.java))
                    }
                }
                //  myToast(requireActivity(), "${model.title}")
                // Toast.makeText(requireContext(), "${model.title}", Toast.LENGTH_SHORT).show()
            }
        })
        binding.imageSlide.startSliding(2000) // with new period
        // binding.imageSlide.stopSliding()
    }


    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        try {
                            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                            lattitude!!.text = "Lattitude: " + addresses[0].latitude
//                            longitude!!.text = "Longitude: " + addresses[0].longitude
                            Log.e(
                                ContentValues.TAG,
                                " addresses[0].latitude${addresses?.get(0)?.latitude}"
                            )
                            Log.e(
                                ContentValues.TAG,
                                " addresses[0].latitude${addresses?.get(0)?.longitude}"
                            )
                            sessionManager.latitude = addresses?.get(0)?.latitude.toString()
                            sessionManager.longitude = addresses?.get(0)?.longitude.toString()

                            addresses?.get(0)?.getAddressLine(0)

                            val locality = addresses?.get(0)?.locality
                            val countryName = addresses?.get(0)?.countryName
                            val countryCode = addresses?.get(0)?.countryCode
                            val postalCode = addresses?.get(0)?.postalCode.toString()
                            val subLocality = addresses?.get(0)?.subLocality
                            val subAdminArea = addresses?.get(0)?.subAdminArea
                            currentAddress = "$subLocality, $locality, $countryName"
                            postalCodeNew=postalCode
                            apiCallNearByDoctor(postalCode)


                           // binding.tvLocation.text = currentAddress
                            binding.tvLocation.text = addresses?.get(0)?.getAddressLine(0)


                            Log.e(ContentValues.TAG, "locality-$locality")
                            Log.e(ContentValues.TAG, "countryName-$countryName")
                            Log.e(ContentValues.TAG, "countryCode-$countryCode")
                            Log.e(ContentValues.TAG, "postalCode-$postalCode")
                            Log.e(ContentValues.TAG, "subLocality-$subLocality")
                            Log.e(ContentValues.TAG, "subAdminArea-$subAdminArea")

                            Log.e(
                                ContentValues.TAG,
                                " addresses[0].Address${addresses?.get(0)?.getAddressLine(0)}"
                            )

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        } else {
            askPermission()
        }
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    override fun onStart() {
        super.onStart()
        if (isOnline(requireContext())) {
         } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(
                    requireContext(), "Please provide the required permission", Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
    }

    override fun locationOn() {
        getLastLocation()
       // easyWayLocation.startLocation()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("LogNotTimber")
    override fun currentLocation(location: Location?) {
        val latitude = location!!.latitude
        val longitude = location.longitude

        Log.e("getCurrentLocation", ">>>>>>>>>>:$latitude\n$longitude ")
        GlobalScope.launch {
            // getLocationDetail.getAddress(location.latitude, location.longitude, "xyz")
            getLocationDetail.getAddress(location.latitude, location.longitude, "AAA")
            //getLastLocation()
        }

    }

    override fun locationCancelled() {

     }

    @SuppressLint("LogNotTimber")
    override fun locationData(locationData: LocationData?) {
        locationData?.full_address.toString()
//        val currentAddress1 = locationData?.country.toString()
//        val Address = currentAddress+currentAddress1

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EasyWayLocation.LOCATION_SETTING_REQUEST_CODE -> easyWayLocation.onActivityResult(
                resultCode
            )
        }
    }

    private fun apiCallNearByDoctor(postalCode: String) {

         ApiClient.apiService.getNearByDoctor(
             postalCode,

        ).enqueue(object :
                Callback<ModelNearByDoctor> {
                @SuppressLint("LogNotTimber", "SuspiciousIndentation")
                override fun onResponse(
                    call: Call<ModelNearByDoctor>,
                    response: Response<ModelNearByDoctor>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Unable to fetch nearest doctor")
                        } else if (response.body()!!.status == 1) {
                            binding.rvAllDoctor.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.rvAllDoctor.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                 mainData = response.body()!!.result
                                countN1=0
                                adapter = activity?.let {
                                    AdapterNearByDoctor(it,mainData )
                                }
                             }
                        } else {
                            binding.shimmer.visibility = View.GONE

                              myToast(requireActivity(), response.body()!!.message.toString())
                            //  progressDialog!!.dismiss()
                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelNearByDoctor>, t: Throwable) {
                    binding.shimmer.visibility = View.GONE
                    countN1++
                    if (countN1 <= 3) {
                        apiCallNearByDoctor(postalCode)
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()

                    }
//                                                                                                    78.3681035
                }

            })

    }

    private fun apiCallGetConsultationAccepted() {
        ApiClient.apiService.getConsultationAccptedHome(sessionManager.id.toString(), "accepted")
            .enqueue(object : Callback<ModelUpComingHome> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelUpComingHome>, response: Response<ModelUpComingHome>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server error")
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                        } else {
                            homeCall="1"
                            mainDataNew=response.body()!!.result
                            setRecyclerViewAdapter(mainDataNew)
                            binding.shimmer.visibility = View.GONE
                            progressDialog!!.dismiss()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelUpComingHome>, t: Throwable) {
                    countN++
                    if (countN <= 3) {
                        apiCallGetConsultationAccepted()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }
    private fun setRecyclerViewAdapter(data: ArrayList<ResultXXXX>) {
        binding.rvAppointment.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvAppointment.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            adapter = AdapterAppointments(requireContext(), data, this@HomeFragment)
        }
    }
    companion object{
        var postalCodeNew=""
        var homeCall=""
    }
    private fun videoCallFun(startTime: String, id: String) {
        meetingId = id
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = sessionManager.customerName
        jitsiMeetUserInfo.email = sessionManager.email
        try {
            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://jitsi.ethicalhealthcare.in/"))
                .setRoom(startTime)
                .setAudioMuted(false)
                .setVideoMuted(true)
                .setAudioOnly(false)
                .setUserInfo(jitsiMeetUserInfo)
                .setConfigOverride("enableInsecureRoomNameWarning", false)
                .setFeatureFlag("readOnlyName", true)
                .setFeatureFlag("prejoinpage.enabled", false)
                //  .setFeatureFlag("lobby-mode.enabled", false)
                // .setToken("123") // Set the meeting password
                //.setFeatureFlag("autoKnockLobby", false) // Disable lobby mode
                //.setFeatureFlag("disableModeratorIndicator", false)
                //.setFeatureFlag("chat.enabled",false)
                .setConfigOverride("requireDisplayName", true)
                .build()
            JitsiMeetActivity.launch(requireContext(), defaultOptions)
            ratingPage = true
            //  startActivity(Intent(requireContext(),Rating::class.java))
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
    }

    override fun onResume() {
        super.onResume()
         easyWayLocation.startLocation()
        if (ratingPage) {
            //  (activity as Appointments).refresh()
            val intent = Intent(context as Activity, Rating::class.java)
                .putExtra("meetingId", meetingId)
            (context as Activity).startActivity(intent)
            ratingPage = false
        }
    }

    override fun popupRemainingTime(startTime: String) {
        var view = layoutInflater.inflate(R.layout.time_dialognew, null)
        dialog = Dialog(requireContext())
        endTime = startTime
        val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialogNew)
        val hour = view!!.findViewById<TextView>(R.id.tvHourTime)
        val minute = view!!.findViewById<TextView>(R.id.tvMinuteTime)
        val second = view!!.findViewById<TextView>(R.id.tvSecondTime)
        dialog = Dialog(requireContext())

        currentTime = SimpleDateFormat("yy/MM/dd HH:m:ss", Locale.getDefault()).format(Date())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)
        // val d1 = format.parse("2023/03/29 11:04:00")
//        Log.e("currentDate", currentTime)
//        Log.e("EndTime", startTime)

        remainingTime()
        fun timeCalculator(seconds: Long) {
            print(seconds)
            hours = (seconds / 3600).toInt().toString()
            minutes = (seconds % 3600 / 60).toInt().toString()
            secondsNew = (seconds % 3600 % 60).toInt().toString()

            hour.text = hours
            minute.text = minutes
            second.text = secondsNew

            if (second.text.contains("-")){
                apiCallGetConsultationAccepted()

            }
            println("Hours: $hours")
            println("Minutes: $minutes")
            println("Seconds: $seconds")
        }

        timeCalculator(diffTimeSeconds!!)

        dialog?.show()
        btnOkDialog.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun remainingTime() {
        val format = SimpleDateFormat("yy/MM/dd HH:m:ss")

        try {
            d1  = format.parse(currentTime)
            d2 = format.parse(endTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.e("d1", d1!!.time.toString())
        Log.e("d2", d2!!.time.toString())

        diffTime = (d2!!.time - d1!!.time)
        diffTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(diffTime!!)

        Log.e("minnew1", diffTimeSeconds.toString())

    }
    override fun videoCall(startTime: String, id: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Join Meeting?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)

                videoCallFun(startTime, id)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }



}
