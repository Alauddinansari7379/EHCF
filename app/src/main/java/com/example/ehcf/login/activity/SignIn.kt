package com.example.ehcf.login.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Fragment.MainActivity
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.PhoneNumber.Activity.PhoneNumber
import com.example.ehcf.R
import com.example.ehcf.Registration.activity.Registration
import com.example.ehcf.databinding.ActivitySigninBinding
import com.example.ehcf.login.modelResponse.LogInResponse
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class SignIn : AppCompatActivity() {
    private val context: Context = this@SignIn
    private lateinit var sessionManager: SessionManager
    var countryCodeNew = "91"
    var fcmToken = ""
    var count = 0
    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        getToken()

        binding.tvWelComeBack.setOnClickListener {
            //startActivity(Intent(this@SignIn,CalanderTest::class.java))
        }
        binding.spinnerCountryCode.setOnCountryChangeListener {
            val countryCode = binding.spinnerCountryCode.selectedCountryCodeWithPlus

            countryCodeNew = countryCode.substring(1)
            Log.e("Log", "countryCode-$countryCodeNew")

        }

        if (sessionManager.isLogin) {
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        }



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
            apiCallLogin()
        }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(context, Registration::class.java))
        }
        binding.tvForgot.setOnClickListener {
            // myToast(this@SignIn,"Work On Progress")

            startActivity(Intent(context, PhoneNumber::class.java))
        }

    }

    private fun apiCallLogin() {
        val phoneNumber = binding.edtPhone.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val phoneNumberNew = countryCodeNew + phoneNumber
        Log.e("Log", "phoneNumberNew-$phoneNumberNew")

        AppProgressBar.showLoaderDialog(context)
        // ApiClient.getApiService().loveline("20").enqueue(object : Callback<LanguageResponse>

        ApiClient.apiService.login(phoneNumberNew, password, fcmToken)
            .enqueue(object : Callback<LogInResponse> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<LogInResponse>,
                    response: Response<LogInResponse>
                ) {
                    if (response.body()!!.status == 1) {
                        myToast(this@SignIn, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                        sessionManager.isLogin = true
                        sessionManager.fcmToken = response.body()!!.result.fcm_token
                        sessionManager.password = response.body()!!.result.password
                        sessionManager.customerName = response.body()!!.result.customer_name
                        sessionManager.email = response.body()!!.result.email
                        sessionManager.phoneNumber = response.body()!!.result.phone_number
                        sessionManager.phoneWithCode = response.body()!!.result.phone_with_code
                        sessionManager.id = response.body()!!.result.id
                        sessionManager.gender = response.body()!!.result.gender

                        Log.e("Alauddin", "sessionManager.fcmToken-${sessionManager.fcmToken}")
                        Log.e("Alauddin", "sessionManager.password-${sessionManager.password}")
                        Log.e(
                            "Alauddin",
                            "sessionManager.customerName-${sessionManager.customerName}"
                        )
                        Log.e("Alauddin", "sessionManager.email-${sessionManager.email}")
                        Log.e(
                            "Alauddin",
                            "sessionManager.phoneNumber-${sessionManager.phoneNumber}"
                        )
                        Log.e(
                            "Alauddin",
                            "sessionManager.phoneWithCode-${sessionManager.phoneWithCode}"
                        )
                        Log.e("Alauddin", "sessionManager.id-${sessionManager.id}")
                        Log.e("Alauddin", "sessionManager.gender-${sessionManager.gender}")

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)

                    } else {
                        myToast(this@SignIn, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallLogin()
                    } else {
                        myToast(this@SignIn, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })

    }

    @SuppressLint("StringFormatInvalid")
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken = task.result

            // Log and toast
            val msg = getString(R.string.channel_id, fcmToken)
            Log.e("Token", fcmToken)
            // Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
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