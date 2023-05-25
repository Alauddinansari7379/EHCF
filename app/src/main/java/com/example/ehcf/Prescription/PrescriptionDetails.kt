package com.example.ehcf.Prescription

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetial
import com.example.ehcf.Prescription.model.ModelPreDetialJava
import com.example.ehcf.Prescription.model.ModelPrescriptionDetial
import com.example.ehcf.databinding.ActivityPrescriptionDetailsBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.giphy.sdk.analytics.GiphyPingbacks.context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class PrescriptionDetails : AppCompatActivity() {
    private lateinit var binding:ActivityPrescriptionDetailsBinding
    var progressDialog:ProgressDialog?=null
    var id=""
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPrescriptionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


         id=intent.getStringExtra("id").toString()
      //  specialitiesID = intent.getStringExtra("specialitiesID").toString()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)
        apiCallGetPrePending()
    }

    private fun apiCallGetPrePending() {
        progressDialog = ProgressDialog(this@PrescriptionDetails)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.viewPrescriptionDetial(id)
            .enqueue(object : Callback<ModelPrescriptionDetial> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrescriptionDetial>, response: Response<ModelPrescriptionDetial>
                ) {

                    if (response.body()!!.result.isEmpty()) {
                         myToast(this@PrescriptionDetails,"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.recyclerView.apply {
                            adapter = AdapterPrescriptionDetial(this@PrescriptionDetails, response.body()!!)
                            progressDialog!!.dismiss()

                        }
//                        Log.e("Tag", response.body()!!.Result().doctorNotes.toString())
//                        binding.Note.text= response.body()!!.Result().doctorNotes.toString()
//                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelPrescriptionDetial>, t: Throwable) {
                    myToast(this@PrescriptionDetails, "Something went wrong")
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