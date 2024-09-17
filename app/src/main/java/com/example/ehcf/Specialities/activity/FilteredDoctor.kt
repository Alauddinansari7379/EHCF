package com.example.ehcf.Specialities.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OnlineDoctor.adapter.AdapterOnlineDoctor
import com.example.ehcf.OnlineDoctor.model.ModelOnlineDoctor
import com.example.ehcf.R
import com.example.ehcf.Specialities.adapter.AdapterFilteredDoctor
import com.example.ehcf.Specialities.model.ModelFilteredDoctor
import com.example.ehcf.databinding.ActivityFilteredDoctorBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import io.supercharge.shimmerlayout.ShimmerLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class FilteredDoctor : AppCompatActivity() {
    private lateinit var binding: ActivityFilteredDoctorBinding
    private val context = this@FilteredDoctor
    private var specialitiesID = ""
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var count = 0
    var count1 = 0
    var count2 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        if (sessionManager.bookingType != "1") {
            binding.layoutOnline.visibility = View.GONE
        }

        shimmerFrameLayout = findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        specialitiesID = intent.getStringExtra("specialitiesID").toString()
        Log.e("specialitiesID", "$specialitiesID")
        apiCallFilteredDoctor()
        // apiCallAllDoctor()
        binding.btnOnlineDoctor.setOnClickListener {
            apiCallOnlineDoctor()
        }
        binding.btnAllDoctors.setOnClickListener {
            apiCallFilteredDoctor1()
        }

    }


    private fun apiCallOnlineDoctor() {

        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.onlineDoctors(specialitiesID, "").enqueue(object :
            Callback<ModelOnlineDoctor> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelOnlineDoctor>,
                response: Response<ModelOnlineDoctor>
            ) {

                try {

                    if (response.body()!!.result.isEmpty()) {
                        myToast(this@FilteredDoctor, "No Online Doctor Found")
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterOnlineDoctor(this@FilteredDoctor, response.body()!!)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } else {
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterOnlineDoctor(this@FilteredDoctor, response.body()!!)
                            AppProgressBar.hideLoaderDialog()
                        }

                    }
                } catch (e: Exception) {
                    myToast(this@FilteredDoctor, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }

            }

            override fun onFailure(call: Call<ModelOnlineDoctor>, t: Throwable) {
                if (count <= 3) {
                    apiCallOnlineDoctor()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            }

        })
    }

    private fun apiCallFilteredDoctor() {


        Log.e("specialitiesIDAPI", "$specialitiesID")

        ApiClient.apiService.filteredDoctor(specialitiesID)
            .enqueue(object :
                Callback<ModelFilteredDoctor> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFilteredDoctor>,
                    response: Response<ModelFilteredDoctor>
                ) {

                    try {

                        if (response.body()!!.result.isEmpty()) {
                            myToast(this@FilteredDoctor, "No Doctor Found")
                            binding.shimmer.visibility = View.GONE

                         } else {
                            binding.rvAllDoctor.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.rvAllDoctor.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                adapter =
                                    AdapterFilteredDoctor(this@FilteredDoctor, response.body()!!)
                             }

                        }
                    } catch (e: Exception) {
                        myToast(this@FilteredDoctor, "Something went wrong")
                        e.printStackTrace()

                    }

                }

                override fun onFailure(call: Call<ModelFilteredDoctor>, t: Throwable) {
                    myToast(this@FilteredDoctor, "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    count1++
                    if (count1 <= 3) {
                        apiCallFilteredDoctor()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })

    }

    private fun apiCallFilteredDoctor1() {

     AppProgressBar.showLoaderDialog(context)

        Log.e("specialitiesIDAPI", "$specialitiesID")

        ApiClient.apiService.filteredDoctor(specialitiesID)
            .enqueue(object :
                Callback<ModelFilteredDoctor> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFilteredDoctor>,
                    response: Response<ModelFilteredDoctor>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            myToast(this@FilteredDoctor, "No Doctor Found")
                            binding.shimmer.visibility = View.GONE
                            binding.rvAllDoctor.apply {
                                count2=0
                                shimmerFrameLayout?.startShimmer()
                                binding.rvAllDoctor.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                adapter =
                                    AdapterFilteredDoctor(this@FilteredDoctor, response.body()!!)
                                AppProgressBar.hideLoaderDialog()
                            }
                        } else {
                            binding.rvAllDoctor.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.rvAllDoctor.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                adapter =
                                    AdapterFilteredDoctor(this@FilteredDoctor, response.body()!!)
                                AppProgressBar.hideLoaderDialog()
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@FilteredDoctor, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelFilteredDoctor>, t: Throwable) {
                     binding.shimmer.visibility = View.GONE

                    count2++
                    if (count2 <= 3) {
                        apiCallFilteredDoctor1()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    override fun onStart() {
        super.onStart()
        CheckInternet().check { connected ->
            if (connected) {

                // myToast(requireActivity(),"Connected")
            } else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }

}