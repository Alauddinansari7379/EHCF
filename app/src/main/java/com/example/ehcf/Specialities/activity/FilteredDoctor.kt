package com.example.ehcf.Specialities.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Specialities.adapter.AdapterFilteredDoctor
import com.example.ehcf.Specialities.model.ModelFilteredDoctor
import com.example.ehcf.databinding.ActivityFilteredDoctorBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilteredDoctor : AppCompatActivity() {
    private lateinit var binding: ActivityFilteredDoctorBinding
    private val context: Context = this@FilteredDoctor
    private var specialitiesID = ""
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        specialitiesID = intent.getStringExtra("specialitiesID").toString()
        Log.e("specialitiesID", "$specialitiesID")
        apiCallFilteredDoctor()
        // apiCallAllDoctor()


    }


    private fun apiCallAllDoctor() {

        progressDialog = ProgressDialog(this@FilteredDoctor)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

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
                            adapter = AdapterAllDoctor(this@FilteredDoctor, response.body()!!)
                            progressDialog!!.dismiss()
                        }
                    } else {
                        myToast(this@FilteredDoctor, response.body()!!.message.toString())
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelAllDoctorNew>, t: Throwable) {
                    myToast(this@FilteredDoctor, "${t.message}")
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
                    if (response.body()!!.result.isEmpty()) {
                        myToast(this@FilteredDoctor, "No Doctor Found")
                        progressDialog!!.dismiss()
                    } else {
                        binding.rvAllDoctor.apply {
                            adapter = AdapterFilteredDoctor(this@FilteredDoctor, response.body()!!)
                            progressDialog!!.dismiss()
                        }

                    }
                }

                override fun onFailure(call: Call<ModelFilteredDoctor>, t: Throwable) {
                    myToast(this@FilteredDoctor, "${t.message}")
                    progressDialog!!.dismiss()

                }

            })

    }

}