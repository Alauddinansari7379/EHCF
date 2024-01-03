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
    private val context: Context = this@FilteredDoctor
    private var specialitiesID = ""
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        if (sessionManager.bookingType!="1"){
            binding.layoutOnline.visibility=View.GONE
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


    private fun apiCallDoctor() {

        progressDialog = ProgressDialog(this@FilteredDoctor)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
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
                    try {
                        if (response.body()!!.status == 1) {
                            binding.rvAllDoctor.apply {

                                adapter = AdapterAllDoctor(this@FilteredDoctor, response.body()!!)
                                progressDialog!!.dismiss()
                            }
                        } else {
                            myToast(this@FilteredDoctor, response.body()!!.message.toString())
                            progressDialog!!.dismiss()
                            binding.shimmer.visibility = View.GONE

                        }
                    }catch (e:Exception){
                        myToast(this@FilteredDoctor,"Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelAllDoctorNew>, t: Throwable) {
                    myToast(this@FilteredDoctor, "${t.message}")
                    progressDialog!!.dismiss()

                }

            })

    }

    private fun apiCallOnlineDoctor() {
        progressDialog = ProgressDialog(this@FilteredDoctor)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

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
                        progressDialog!!.dismiss()
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterOnlineDoctor(this@FilteredDoctor, response.body()!!)
                            progressDialog!!.dismiss()
                        }
                    } else {
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterOnlineDoctor(this@FilteredDoctor, response.body()!!)
                            progressDialog!!.dismiss()
                        }

                    }
                }catch (e:Exception){
                    myToast(this@FilteredDoctor,"Something went wrong")
                    progressDialog!!.dismiss()
                }

            }

            override fun onFailure(call: Call<ModelOnlineDoctor>, t: Throwable) {
                myToast(this@FilteredDoctor, "Something went wrong")
                progressDialog!!.dismiss()

            }

        })
    }

    private fun apiCallFilteredDoctor() {

        progressDialog = ProgressDialog(this@FilteredDoctor)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        //  progressDialog!!.show()

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

                        progressDialog!!.dismiss()
                    } else {
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterFilteredDoctor(this@FilteredDoctor, response.body()!!)
                            progressDialog!!.dismiss()
                        }

                    }
                    } catch (e: Exception) {
                        myToast(this@FilteredDoctor, "Something went wrong")
                        e.printStackTrace()
                         progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelFilteredDoctor>, t: Throwable) {
                    myToast(this@FilteredDoctor, "Something went wrong")
                    binding.shimmer.visibility = View.GONE

                    progressDialog!!.dismiss()

                }

            })

    }
    private fun apiCallFilteredDoctor1() {

        progressDialog = ProgressDialog(this@FilteredDoctor)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
          progressDialog!!.show()

        Log.e("specialitiesIDAPI", "$specialitiesID")

        ApiClient.apiService.filteredDoctor(specialitiesID)
            .enqueue(object :
                Callback<ModelFilteredDoctor> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFilteredDoctor>,
                    response: Response<ModelFilteredDoctor>
                ) {
                    try{
                    if (response.body()!!.result.isEmpty()) {
                        myToast(this@FilteredDoctor, "No Doctor Found")
                        binding.shimmer.visibility = View.GONE
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterFilteredDoctor(this@FilteredDoctor, response.body()!!)
                            progressDialog!!.dismiss()
                        }
                        progressDialog!!.dismiss()
                    } else {
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterFilteredDoctor(this@FilteredDoctor, response.body()!!)
                            progressDialog!!.dismiss()
                        }

                    }
                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(this@FilteredDoctor,"Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelFilteredDoctor>, t: Throwable) {
                    myToast(this@FilteredDoctor, "Something went wrong")
                    binding.shimmer.visibility = View.GONE

                    progressDialog!!.dismiss()

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