package com.example.ehcf

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.Fragment.MainActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.OnlineDoctor.model.ModelCreateConsultation
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.databinding.ActivityPaymentModeBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.razorpay.Checkout
 import com.razorpay.PaymentResultListener
 import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

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
   // private val razorpay = Razorpay(this@PaymentMode, "rzp_test_tnOp3sjGL6gTju")

    private lateinit var binding: ActivityPaymentModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)





        Log.e("memberid", AdapterFamilyListView.memberID)

        if (sessionManager.bookingType.toString() == "1" && PrescriptionDetails.FollowUP == "") {
            binding.cardCashOnDelivery.visibility = View.GONE
            binding.cardFreeOfCost.visibility=View.GONE

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
            binding.cardFreeOfCost.visibility=View.VISIBLE
            binding.cardRazorPay.visibility=View.GONE
            binding.cardCashOnDelivery.visibility=View.GONE


        }
//        razorpay?.getPaymentMethods(object : PaymentMethodsCallback {
//            override fun onPaymentMethodsReceived(result: String?) {
//                /**
//                 * This returns JSON data
//                 * The structure of this data can be seen at the following link:
//                 * https://api.razorpay.com/v1/methods?key_id=rzp_test_1DP5mmOlF5G5ag
//                 *
//                 */
//                Log.d("Result", "" + result)
//               // inflateLists(result)
//            }
//
//            override fun onError(error: String?) {
//                if (error != null) {
//                    Log.e("Get Payment error", error)
//                }
//            }
//        })
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
            startPaymentOnline()
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
//     private fun createWebView() {
//        razorpay?.setWebView(binding.web)
//    }
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

    private fun startPaymentOnline() {
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "EHCF")
            options.put("description", "Payment Description")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", "https://ehcf.thedemostore.in/uploads/prescriptions/1689586754.png")
            options.put("theme.color", "#9F367A");
            options.put("currency", "INR");
            options.put("amount", sessionManager.pricing!!.toInt() * 100)//pass amount in currency subunits
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