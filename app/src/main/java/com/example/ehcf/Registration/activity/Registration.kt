package com.example.ehcf.Registration.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Registration.ModelResponse.ModelGender
import com.example.ehcf.Registration.ModelResponse.RegistationResponse
import com.example.ehcf.Registration.ModelResponse.SpinnerModel
import com.example.ehcf.databinding.ActivityRegistrationBinding
import com.example.ehcf.login.activity.SignIn
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Registration : AppCompatActivity() {
    private val context: Context = this@Registration
    var loading: LottieAnimationView? = null

    var bloodGroupList = ArrayList<SpinnerModel>()
    var genderList = ArrayList<ModelGender>()
    private var bloodGroup=""
    private var phoneNumberWithCode=""
    var progressDialog: ProgressDialog? =null
    private var phoneNumberWithCodeNew=""
    private var countryCode=""
    private var genderValue=0

    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("Alla,","countryCode$countryCode")
        Log.e("Alla,","genderValue$genderValue")
        Log.e("Alla,","bloodGroup$bloodGroup")

        progressDialog = ProgressDialog(this@Registration)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)


        binding.spinnerCountryCode.setOnCountryChangeListener {
             countryCode = binding.spinnerCountryCode.selectedCountryCodeWithPlus
           val matchingcode = countryCode.substring(countryCode.length ,1);
        }

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }




        genderList.add(ModelGender("Male",1))
        genderList.add(ModelGender("Female",2))
        genderList.add(ModelGender("Transgender",3))
        genderList.add(ModelGender("Other",4))
        binding.spinnerGender.adapter = ArrayAdapter<ModelGender>(context, android.R.layout.simple_list_item_1, genderList)



        bloodGroupList.add(SpinnerModel("A+"))
        bloodGroupList.add(SpinnerModel("A-"))
        bloodGroupList.add(SpinnerModel("B+"))
        bloodGroupList.add(SpinnerModel("B-"))
        bloodGroupList.add(SpinnerModel("O+"))
        bloodGroupList.add(SpinnerModel("O-"))
        bloodGroupList.add(SpinnerModel("AB+"))
        bloodGroupList.add(SpinnerModel("AB-"))



        binding.spinnerBloodGroup.adapter = ArrayAdapter<SpinnerModel>(context, android.R.layout.simple_list_item_1, bloodGroupList)


        binding.spinnerBloodGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (bloodGroupList.size > 0) {
                    bloodGroup = bloodGroupList[i].bloodGroup

                    Log.e(ContentValues.TAG, "bloodGroup: $bloodGroup")
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (genderList.size > 0) {

                    genderValue = genderList[i].value.toString().toInt()

                    Log.e(ContentValues.TAG, "genderValue: $genderValue")
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.btnRegister.setOnClickListener {
            if (binding.edtFirstName.text.isEmpty()){
                binding.edtFirstName.error="First Name Required"
                binding.edtFirstName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtLastName.text.isEmpty()){
                binding.edtLastName.error="Last Name Required"
                binding.edtLastName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPhoneNumber.text.isEmpty()){
                binding.edtPhoneNumber.error="Phone Number Required"
                binding.edtPhoneNumber.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtEmail.text.isEmpty()){
                binding.edtEmail.error="Email Required"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }
            if (binding.spinnerBloodGroup.selectedItem==null){
                myToast(this,"Blood Group Required")
                return@setOnClickListener
            }
            if (binding.edtPassword.text.isEmpty()){
                binding.edtPassword.error="Password Required"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtConfirmPassword.text!!.isEmpty()){
                binding.edtConfirmPassword.error="Password Required"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtBiography.text.isEmpty()){
                binding.edtBiography.error="Please enter Biography"
                binding.edtBiography.requestFocus()
                return@setOnClickListener
            }



            val firstName= binding.edtFirstName.text.toString()
            val lastName= binding.edtLastName.text.toString()
            val coustmerName= "$firstName $lastName"
            val phoneNumber=binding.edtPhoneNumber.text.toString()
            phoneNumberWithCode=binding.edtPhoneNumberWithCode.text.toString()
            val email=binding.edtEmail.text.toString()
            bloodGroup=binding.spinnerBloodGroup.selectedItem.toString()
            val password=binding.edtPassword.text.toString()
            val confirmPassword=binding.edtConfirmPassword.text.toString()
            val biography=binding.edtBiography.text.toString()
            val fcmToken="Alauddin1234"

            phoneNumberWithCodeNew = countryCode + phoneNumberWithCode

            if(password !=confirmPassword){
                binding.edtConfirmPassword.error="Password Miss Match"
                binding.edtConfirmPassword.requestFocus()
            }
            else{

                progressDialog!!.show()

                ApiClient.apiService.register(coustmerName,phoneNumber, phoneNumberWithCodeNew,email,password,bloodGroup,genderValue,fcmToken)
                    .enqueue(object :Callback<RegistationResponse>{
                    @SuppressLint("LogNotTimber")
                    override fun onResponse(
                        call: Call<RegistationResponse>, response: Response<RegistationResponse>) {

                         Log.e("Ala","${response.body()!!.result}")
                        Log.e("Ala", response.body()!!.message)
                        Log.e("Ala","${response.body()!!.status}")

                        if (response.body()!!.status==1){
                            myToast(this@Registration,response.body()!!.message)
                            progressDialog!!.dismiss()
                            startActivity(Intent(context, SignIn::class.java))

                        }
                        else{
                            myToast(this@Registration,"${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        }

                    }

                    override fun onFailure(call: Call<RegistationResponse>, t: Throwable) {
                        myToast(this@Registration,t.message.toString())
                        progressDialog!!.dismiss()

                    }

                })
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