package com.example.ehcf.Dashboard

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.Dashboard.modelResponse.SearchbyLocationRes
import com.example.ehcf.DateForConsultaion.activity.ShuduleTiming
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
        binding.btnBookAppointment.setOnClickListener {
            startActivity(Intent(this, ShuduleTiming::class.java))
        }

        sessionManager = SessionManager(this)

        progressDialog = ProgressDialog(this@Dashboard)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

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

            apiCall()
        }
    }

        private fun apiCall() {
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

}