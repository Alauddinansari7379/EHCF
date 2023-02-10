package com.example.ehcf.Dashboard.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.Dashboard.modelResponse.SearchbyLocationRes
import com.example.ehcf.Helper.myToast
import com.example.ehcf.databinding.ActivityDashboard2Binding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboard2Binding
    private val context: Context = this@Dashboard
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboard2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        sessionManager = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed(200) {
            apiCallAllDoctor()

        }

        binding.btnSignIn.setOnClickListener {
            if (binding.edtLocation.text!!.isEmpty()) {
                binding.edtLocation.error = "Enter Location"
                binding.edtLocation.requestFocus()
                return@setOnClickListener
            }
//            if (binding.edtSpecilization.text!!.isEmpty()) {
//                binding.edtSpecilization.error = "Enter Password"
//                binding.edtSpecilization.requestFocus()
//                return@setOnClickListener
//            }

            apiCallSearch()
        }
    }

        private fun apiCallSearch() {
            progressDialog = ProgressDialog(this@Dashboard)
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()
            val search = binding.edtLocation.text.toString().trim()
            val password = binding.edtSpecilization.text.toString().trim()

            ApiClient.apiService.searchByLocation(sessionManager.latitude.toString(),sessionManager.longitude.toString(),search)
                .enqueue(object :
                Callback<SearchbyLocationRes> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<SearchbyLocationRes>,
                    response: Response<SearchbyLocationRes>
                ) {
                    if (response.body()!!.status==1){
                        myToast(this@Dashboard, response.body()!!.message)
                        progressDialog!!.dismiss()

                    }else{
                        myToast(this@Dashboard, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }
                }
                override fun onFailure(call: Call<SearchbyLocationRes>, t: Throwable) {
                    myToast(this@Dashboard,"${t.message}")
                    progressDialog!!.dismiss()

                }

            })

        }
        private fun apiCallAllDoctor() {

            progressDialog = ProgressDialog(this@Dashboard)
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()

            val lat="435435"
            val lng="54357"
            val searchNew=""
            ApiClient.apiService.getAllDoctor(sessionManager.latitude,sessionManager.longitude,searchNew)
                .enqueue(object :
                Callback<ModelAllDoctorNew> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAllDoctorNew>,
                    response: Response<ModelAllDoctorNew>
                ) {
                    if (response.body()!!.status==1){
                        binding.rvAllDoctor.apply {
                            adapter = AdapterAllDoctor(this@Dashboard, response.body()!!)
                            progressDialog!!.dismiss()
                        }
                    }else{
                        myToast(this@Dashboard, response.body()!!.message.toString())
                        progressDialog!!.dismiss()
                    }
                }
                override fun onFailure(call: Call<ModelAllDoctorNew>, t: Throwable) {
                    myToast(this@Dashboard,"${t.message}")
                    progressDialog!!.dismiss()

                }

            })

        }

}