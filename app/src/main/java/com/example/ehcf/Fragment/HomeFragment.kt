package com.example.ehcf.Fragment

import android.Manifest
import android.annotation.SuppressLint
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afdhal_fa.imageslider.`interface`.ItemClickListener
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.GetLocationDetail
import com.example.easywaylocation.Listener
import com.example.easywaylocation.LocationData
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.Fragment.test.ImageUpload
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.Specialities
import com.example.ehcf.databinding.FragmentHomeBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.io.IOException
import java.util.*


class HomeFragment : Fragment(), Listener, LocationData.AddressCallBack {
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


    // val container = view?.findViewById(com.example.ehcf.R.id.shimmer) as ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.ehcf.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();


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

        binding.symtom.setOnClickListener {
            startActivity(Intent(requireContext(), ImageUpload::class.java))
        }

        getLocationDetail = GetLocationDetail(this, requireContext())
        easyWayLocation = EasyWayLocation(requireContext(), false, false, this)


        lm =
            requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        val current = resources.configuration.locale

        sessionManager = SessionManager(requireContext())


        id = sessionManager.id.toString()
     //   binding.id.text = id

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("FetchContact89", "fetchContacts: coroutine start")

            apiCallAllDoctor()

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
                "https://ehcf.thedemostore.in/uploads/prescriptions/1679740963.png",
                ""
            )
        )
        imageList.add(
            SlideUIModel(
                "https://ehcf.thedemostore.in/uploads/prescriptions/1679741056.png",
                ""
            )
        )
        imageList.add(
            SlideUIModel(
                "https://ehcf.thedemostore.in/uploads/prescriptions/1679741017.png",
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
        binding.imageSlide.startSliding(100) // with new period
        binding.imageSlide.stopSliding()
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
                                " addresses[0].latitude${addresses[0].latitude}"
                            )
                            Log.e(
                                ContentValues.TAG,
                                " addresses[0].latitude${addresses[0].longitude}"
                            )
                            sessionManager.latitude = addresses[0].latitude.toString()
                            sessionManager.longitude = addresses[0].longitude.toString()

                            addresses[0].getAddressLine(0)

                            val locality = addresses[0].locality
                            val countryName = addresses[0].countryName
                            val countryCode = addresses[0].countryCode
                            val postalCode = addresses[0].postalCode
                            val subLocality = addresses[0].subLocality
                            val subAdminArea = addresses[0].subAdminArea

                            currentAddress = "$subLocality, $locality, $countryName"

                            binding.tvLocation.text = currentAddress

                            Log.e(ContentValues.TAG, "locality-$locality")
                            Log.e(ContentValues.TAG, "countryName-$countryName")
                            Log.e(ContentValues.TAG, "countryCode-$countryCode")
                            Log.e(ContentValues.TAG, "postalCode-$postalCode")
                            Log.e(ContentValues.TAG, "subLocality-$subLocality")
                            Log.e(ContentValues.TAG, "subAdminArea-$subAdminArea")

                            Log.e(
                                ContentValues.TAG,
                                " addresses[0].Address${addresses[0].getAddressLine(0)}"
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
        }

    }

    override fun locationCancelled() {
        TODO("Not yet implemented")
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

    private fun apiCallAllDoctor() {
//
//        progressDialog = ProgressDialog(requireContext())
//        progressDialog!!.setMessage("Loading..")
//        progressDialog!!.setTitle("Please Wait")
//        progressDialog!!.isIndeterminate = false
//        progressDialog!!.setCancelable(true)
        //  progressDialog!!.show()

        val lat = "435435"
        val lng = "54357"
        val searchNew = ""
        ApiClient.apiService.getAllDoctor(
            sessionManager.latitude,
            sessionManager.longitude,
            searchNew
        )
            .enqueue(object :
                Callback<ModelAllDoctorNew> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAllDoctorNew>,
                    response: Response<ModelAllDoctorNew>
                ) {
                    if (response.body()!!.status == 1) {
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterAllDoctor(requireContext(), response.body()!!)
                            //  progressDialog!!.dismiss()
                        }
                    } else {
                        binding.shimmer.visibility = View.GONE

                        //  myToast(requireActivity(), response.body()!!.message.toString())
                        //  progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelAllDoctorNew>, t: Throwable) {
                    binding.shimmer.visibility = View.GONE

                    // myToast(requireActivity(),"${t.message}")
                    // progressDialog!!.dismiss()

                }

            })

    }

    override fun onResume() {
        super.onResume()
        easyWayLocation.startLocation()
    }
}
