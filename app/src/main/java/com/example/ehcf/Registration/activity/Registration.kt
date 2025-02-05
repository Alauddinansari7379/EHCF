package com.example.ehcf.Registration.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.Registration.ModelResponse.ModelGender
import com.example.ehcf.Registration.ModelResponse.ModelOTP
import com.example.ehcf.Registration.ModelResponse.ModelOTPResponse
import com.example.ehcf.Registration.ModelResponse.RegistationResponse
import com.example.ehcf.Registration.ModelResponse.SpinnerModel
import com.example.ehcf.databinding.ActivityRegistrationBinding
import com.example.ehcf.login.activity.SignIn
import com.example.myrecyview.apiclient.ApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Registration : AppCompatActivity() {
    private val context = this@Registration
    var loading: LottieAnimationView? = null
    var mydilaog: Dialog? = null

    var bloodGroupList = ArrayList<SpinnerModel>()
    var genderList = ArrayList<ModelGender>()
    private var bloodGroup = ""
    private var phoneNumberWithCode = ""
     private var phoneNumberWithCodeNew = ""
    private var countryCode = ""
    private var fcmToken = ""
    private var genderValue = 0
    private var count2 = 0
    private var responseOTP = "0"
    private var varifyed = false

    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("Alla,", "countryCode$countryCode")
        Log.e("Alla,", "genderValue$genderValue")
        Log.e("Alla,", "bloodGroup$bloodGroup")


        getToken()
        binding.spinnerCountryCode.setOnCountryChangeListener {
            countryCode = binding.spinnerCountryCode.selectedCountryCodeWithPlus
//            val matchingcode = countryCode.substring(countryCode.length, 1);
        }

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSendOTP.setOnClickListener {
            countryCode = binding.spinnerCountryCode.selectedCountryCodeWithPlus
            val phoneWithCode = binding.edtPhoneNumberWithCode.text.toString()
            val phoneWithCodeNew = countryCode + phoneWithCode
            val phoneWithCodeNew1 = phoneWithCodeNew.substring(1);

            Log.e("Alla,", "phoneWithCodeNew-$phoneWithCodeNew1")

            apiCallOTP(phoneWithCodeNew1)

        }

        binding.btnVerify.setOnClickListener {
            if (binding.edtEnterOTP.text.toString() == responseOTP) {
                binding.layoutPhoneWithCode.setBackgroundResource(R.drawable.corner_green);
                // binding.edtPhoneNumberWithCode.setBackgroundColor(Color.parseColor("#FF4CAF50"))
                myToast(this, "Phone Number Verified")
                varifyed = true
                binding.layoutOTP.visibility = View.GONE
                binding.edtPhoneNumberWithCode.isEnabled = false

            } else if (binding.edtEnterOTP.text.isEmpty()) {
                binding.edtEnterOTP.error = "Enter OTP"
                binding.edtEnterOTP.requestFocus()

            } else {
                binding.edtEnterOTP.error = "Wrong OTP"
                binding.edtEnterOTP.requestFocus()
            }


        }



        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar1 = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                DateFormat.getDateInstance().format(newDate.time)
                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDOB.text = date
                dateOfBirth = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate.time)

                Log.e("selectedDate", dateOfBirth)
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )

        binding.tvDOB.setOnClickListener {
            datePicker.show()
        }

        genderList.add(ModelGender("Male", 1))
        genderList.add(ModelGender("Female", 2))
        genderList.add(ModelGender("Transgender", 3))
        genderList.add(ModelGender("Other", 4))
        binding.spinnerGender.adapter =
            ArrayAdapter<ModelGender>(context, android.R.layout.simple_list_item_1, genderList)



        bloodGroupList.add(SpinnerModel("NA"))
        bloodGroupList.add(SpinnerModel("A+"))
        bloodGroupList.add(SpinnerModel("A-"))
        bloodGroupList.add(SpinnerModel("B+"))
        bloodGroupList.add(SpinnerModel("B-"))
        bloodGroupList.add(SpinnerModel("O+"))
        bloodGroupList.add(SpinnerModel("O-"))
        bloodGroupList.add(SpinnerModel("AB+"))
        bloodGroupList.add(SpinnerModel("AB-"))



        binding.spinnerBloodGroup.adapter =
            ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_list_item_1, bloodGroupList)


        binding.edtPhoneNumberWithCode.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                if (binding.edtPhoneNumberWithCode.length() >= 10) {
                    binding.layoutOTP.visibility = View.VISIBLE
                } else {
                    binding.layoutOTP.visibility = View.GONE

                }
            }
        })

        binding.spinnerBloodGroup.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (bloodGroupList.size > 0) {
                        bloodGroup = bloodGroupList[i].bloodGroup

                        Log.e(ContentValues.TAG, "bloodGroup: $bloodGroup")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                if (genderList.size > 0) {

                    genderValue = genderList[i].value.toString().toInt()

                    Log.e(ContentValues.TAG, "genderValue: $genderValue")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.btnRegister.setOnClickListener {
            if (binding.edtFirstName.text.isEmpty()) {
                binding.edtFirstName.error = "First Name Required"
                binding.edtFirstName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtLastName.text.isEmpty()) {
                binding.edtLastName.error = "Last Name Required"
                binding.edtLastName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPhoneNumberWithCode.text.isEmpty()) {
                binding.edtPhoneNumberWithCode.error = "Verify Phone Number"
                binding.edtPhoneNumberWithCode.requestFocus()
                binding.layoutOTP.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (!varifyed) {
                binding.edtPhoneNumberWithCode.error = "Verify Phone Number"
                binding.edtPhoneNumberWithCode.requestFocus()
                binding.layoutOTP.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (binding.edtEmail.text.isEmpty()) {
                binding.edtEmail.error = "Email Required"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }
            if (binding.spinnerBloodGroup.selectedItem == null) {
                myToast(this, "Blood Group Required")
                return@setOnClickListener
            }
            if (binding.edtPassword.text.isEmpty()) {
                binding.edtPassword.error = "Password Required"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtConfirmPassword.text!!.isEmpty()) {
                binding.edtConfirmPassword.error = "Password Required"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            if (binding.tvDOB.text!!.isEmpty()) {
                binding.tvDOB.error = "Select Your DOB"
                binding.tvDOB.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtBiography.text.isEmpty()) {
                binding.edtBiography.error = "Please enter Biography"
                binding.edtBiography.requestFocus()
                return@setOnClickListener
            }
            apiCallRegistration()

        }
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

    private fun subscribed() {
        Firebase.messaging.subscribeToTopic("Patient")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(ContentValues.TAG, msg)
            }
    }

    private fun timeCounter() {
        object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.btnSendOTP.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.shimmer_color));
                binding.tvSecond.text =
                    "OTP Resend In : " + millisUntilFinished / 1000 + " " + " Seconds"
            }

            override fun onFinish() {
                binding.btnSendOTP.isClickable = true
                binding.btnSendOTP.setBackgroundColor(Color.parseColor("#9F367A"))

            }
        }.start()
    }

    companion object {
        var coustmerNameCom = ""
        var phoneNumberCom = ""
        var phoneNumberWithCodeCom = ""
        var emailCom = ""
        var passwordCom = ""
        var bloodGroupCom = ""
        var genderValueCom = ""
        var fcmTokenCom = ""
        var dateOfBirth = ""
    }

    private fun apiCallRegistration() {

        val firstName = binding.edtFirstName.text.toString()
        val middleNAme = binding.edtMiddelName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        var coustmerName = "$firstName $middleNAme $lastName"
        phoneNumberWithCode = binding.edtPhoneNumberWithCode.text.toString()
        val email = binding.edtEmail.text.toString()
        bloodGroup = binding.spinnerBloodGroup.selectedItem.toString()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtConfirmPassword.text.toString()
        val biography = binding.edtBiography.text.toString()

        phoneNumberWithCodeNew = countryCode + phoneNumberWithCode
        val phoneWithCodeNew1 = phoneNumberWithCodeNew.substring(1);

        if (password != confirmPassword) {
            binding.edtConfirmPassword.error = "Password Miss Match"
            binding.edtConfirmPassword.requestFocus()
        } else {

            coustmerNameCom = coustmerName
            phoneNumberCom = binding.edtPhoneNumberWithCode.text.toString()
            phoneNumberWithCodeCom = phoneWithCodeNew1
            emailCom = email
            passwordCom = password
            bloodGroupCom = bloodGroup
            genderValueCom = genderValue.toString()
            fcmTokenCom = fcmToken
            startActivity(Intent(this@Registration, SignaturaPad::class.java))

        }
    }


    private fun apiCallOTP(phoneWithCodeNew: String) {

       AppProgressBar.showLoaderDialog(context)


        ApiClient.apiService.checkPhone(phoneWithCodeNew)
            .enqueue(object :
                Callback<ModelOTP> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelOTP>,
                    response: Response<ModelOTP>
                ) {
                    if (response.code() == 500) {
                        myToast(this@Registration, "Server Error")
                        AppProgressBar.hideLoaderDialog()
                    } else if (response.body()!!.status == 1) {
                        responseOTP = response.body()!!.result.otp
                        myToast(this@Registration, "OTP Send Successfully")
                        binding.btnSendOTP.text = "Resend OTP"
                        binding.btnSendOTP.isClickable = false
                        binding.edtEnterOTP.requestFocus()
                        binding.btnSendOTP.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.shimmer_color));
                        //  binding.btnSendOTP.setBackgroundColor(Color.parseColor("#9F367A"))
                        timeCounter()
                        AppProgressBar.hideLoaderDialog()
                    } else {

                        myToast(this@Registration, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()


                    }
                }

                override fun onFailure(call: Call<ModelOTP>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        apiCallOTP(phoneWithCodeNew)
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


//    override fun onProgressChanged(view: WebView, progress: Int) {
//        //Make the bar disappear after URL is loaded, and changes string to Loading...
//        //setTitle("Loading...");
//        setProgress(progress * 100) //Make the bar disappear after URL is loaded
//
//        // Return the app name after finish loading
//        if (progress == 100) {
//            loading!!.visibility = View.GONE
//        } else {
//            loading!!.visibility = View.VISIBLE
//        }
//    }

}