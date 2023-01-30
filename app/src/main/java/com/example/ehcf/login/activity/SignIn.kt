package com.example.ehcf.login.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Fragment.MainActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.PhoneNumber.Activity.PhoneNumber
import com.example.ehcf.Registration.activity.Registration
import com.example.ehcf.Testing.CalanderTest
import com.example.ehcf.databinding.ActivitySigninBinding
import com.example.ehcf.login.modelResponse.LogInResponse
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : AppCompatActivity() {
    private val context: Context = this@SignIn
    var progressDialog: ProgressDialog? =null
    private lateinit var sessionManager: SessionManager

    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)



        binding.tvWelComeBack.setOnClickListener {
            startActivity(Intent(this@SignIn,CalanderTest::class.java))
        }

        if (sessionManager.isLogin) {
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        }

        progressDialog = ProgressDialog(this@SignIn)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        binding.btnSignIn.setOnClickListener {
            if (binding.edtPhone.text!!.isEmpty()) {
                binding.edtPhone.error = "Enter Phone Number"
                binding.edtPhone.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPassword.text!!.isEmpty()) {
                binding.edtPassword.error = "Enter Password"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }
            val phoneNumber = binding.edtPhone.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val fcmToken="sadasdqweqq34e23fdcdsf"

            progressDialog!!.show()
           // ApiClient.getApiService().loveline("20").enqueue(object : Callback<LanguageResponse>

            ApiClient.apiService.login(phoneNumber,password,fcmToken).enqueue(object :Callback<LogInResponse>{
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<LogInResponse>,
                    response: Response<LogInResponse>
                ) {
                    if (response.body()!!.status==1){
                        myToast(this@SignIn, response.body()!!.message)
                        progressDialog!!.dismiss()
                        sessionManager.isLogin = true
                        sessionManager.fcmToken = response.body()!!.result.fcm_token
                        sessionManager.password =response.body()!!.result.password
                        sessionManager.customerName =response.body()!!.result.customer_name
                        sessionManager.email =response.body()!!.result.email
                        sessionManager.phoneNumber =response.body()!!.result.phone_number
                        sessionManager.phoneWithCode =response.body()!!.result.phone_with_code
                        sessionManager.id =response.body()!!.result.id
                        sessionManager.gender =response.body()!!.result.gender

                        Log.e("Alauddin","sessionManager.fcmToken-${sessionManager.fcmToken}")
                        Log.e("Alauddin","sessionManager.password-${sessionManager.password}")
                        Log.e("Alauddin","sessionManager.customerName-${sessionManager.customerName}")
                        Log.e("Alauddin","sessionManager.email-${sessionManager.email}")
                        Log.e("Alauddin","sessionManager.phoneNumber-${sessionManager.phoneNumber}")
                        Log.e("Alauddin","sessionManager.phoneWithCode-${sessionManager.phoneWithCode}")
                        Log.e("Alauddin","sessionManager.id-${sessionManager.id}")
                        Log.e("Alauddin","sessionManager.gender-${sessionManager.gender}")

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)

                    }else{
                        myToast(this@SignIn, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                    myToast(this@SignIn,"${t.message}")
                    progressDialog!!.dismiss()

                }

            })

        }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(context, Registration::class.java))
        }
        binding.tvForgot.setOnClickListener {
            startActivity(Intent(context, PhoneNumber::class.java))
        }

    }

}