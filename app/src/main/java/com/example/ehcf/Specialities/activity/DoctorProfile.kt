package com.example.ehcf.Specialities.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.CreateSlot.activity.ShuduleTiming
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Specialities.adapter.AdapterDoctorProfile
import com.example.ehcf.Specialities.adapter.AdapterFilteredDoctor
import com.example.ehcf.Specialities.model.ModelDoctorProfile
import com.example.ehcf.Specialities.model.ModelFilteredDoctor
import com.example.ehcf.databinding.ActivityDoctorProfileBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorProfile : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorProfileBinding
    var progressDialog: ProgressDialog? = null
    private var context: Context = this@DoctorProfile
    var doctorId=""
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager= SessionManager(this)
        //         doctorId = intent.getStringExtra("doctorId").toString()
         doctorId= intent.getStringExtra("doctorId").toString()
        Log.e("doctorId","$doctorId")
        apiCallDoctorProfile()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
//        binding.bt.setOnClickListener{
//            val intent = Intent(context as Activity, ShuduleTiming::class.java)
//                .putExtra("doctorId",doctorId)
//            context.startActivity(intent)
//        }

    }

    private fun apiCallDoctorProfile() {

        progressDialog = ProgressDialog(this@DoctorProfile)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        Log.e("doctorId", "$doctorId")

        ApiClient.apiService.doctorProfile(doctorId)
            .enqueue(object :
                Callback<ModelDoctorProfile> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDoctorProfile>,
                    response: Response<ModelDoctorProfile>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        myToast(this@DoctorProfile, "No Doctor Found")
                        progressDialog!!.dismiss()
                    } else {
                        binding.rvAllDoctor.apply {
                            adapter = AdapterDoctorProfile(this@DoctorProfile, response.body()!!)
                            progressDialog!!.dismiss()
                        }

                    }
                }

                override fun onFailure(call: Call<ModelDoctorProfile>, t: Throwable) {
                    myToast(this@DoctorProfile, "${t.message}")
                    progressDialog!!.dismiss()

                }

            })

    }

}