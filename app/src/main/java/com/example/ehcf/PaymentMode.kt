package com.example.ehcf

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView.Companion.memberID
import com.example.ehcf.Fragment.MainActivity
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OnlineDoctor.model.ModelCreateConsultation
import com.example.ehcf.Pharmacy.activity.BrowseMedicine
import com.example.ehcf.Pharmacy.model.ModelOrder
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.databinding.ActivityPaymentModeBinding
import com.example.ehcf.phonepesdk.ApiUtilities
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
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
    private val context = this@PaymentMode
    private var doctorId = ""
    private lateinit var sessionManager: SessionManager
    var selectedate = ""
    var startTime = ""
    var slotId = ""
    var amt = 1000
    var countN = 0
    var countN1 = 0
    var countN2 = 0
    var countN3 = 0
    var countN4 = 0
    var countN5 = 0
    var countN6 = 0
    var countN7 = 0
    var countN8 = 0
    var title = ""
    var paymentMode = ""
    var description = ""
    var medicine = ""
    var slug = ""
    var addressId = ""


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
        medicine = intent.getStringExtra("Medicine").toString()
        slug = intent.getStringExtra("slug").toString()
        addressId = intent.getStringExtra("addressId").toString()
        Log.d("TAGNEw", "err")

        PhonePe.init(this@PaymentMode)
        try {
            val upiApps = PhonePe.getUpiApps()
            Log.e("UPIAPPS", upiApps.toString())
        } catch (exception: PhonePeInitException) {
            exception.printStackTrace();
        }

        val data = JSONObject()
        data.put("merchantTransactionId", MERCHANT_TID)//String. Mandatory

        data.put("merchantId", MERCHANT_ID) //String. Mandatory
        if (PrescriptionDetails.FollowUP == "1") {
            binding.cardFreeOfCost.visibility = View.VISIBLE
            binding.cardRazorPay.visibility = View.GONE
            binding.cardCashOnDelivery.visibility = View.GONE
            binding.cardPhonePay.visibility = View.GONE


        }
        if (sessionManager.pricing == "Free") {
            sessionManager.pricing = "0".toInt().toString()
        }
        if (sessionManager.pricing!!.isEmpty()) {
            sessionManager.pricing = "0".toInt().toString()
        }
        if (sessionManager.pricing != "Free") {
            data.put("amount", sessionManager.pricing!!.toInt() * 100)//Long. Mandatory

            data.put("mobileNumber", "7908834635") //String. Optional
            data.put(
                "callbackUrl",
                "https://webhook.site/6658f3da-60a4-440f-a743-27e3dcdb91a8"
            ) //String. Mandatory

            val paymentInstrument = JSONObject()
            paymentInstrument.put("type", "PAY_PAGE")
            //  paymentInstrument.put("targetApp", "com.phonepe.simulator")

            data.put("paymentInstrument", paymentInstrument)//OBJECT. Mandatory


            val deviceContext = JSONObject()
            deviceContext.put("deviceOS", "ANDROID")
            data.put("deviceContext", deviceContext)


            val payloadBase64 = Base64.encodeToString(
                data.toString().toByteArray(Charset.defaultCharset()), Base64.NO_WRAP
            )
            val checksum = sha256(payloadBase64 + apiEndPoint + salt) + "###1"

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
                    if (medicine == "1") {
                        apiCallCreateOrderMedicine("1")

                    } else {
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
            Log.e("Data", data.toString())
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
                    Toast.makeText(this@PaymentMode, res.body()!!.message, Toast.LENGTH_SHORT)
                        .show()
                    if (res.body()!!.code == "PAYMENT_SUCCESS") {
                        if (medicine == "1") {
                            apiCallCreateOrderMedicine("2")
                        } else {
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


    }

    private fun sha256(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }


    private fun apiCallCreateBookingOnlineConsultOnline() {
        AppProgressBar.showLoaderDialog(context)
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
                    try {
                        if (response.code() == 500) {
                            myToast(this@PaymentMode, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
                            popUpConsultOnline()
                            countN = 0
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            myToast(this@PaymentMode, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    countN++
                    if (countN <= 3) {
                        apiCallCreateBookingOnlineConsultOnline()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun apiCallCreateOrderMedicine(paymentMode: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.createOrder(
            sessionManager.id.toString(), sessionManager.pricing!!,
            paymentMode,
            addressId, slug, memberID

        )
            .enqueue(object : Callback<ModelOrder> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelOrder>, response: Response<ModelOrder>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@PaymentMode, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(this@PaymentMode, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.body()!!.message.contains("Order placed successfully")) {
                            myToast(this@PaymentMode, response.body()!!.message)
                            BrowseMedicine.cartQty = "0"
                            addressId = ""
                            popUpConsultOnline()
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            myToast(this@PaymentMode, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                        }

                    } catch (e: Exception) {
                        myToast(this@PaymentMode, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelOrder>, t: Throwable) {
                    countN1++
                    if (countN1 <= 3) {
                        apiCallCreateOrderMedicine(paymentMode)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun apiCallCreateBookingOnlineConsultOnlineFree() {
        AppProgressBar.showLoaderDialog(context)
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
                        countN2 = 0
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    countN2++
                    if (countN2 <= 3) {
                        apiCallCreateBookingOnlineConsultOnlineFree()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun apiCallCreateBookingAppointmentOnline() {

        AppProgressBar.showLoaderDialog(context)
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
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    countN3++
                    if (countN3 <= 3) {
                        apiCallCreateBookingAppointmentOnline()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun apiCallCreateBookingAppointmentOnlineFree() {

        AppProgressBar.showLoaderDialog(context)
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
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    countN4++
                    if (countN4 <= 3) {
                        apiCallCreateBookingAppointmentOnlineFree()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun apiCallCreateBookingHomeVisitOnline() {

        AppProgressBar.showLoaderDialog(context)
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
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    countN5++
                    if (countN5 <= 3) {
                        apiCallCreateBookingHomeVisitOnline()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun apiCallCreateBookingHomeVisitOnlineFree() {

        AppProgressBar.showLoaderDialog(context)
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
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    countN6++
                    if (countN6 <= 3) {
                        apiCallCreateBookingHomeVisitOnlineFree()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun popUpOnline() {
        val pDialog = SweetAlertDialog(
            context,
            SweetAlertDialog.SUCCESS_TYPE
        )
        pDialog.setTitleText("Your Booking Confirmed")
        pDialog.setConfirmText("Ok")
        pDialog.showCancelButton(true)
        pDialog.setConfirmClickListener { sDialog ->
            sDialog.cancel()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            finish()
            startActivity(intent)
        }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
        pDialog.setCancelable(false)
        pDialog.show()


    }

    private fun popUpConsultOnline() {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.setTitleText("Your Booking Confirmed")
        pDialog.setConfirmText("Ok")
        pDialog.showCancelButton(true)
        pDialog.setConfirmClickListener { sDialog ->
            sDialog.cancel()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            finish()
            startActivity(intent)
        }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
        pDialog.setCancelable(false)
        pDialog.show()

    }

    private fun popUpHomeVisitOnline() {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.setTitleText("Your Booking Confirmed")
        pDialog.setConfirmText("Ok")
        pDialog.showCancelButton(true)
        pDialog.setConfirmClickListener { sDialog ->
            sDialog.cancel()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            finish()
            startActivity(intent)
        }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
        pDialog.setCancelable(false)
        pDialog.show()

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
                "${sessionManager.imageurl}prescriptions/1689586754.png"
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
        if (medicine == "1") {
            apiCallCreateOrderMedicine("2")
        } else
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
    }

    private fun apiCallCreateBookingAppointment() {

        AppProgressBar.showLoaderDialog(context)
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
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.body()!!.status == 1) {
                        popUp()
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    countN7++
                    if (countN7 <= 3) {
                        apiCallCreateBookingAppointment()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun apiCallCreateBookingHomeVisit() {
        AppProgressBar.showLoaderDialog(context)
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
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@PaymentMode, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelCreateConsultation>, t: Throwable) {
                    countN8++
                    if (countN8 <= 3) {
                        apiCallCreateBookingAppointment()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private fun popUp() {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.setTitleText("Your Booking Confirmed")
        pDialog.setConfirmText("Ok")
        pDialog.showCancelButton(true)
        pDialog.setConfirmClickListener { sDialog ->
            sDialog.cancel()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            finish()
            startActivity(intent)
        }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
        pDialog.setCancelable(false)
        pDialog.show()

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