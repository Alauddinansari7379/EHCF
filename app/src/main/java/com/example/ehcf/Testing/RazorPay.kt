package com.example.ehcf.Testing

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Appointments.Appointments
import com.example.ehcf.CreateSlot.activity.ShuduleTiming
import com.example.ehcf.CreateSlot.model.ModelCreateBooking
import com.example.ehcf.Helper.myToast
import com.example.ehcf.databinding.ActivityRazorPayBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RazorPay : AppCompatActivity(), PaymentResultListener {
    private val context: Context = this@RazorPay
    private lateinit var sessionManager: SessionManager

    var progressDialog: ProgressDialog? = null
    private lateinit var binding: ActivityRazorPayBinding
    var amt = 1000
    var doctorId = ""
    var selectedate = ""
    var startTime = ""
    var title = ""
    var description = ""
    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRazorPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        startPayment()

        doctorId = intent.getStringExtra("doctorId").toString()
        selectedate = intent.getStringExtra("selecteDate").toString()
        startTime = intent.getStringExtra("startTime").toString()
        title = intent.getStringExtra("title").toString()
        description = intent.getStringExtra("description").toString()

        Log.e("doctorId","$doctorId")
        Log.e("selectedate","$selectedate")
        Log.e("startTime","$startTime")
        Log.e("title","$title")
        Log.e("description","$description")


        // startPayment()

        binding.btnPayment.setOnClickListener {
            if (binding.edtEnterAmount.text.isEmpty()) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show()
            } else {
                amt = binding.edtEnterAmount.text.toString().toInt()
                startPayment()
            }
        }

    }
    private fun apiCallCreateBooking(){

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

    private fun startPayment() {
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "Alauddin Ansari")
            options.put("description", "Payment Description")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amt * 100)//pass amount in currency subunits
            val prefill = JSONObject()
            prefill.put("email", "alauddinansari7379@gmail.com.com")
            prefill.put("contact", "7379452259")
            options.put("prefill", prefill)
            co.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        apiCallCreateBooking()
        Toast.makeText(this, "Payment Successful: ", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Field ", Toast.LENGTH_LONG).show()
        val intent = Intent(applicationContext, ShuduleTiming::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)

    }

}