package com.example.ehcf.OnlineDoctor.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Fragment.MainActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OnlineDoctor.model.ModelOnlineDoctor
import com.example.ehcf.PaymentMode
import com.example.ehcf.Specialities.adapter.AdapterDoctorProfile
import com.example.ehcf.Specialities.adapter.AdapterOnlineDoctor
import com.example.ehcf.databinding.ActivityFindYourDoctor1Binding
import com.example.ehcf.login.modelResponse.LogInResponse
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnlineDoctor : AppCompatActivity() {
    private lateinit var binding: ActivityFindYourDoctor1Binding
    private val context: Context = this@OnlineDoctor
    var progressDialog: ProgressDialog? = null
    var specialitiesID=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindYourDoctor1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        specialitiesID = intent.getStringExtra("specialitiesID").toString()
        Log.e("specialitiesID", "$specialitiesID")

        apiCallOnlineDoctor()


        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun apiCallOnlineDoctor(){
        progressDialog = ProgressDialog(this@OnlineDoctor)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.onlineDoctors(specialitiesID,"").enqueue(object :
            Callback<ModelOnlineDoctor> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelOnlineDoctor>,
                response: Response<ModelOnlineDoctor>
            ) {

                if (response.body()!!.status == 1) {
                    binding.rvAllDoctor.apply {
                        adapter = AdapterOnlineDoctor(this@OnlineDoctor, response.body()!!)
                        progressDialog!!.dismiss()
                    }
                } else {
                    myToast(this@OnlineDoctor, response.body()!!.message.toString())
                    progressDialog!!.dismiss()
                }

            }

            override fun onFailure(call: Call<ModelOnlineDoctor>, t: Throwable) {
                myToast(this@OnlineDoctor,"${t.message}")
                progressDialog!!.dismiss()

            }

        })
    }


}