package com.example.ehcf.OnlineDoctor.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OnlineDoctor.model.ModelOnlineDoctor
import com.example.ehcf.OnlineDoctor.adapter.AdapterOnlineDoctor
import com.example.ehcf.databinding.ActivityFindYourDoctor1Binding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

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

                if (response.body()!!.result.isEmpty()) {
                    myToast(this@OnlineDoctor, "No Doctor Found")
                    progressDialog!!.dismiss()
                } else {
                    binding.rvAllDoctor.apply {
                        adapter = AdapterOnlineDoctor(this@OnlineDoctor, response.body()!!)
                        progressDialog!!.dismiss()
                    }

                }

            }

            override fun onFailure(call: Call<ModelOnlineDoctor>, t: Throwable) {
                myToast(this@OnlineDoctor,"Something went wrong")
                progressDialog!!.dismiss()

            }

        })
    }
    override fun onStart() {
        super.onStart()
        CheckInternet().check { connected ->
            if (connected) {

                // myToast(requireActivity(),"Connected")
            }
            else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }


}