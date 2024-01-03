package com.example.ehcf

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.Fragment.MainActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OnlineDoctor.model.ModelCreateConsultation
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.databinding.ActivityPaymentModeBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.google.gson.Gson
import com.papayacoders.phonepe.ApiUtilities
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePeInitException
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.nio.charset.Charset
import java.security.MessageDigest

class PaymentMode : AppCompatActivity(), PaymentResultListener {
    private val context: Context = this@PaymentMode
    private var doctorId = ""
    private lateinit var sessionManager: SessionManager
    var selectedate = ""
    var progressDialog: ProgressDialog? = null
    var startTime = ""
    var slotId = ""
    var amt = 1000
    var title = ""
    var paymentMode = ""
    var description = ""

    var apiEndPoint = "/pg/v1/pay"
    val salt = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399" // salt key
    val MERCHANT_ID = "PGTESTPAYUAT"  // Merhcant id
    val MERCHANT_TID = "txnId"
    val BASE_URL = "https://api-preprod.phonepe.com/"


    // private val razorpay = Razorpay(this@PaymentMode, "rzp_test_tnOp3sjGL6gTju")

    private lateinit var binding: ActivityPaymentModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        PhonePe.init(this@PaymentMode)
        try {
            val upiApps = PhonePe.getUpiApps()
            Log.e("UPIAPPS",upiApps.toString())
        } catch (exception: PhonePeInitException) {
            exception.printStackTrace();
        }

        val data = JSONObject()
        data.put("merchantTransactionId", MERCHANT_TID)//String. Mandatory

        data.put("merchantId" , MERCHANT_ID) //String. Mandatory

        data.put("amount", sessionManager.pricing!!.toInt() * 100 )//Long. Mandatory

        data.put("mobileNumber", "7908834635") //String. Optional
        data.put("callbackUrl", "https://webhook.site/6658f3da-60a4-440f-a743-27e3dcdb91a8") //String. Mandatory

        val paymentInstrument = JSONObject()
        paymentInstrument.put("type", "PAY_PAGE")
        //  paymentInstrument.put("targetApp", "com.phonepe.simulator")

        data.put("paymentInstrument", paymentInstrument )//OBJECT. Mandatory


        val deviceContext = JSONObject()
        deviceContext.put("deviceOS", "ANDROID")
        data.put("deviceContext", deviceContext)



        val payloadBase64 = Base64.encodeToString(
            data.toString().toByteArray(Charset.defaultCharset()), Base64.NO_WRAP
        )
        val checksum = sha256(payloadBase64 + apiEndPoint + salt)+"###1"

       // SHA256(base64 encoded payload + "/pg/v1/pay" + salt key) + ### + salt index

        val b2BPGRequest = B2BPGRequestBuilder()
            .setData(payloadBase64)
            .setChecksum(checksum)
            .setUrl(apiEndPoint)
            .build()


        binding.cardPhonePay.setOnClickListener {
            Log.e("payloadBase64", "$payloadBase64")
            Log.e("checksum", "$checksum")
            try {
                PhonePe.getImplicitIntent(this, b2BPGRequest, "")
                    ?.let { startActivityForResult(it, 1) };
            } catch (e: PhonePeInitException) {
            }

        }


        Log.e("memberid", AdapterFamilyListView.memberID)

        if (sessionManager.bookingType.toString() == "1" && PrescriptionDetails.FollowUP == "") {
            binding.cardCashOnDelivery.visibility = View.GONE
            binding.cardFreeOfCost.visibility = View.GONE

        }

        binding.imgBack.setOnClickListener {
            AdapterFamilyListView.memberID = ""
            onBackPressed()
        }

        doctorId = intent.getStringExtra("doctorId").toString()
        selectedate = intent.getStringExtra("selecteDate").toString()
        startTime = intent.getStringExtra("startTime").toString()
        slotId = intent.getStringExtra("slotId").toString()
        title = intent.getStringExtra("title").toString()
        description = intent.getStringExtra("description").toString()

        Log.e("doctorId", doctorId)
        Log.e("selectedate", selectedate)
        Log.e("startTime", startTime)
        Log.e("title", title)
        Log.e("description", description)
        binding.cardFreeOfCost.setOnClickListener {
            when (sessionManager.bookingType) {
                "1" -> {
                    apiCallCreateBookingOnlineConsultOnlineFree()
                }
                "2" -> {
                    apiCallCreateBookingAppointmentOnlineFree()
                }
                else -> {
                    apiCallCreateBookingHomeVisitOnlineFree()
                }
            }
        }
        if (PrescriptionDetails.FollowUP == "1") {
            binding.cardFreeOfCost.visibility = View.VISIBLE
            binding.cardRazorPay.visibility = View.GONE
            binding.cardCashOnDelivery.visibility = View.GONE


        }
/*        razorpay?.getPaymentMethods(object : PaymentMethodsCallback {
            override fun onPaymentMethodsReceived(result: String?) {
                */
        /**
         * This returns JSON data
         * The structure of this data can be seen at the following link:
         * https://api.razorpay.com/v1/methods?key_id=rzp_test_1DP5mmOlF5G5ag
         *
         *//*
                Log.d("Result", "" + result)
               // inflateLists(result)
            }

            override fun onError(error: String?) {
                if (error != null) {
                    Log.e("Get Payment error", error)
                }
            }
        })*/
        binding.cardCashOnDelivery.setOnClickListener {
            SweetAlertDialog(this@PaymentMode, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to Confirm?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    when (sessionManager.bookingType) {
                        "1" -> {
                            apiCallCreateBookingOnlineConsultOnline()
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
            startPaymentOnlineRazorPay()
            // createWebView()

//            val checkout = Checkout()
//                .upiTurbo(this@PaymentMode) // mandatory
//                .setColor("/*color*/ #000000") // optional


//            val intent = Intent(context as Activity, RazorPay::class.java)
//                .putExtra("doctorId",doctorId)
//                .putExtra("selecteDate",selectedate)
//                .putExtra("startTime",startTime)
//                .putExtra("slotId",slotId)
//            context.startActivity(intent)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            Log.e("Data",data.toString())
            checkStatus()

           // myToast(this@PaymentMode, "onActivityResult")
        }
    }
    private fun checkStatus() {
        val xVerify = sha256("/pg/v1/status/$MERCHANT_ID/${MERCHANT_TID}${salt}") + "###1"

        Log.d("phonepe", "xverify : $xVerify")

        val headers = mapOf(
            "Content-Type" to "application/json",
            "X-VERIFY" to xVerify,
            "X-MERCHANT-ID" to MERCHANT_ID,
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getApiInterface().checkStatus(MERCHANT_ID, MERCHANT_TID, headers)
            withContext(Dispatchers.Main) {
                Log.d("phonepe", "APIResponse${res.body()}")

                if (res.body() != null && res.body()!!.success) {
                    Log.d("phonepe", "onCreate: success")
                    Toast.makeText(this@PaymentMode, res.body()!!.message, Toast.LENGTH_SHORT).show()
                    if (res.body()!!.code=="PAYMENT_SUCCESS") {
                        when (sessionManager.bookingType) {
                            "1" -> {
                                apiCallCreateBookingOnlineConsultOnline()
                            }
                            "2" -> {
                                apiCallCreateBookingAppointmentOnline()
                            }
                            else -> {
                                apiCallCreateBookingHomeVisitOnline()
                            }
                        }
                    }

                }
            }
        }


    }

    private fun sha256(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }


    private fun apiCallCreateBookingOnlineConsultOnline() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount = "1000"
        paymentMode = "2"

        ApiClient.apiService.createConsultation(
            sessionManager.id.toString(),
            doctorId,
            sessionManager.pricing,
            paymentMode,
            sessionManager.bookingType,
            selectedate,
            startTime,
            slotId,
            AdapterFamilyListView.memberID
        )
            .enqueue(object : Callback<ModelCreateConsultation> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>, response: Response<ModelCreateConsultation>
                ) {
                    if (response.code() == 500) {
                        myToast(this@PaymentMode, "Server Error")
                    } else if (response.code() == 200) {
                        popUpConsultOnline()
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {

                }


            })
    }

    private fun apiCallCreateBookingOnlineConsultOnlineFree() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount = "1000"
        paymentMode = "5"

        ApiClient.apiService.createConsultation(
            sessionManager.id.toString(),
            doctorId,
            sessionManager.pricing,
            paymentMode,
            sessionManager.bookingType,
            selectedate,
            startTime,
            slotId,
            PrescriptionDetails.FollowUPMemberName
        )
            .enqueue(object : Callback<ModelCreateConsultation> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>, response: Response<ModelCreateConsultation>
                ) {
                    if (response.code() == 500) {
                        myToast(this@PaymentMode, "Server Error")
                    } else if (response.code() == 200) {
                        popUpConsultOnline()
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {

                }


            })
    }

    private fun apiCallCreateBookingAppointmentOnline() {

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount = "1000"
        paymentMode = "2"

        ApiClient.apiService.createConsultation(
            sessionManager.id.toString(),
            doctorId,
            sessionManager.pricing,
            paymentMode,
            sessionManager.bookingType,
            selectedate,
            startTime,
            slotId,
            AdapterFamilyListView.memberID
        )
            .enqueue(object : Callback<ModelCreateConsultation> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>,
                    response: Response<ModelCreateConsultation>
                ) {
                    if (response.body()!!.status == 1) {
                        popUpOnline()
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {

                }


            })
    }

    private fun apiCallCreateBookingAppointmentOnlineFree() {

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount = "1000"
        paymentMode = "5"

        ApiClient.apiService.createConsultation(
            sessionManager.id.toString(),
            doctorId,
            sessionManager.pricing,
            paymentMode,
            sessionManager.bookingType,
            selectedate,
            startTime,
            slotId,
            PrescriptionDetails.FollowUPMemberName
        )
            .enqueue(object : Callback<ModelCreateConsultation> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>,
                    response: Response<ModelCreateConsultation>
                ) {
                    if (response.body()!!.status == 1) {
                        popUpOnline()
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {

                }


            })
    }

    /*
        private fun apiCallCreateBookingAppointment(){

            progressDialog = ProgressDialog(this)
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
            val amount="1000"
            val paymentMode="2"

            ApiClient.apiService.createBooking(sessionManager.id.toString(),doctorId,startTime,title,description,amount,paymentMode)
                .enqueue(object : Callback<ModelCreateBooking>
                {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<ModelCreateBooking>,
                        response: Response<ModelCreateBooking>
                    )
                    {
                        if (response.body()!!.status==1){
                            popUp()
                            progressDialog!!.dismiss()
                        }else{
                            myToast(this@RazorPay,response.body()!!.message)
                            progressDialog!!.dismiss()
                        }

                    }

                    override fun onFailure(call: Call<ModelCreateBooking>, t: Throwable) {

                    }


                })
        }
    */
    private fun apiCallCreateBookingHomeVisitOnline() {

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount = "1000"
        val paymentMode = "2"

        ApiClient.apiService.createConsultation(
            sessionManager.id.toString(),
            doctorId,
            sessionManager.pricing,
            paymentMode,
            sessionManager.bookingType,
            selectedate,
            startTime,
            slotId,
            AdapterFamilyListView.memberID
        )
            .enqueue(object : Callback<ModelCreateConsultation> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>,
                    response: Response<ModelCreateConsultation>
                ) {
                    if (response.body()!!.status == 1) {
                        popUpHomeVisitOnline()
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {

                }


            })
    }

    private fun apiCallCreateBookingHomeVisitOnlineFree() {

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount = "1000"
        val paymentMode = "5"

        ApiClient.apiService.createConsultation(
            sessionManager.id.toString(),
            doctorId,
            sessionManager.pricing,
            paymentMode,
            sessionManager.bookingType,
            selectedate,
            startTime,
            slotId,
            PrescriptionDetails.FollowUPMemberName
        )
            .enqueue(object : Callback<ModelCreateConsultation> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>,
                    response: Response<ModelCreateConsultation>
                ) {
                    if (response.body()!!.status == 1) {
                        popUpHomeVisitOnline()
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {

                }


            })
    }

    private fun popUpOnline() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your Booking Confirmed")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                finish()
                startActivity(intent)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }

    private fun popUpConsultOnline() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your Booking Confirmed")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                finish()
                startActivity(intent)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }

    private fun popUpHomeVisitOnline() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your Booking Confirmed")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                finish()
                startActivity(intent)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }

    private fun startPaymentOnlineRazorPay() {
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "EHCF")
            options.put("description", "Payment Description")
            //You can omit the image option to fetch the image from the dashboard
            options.put(
                "image",
                "https://ehcf.thedemostore.in/uploads/prescriptions/1689586754.png"
            )
            options.put("theme.color", "#9F367A");
            options.put("currency", "INR");
            options.put(
                "amount", sessionManager.pricing!!.toInt() * 100
            )//pass amount in currency subunits
            val prefill = JSONObject()
            prefill.put("email", "alauddinansari7379@gmail.com")
            prefill.put("contact", "7379452259")
            options.put("prefill", prefill)
            co.open(this, options)

        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful: ", Toast.LENGTH_LONG).show()
        when (sessionManager.bookingType) {
            "1" -> {
                apiCallCreateBookingOnlineConsultOnline()
            }
            "2" -> {
                apiCallCreateBookingAppointmentOnline()
            }
            else -> {
                apiCallCreateBookingHomeVisitOnline()
            }
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Field ", Toast.LENGTH_LONG).show()

//        val intent = Intent(applicationContext, MySlot::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        finish()
//        startActivity(intent)

    }

    private fun apiCallCreateBookingAppointment() {

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount = "1000"
        val paymentMode = "1"

        ApiClient.apiService.createConsultation(
            sessionManager.id.toString(),
            doctorId,
            sessionManager.pricing,
            paymentMode,
            sessionManager.bookingType,
            selectedate,
            startTime,
            slotId,
            AdapterFamilyListView.memberID
        )
            .enqueue(object : Callback<ModelCreateConsultation> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>,
                    response: Response<ModelCreateConsultation>
                ) {
                    if (response.code() == 500) {
                        myToast(this@PaymentMode, "Server Error")
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.status == 1) {
                        popUp()
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    t.message?.let { myToast(this@PaymentMode, it) }
                    progressDialog!!.dismiss()
                }


            })
    }

    private fun apiCallCreateBookingHomeVisit() {

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val amount = "1000"
        val paymentMode = "1"

        ApiClient.apiService.createConsultation(
            sessionManager.id.toString(),
            doctorId,
            sessionManager.pricing,
            paymentMode,
            sessionManager.bookingType,
            selectedate,
            startTime,
            slotId,
            AdapterFamilyListView.memberID
        )
            .enqueue(object : Callback<ModelCreateConsultation> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelCreateConsultation>,
                    response: Response<ModelCreateConsultation>
                ) {
                    if (response.body()!!.status == 1) {
                        popUp()
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    t.message?.let { myToast(this@PaymentMode, it) }
                    progressDialog!!.dismiss()
                }


            })
    }

    private fun popUp() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your Booking Confirmed")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                finish()
                startActivity(intent)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }

    private fun popUpConfirm(startTime: String, id: String) {

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