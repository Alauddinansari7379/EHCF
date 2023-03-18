package com.example.ehcf

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Appointments.Appointments
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OnlineDoctor.model.ModelCreateConsultation
import com.example.ehcf.Testing.RazorPay
import com.example.ehcf.databinding.ActivityPaymentModeBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.giphy.sdk.analytics.GiphyPingbacks.context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class PaymentMode : AppCompatActivity() {
    private val context: Context = this@PaymentMode
    private var doctorId=""
    private lateinit var sessionManager:SessionManager
    var selectedate = ""
    var progressDialog: ProgressDialog? = null
    var startTime = ""
    var title = ""
    var description = ""
    private lateinit var binding: ActivityPaymentModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager= SessionManager(this)

        if (sessionManager.bookingType.toString()=="1"){
            binding.cardCashOnDelivery.visibility=View.GONE

        }

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        doctorId = intent.getStringExtra("doctorId").toString()
        selectedate = intent.getStringExtra("selecteDate").toString()
        startTime = intent.getStringExtra("startTime").toString()
        title = intent.getStringExtra("title").toString()
        description = intent.getStringExtra("description").toString()

        Log.e("doctorId",doctorId)
        Log.e("selectedate",selectedate)
        Log.e("startTime",startTime)
        Log.e("title",title)
        Log.e("description",description)



        binding.cardCashOnDelivery.setOnClickListener {
            SweetAlertDialog(this@PaymentMode, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to Confirm?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    when (sessionManager.bookingType) {
                        "2" -> {
                            apiCallCreateBookingAppointment()
                        }
                        else -> {
                            apiCallCreateBookingHomeVisit()
                        }

                    }
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()


            }



        binding.cardRazorPay.setOnClickListener {
            val intent = Intent(context as Activity, RazorPay::class.java)
                .putExtra("doctorId",doctorId)
                .putExtra("selecteDate",selectedate)
                .putExtra("startTime",startTime)
            context.startActivity(intent)
        }

    }
    private fun apiCallCreateBookingAppointment(){

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount="1000"
        val paymentMode="1"

        ApiClient.apiService.createConsultation(sessionManager.id.toString(),doctorId,amount,paymentMode,sessionManager.bookingType, selectedate,startTime)
            .enqueue(object : Callback<ModelCreateConsultation>
            {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>,
                    response: Response<ModelCreateConsultation>
                )
                {
                    if(response.code()==500){
                        myToast(this@PaymentMode,"Server Error")
                        progressDialog!!.dismiss()

                    }
                    else if (response.body()!!.status==1){
                        popUp()
                        progressDialog!!.dismiss()
                    }


                    else{
                        myToast(this@PaymentMode,response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    t.message?.let { myToast(this@PaymentMode, it) }
                    progressDialog!!.dismiss()
                }


            })
    }

    private fun apiCallCreateBookingHomeVisit(){

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount="1000"
        val paymentMode="1"

        ApiClient.apiService.createConsultation(sessionManager.id.toString(),doctorId,amount,paymentMode,sessionManager.bookingType,selectedate,startTime)
            .enqueue(object : Callback<ModelCreateConsultation>
            {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>,
                    response: Response<ModelCreateConsultation>
                )
                {
                    if (response.body()!!.status==1){
                        popUp()
                        progressDialog!!.dismiss()
                    }else{
                        myToast(this@PaymentMode,response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    t.message?.let { myToast(this@PaymentMode, it) }
                    progressDialog!!.dismiss()
                }


            })
    }
    private fun popUp(){
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Your Booking Confirmed")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                val intent = Intent(applicationContext, Appointments::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                finish()
                startActivity(intent)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }
    private fun popUpConfirm(startTime: String, id: String){

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