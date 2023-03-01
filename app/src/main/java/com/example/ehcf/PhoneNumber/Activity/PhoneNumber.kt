package com.example.ehcf.PhoneNumber.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OTPVerification
import com.example.ehcf.PhoneNumber.ModelReponse.ForgotPasswordResponse
import com.example.ehcf.login.activity.SignIn
import com.example.ehcf.databinding.ActivityPhoneNumberBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class PhoneNumber : AppCompatActivity() {
    private val context: Context = this@PhoneNumber
    var progressDialog: ProgressDialog? =null
    var phoneNumber=""

    private lateinit var binding: ActivityPhoneNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = ProgressDialog(this@PhoneNumber)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(context, SignIn::class.java))
        }

        binding.btnProcess.setOnClickListener {
                if (binding.edtPhoneNumber.text.isEmpty()){
                    binding.edtPhoneNumber.error="Enter Phone Number"
                    binding.edtPhoneNumber.requestFocus()
                    return@setOnClickListener
                }
            phoneNumber= binding.edtPhoneNumber.text.toString()
            progressDialog!!.show()

            ApiClient.apiService.forgotPassword(phoneNumber).enqueue(object :Callback<ForgotPasswordResponse>{

                override fun onResponse(
                    call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>
                ) {

                    // Log.e("Ala","${response.body()!!.result}")
                    Log.e("Ala","${response.body()!!.message}")
                    Log.e("Ala","${response.body()!!.status}")
                    if (response.body()!!.status==1){
                        myToast(this@PhoneNumber,response.body()!!.message)
                        progressDialog!!.dismiss()
                        val intent = Intent(context as Activity, OTPVerification::class.java)
                        intent.putExtra("Mobilenumber", phoneNumber)
                        context.startActivity(intent)

                    }
                    else{
                        myToast(this@PhoneNumber,"${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    myToast(this@PhoneNumber,"Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
        }
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