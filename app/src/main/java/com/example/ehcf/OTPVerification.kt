package com.example.ehcf

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.PhoneNumber.Activity.ResetPassword
import com.example.ehcf.PhoneNumber.ModelReponse.ModelForgotPass
import com.example.ehcf.databinding.ActivityOtpverificationBinding
import com.example.ehcf.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class OTPVerification : AppCompatActivity() {
    private lateinit var binding: ActivityOtpverificationBinding
     private val context: Context = this@OTPVerification
    var phoneNumber=""
    var id=""
    var count=0
    private var otp=""
    var finalOTP=""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timeCounter()
        val intent = intent
        phoneNumber = intent.getStringExtra("Mobilenumber").toString()
        id = intent.getStringExtra("id").toString()
        otp = intent.getStringExtra("otp").toString()
        binding.tvMobileNumber.text = "+$phoneNumber"
        // otpPopup(otp)

        Log.e(ContentValues.TAG, "Mobilenumber-----:---$phoneNumber")

        binding.btnVerify.setOnClickListener {
            val otp1 = binding.otpBox1.text.toString()
            val otp2 = binding.otpBox2.text.toString()
            val otp3 = binding.otpBox3.text.toString()
            val otp4 = binding.otpBox4.text.toString()
            Log.e("otp1", otp1)

            finalOTP = "$otp1$otp2$otp3$otp4"

            Log.e("finalOTP", finalOTP.toString())
            Log.e("otp", otp)


            if (finalOTP == otp) {
                val intent = Intent(context as Activity, ResetPassword::class.java)
                intent.putExtra("Mobilenumber", phoneNumber)
                intent.putExtra("id", id)
                context.startActivity(intent)
            } else {
                myToast(this@OTPVerification, "You are entered wrong OTP")
            }

        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.otpBox1.addTextChangedListener {
            if (binding.otpBox1.length() == 1) {
                binding.otpBox2.requestFocus()
            }
        }
        binding.otpBox2.addTextChangedListener {
            if (binding.otpBox2.length() == 1)
                binding.otpBox3.requestFocus()
        }
        binding.otpBox3.addTextChangedListener {
            if (binding.otpBox3.length() == 1)
                binding.otpBox4.requestFocus()
        }
        binding.tvResend.setOnClickListener {
            binding.tvResend.setTextColor(Color.parseColor("#858284"))
            apiCallResendOTP()
        }
    }
    private fun apiCallResendOTP(){
        ApiClient.apiService.forgotPassword(phoneNumber).enqueue(object :Callback<ModelForgotPass>{
            override fun onResponse(
                call: Call<ModelForgotPass>, response: Response<ModelForgotPass>
            ) {
                // Log.e("Ala","${response.body()!!.result}")
                Log.e("Ala","${response.body()!!.message}")
                Log.e("Ala","${response.body()!!.status}")
                if (response.body()!!.status==1){
                    myToast(this@OTPVerification,response.body()!!.message)
                    AppProgressBar.hideLoaderDialog()
                    val otp = response.body()!!.result.otp.toString()
                    val id = response.body()!!.result.id
                    this@OTPVerification.otp =otp
                    // otpPopup(otp)
                    timeCounter()
                }
                else{
                    myToast(this@OTPVerification,"${response.body()!!.message}")
                    AppProgressBar.hideLoaderDialog()
                }
            }
            override fun onFailure(call: Call<ModelForgotPass>, t: Throwable) {
                count++
                if (count <= 3) {
                    apiCallResendOTP()
                } else {
                    myToast(this@OTPVerification, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }


            }

        })
    }
    private fun otpPopup(otp: String) {
        SweetAlertDialog(this@OTPVerification, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your OTP is $otp")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }


    private fun timeCounter() {
        object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResend.isClickable = false
                binding.tvSecond.text =
                    "OTP Resend In : " + millisUntilFinished / 1000 + " " + " Seconds"
            }

            override fun onFinish() {
                binding.tvResend.isClickable = true
                binding.tvResend.setTextColor(Color.parseColor("#9F367A"))

            }
        }.start()
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