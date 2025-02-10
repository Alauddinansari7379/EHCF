package com.example.ehcf.PhoneNumber.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OTPVerification
import com.example.ehcf.PhoneNumber.ModelReponse.ModelForgotPass
import com.example.ehcf.login.activity.SignIn
import com.example.ehcf.databinding.ActivityPhoneNumberBinding
import com.example.ehcf.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class PhoneNumber : AppCompatActivity() {
    private val context = this@PhoneNumber
    var phoneNumber = ""
    var count = 0

    private lateinit var binding: ActivityPhoneNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(context, SignIn::class.java))
        }

        binding.btnProcess.setOnClickListener {
            myToast(this@PhoneNumber, "Work On Progress")

        }
        binding.btnProcess.setOnClickListener {
            if (binding.edtPhoneNumber.text.isEmpty()) {
                binding.edtPhoneNumber.error = "Enter Phone Number"
                binding.edtPhoneNumber.requestFocus()
                return@setOnClickListener
            }
            apiCallForgotPassword()
        }
    }

    private fun apiCallForgotPassword() {
        val phoneNumberNew = binding.edtPhoneNumber.text.toString()
        val code = "91"
        phoneNumber = code + phoneNumberNew
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.forgotPassword(phoneNumber).enqueue(object :
            Callback<ModelForgotPass> {

            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelForgotPass>, response: Response<ModelForgotPass>
            ) {
                if (response.body()!!.status == 1) {
                    myToast(this@PhoneNumber, response.body()!!.message)
                    AppProgressBar.hideLoaderDialog()
                    val otp = response.body()!!.result.otp
                    val id = response.body()!!.result.id

                    val intent = Intent(context as Activity, OTPVerification::class.java)
                    intent.putExtra("Mobilenumber", phoneNumber)
                    intent.putExtra("id", id.toString())
                    intent.putExtra("otp", otp.toString())
                    Log.e("otp", response.body()!!.result.otp.toString())
                    Log.e("id", response.body()!!.result.id.toString())
                    context.startActivity(intent)
                } else {
                    myToast(context, "Please enter valid phone number")
                    AppProgressBar.hideLoaderDialog()
                }


            }

            override fun onFailure(call: Call<ModelForgotPass>, t: Throwable) {
                count++
                if (count <= 3) {
                    apiCallForgotPassword()
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