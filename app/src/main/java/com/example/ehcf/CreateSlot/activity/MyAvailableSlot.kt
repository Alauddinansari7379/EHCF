package com.example.ehcf.CreateSlot.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.CreateSlot.Adapter.AdapterShuduleTimingNew
import com.example.ehcf.CreateSlot.model.ModelSlotResNew
import com.example.ehcf.Helper.myToast
import com.example.ehcf.PaymentMode
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityShuduleTimingBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MyAvailableSlot : AppCompatActivity(), AdapterShuduleTimingNew.dilog {
    private val context: Context = this@MyAvailableSlot
    var progressDialog: ProgressDialog? = null
    var mydilaog: Dialog? = null
    private var selectedate = ""
    private var dayCode = ""
    var day = ""
    var startTime = ""
    var doctorId = ""

    // private var arrayList = ModelSlotResNew();
    var dialog: Dialog? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityShuduleTimingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShuduleTimingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        binding.imgBack.setOnClickListener {
            onBackPressed()
            //startActivity(Intent(this, MainActivity::class.java))
        }
        selectedate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val selectedDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        sessionManager.selectedDate=selectedDate1

        dayCode = SimpleDateFormat("E", Locale.getDefault()).format(Date())
        when (dayCode) {
            "Mon" -> {
                dayCode = "1"
            }
            "Tue" -> {
                dayCode = "2"
            }
            "Wed" -> {
                dayCode = "3"
            }
            "Thu" -> {
                dayCode = "4"
            }
            "Fri" -> {
                dayCode = "5"
            }
            "Sat" -> {
                dayCode = "6"
            }
            "Sun" -> {
                dayCode = "7"
            }
        }
        Log.e("dayCodeNEw", dayCode)

        binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        // binding.tvDateTotalPatients.text=currentDate


        doctorId = intent.getStringExtra("doctorId").toString()
        Log.e("DoctorId", doctorId)
        Log.e("startTimeNew", startTime)


        apiCall()

//        Handler().postDelayed({
//        apiCall()
//        }, 500)


        val view = layoutInflater.inflate(R.layout.book_dialog, null)

        val btnBookNow = view.findViewById<Button>(R.id.btnBookNowDilog)
        //  tvTimeCounter = view.findViewById<TextView>(R.id.tvTimeCounter)


        binding.btnAppointmentTime1.setOnClickListener {
            dialog = Dialog(this)
            val btnOkDialog = view.findViewById<Button>(R.id.btnBookNowDilog)
            if (view.parent != null) {
                (view.parent as ViewGroup).removeView(view) // <- fix
            }
            dialog!!.setContentView(view)
            // dialog?.setCancelable(false)
            // dialog?.setContentView(view)

            dialog?.show()
        }
        btnBookNow.setOnClickListener {
            dialog?.dismiss()
            //  startActivity(Intent(this, PaymentMode::class.java))
            startActivity(Intent(this, PaymentMode::class.java))

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
                // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                selectedate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate.time)
                val selectedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                sessionManager.selectedDate=selectedDate
                dayCode = SimpleDateFormat("E", Locale.getDefault()).format(newDate.time)

                Log.e(ContentValues.TAG, "dayCode: >>>>>>$dayCode")
                when (dayCode) {
                    "Mon" -> {
                        dayCode = "1"
                    }
                    "Tue" -> {
                        dayCode = "2"
                    }
                    "Wed" -> {
                        dayCode = "3"
                    }
                    "Thu" -> {
                        dayCode = "4"
                    }
                    "Fri" -> {
                        dayCode = "5"
                    }
                    "Sat" -> {
                        dayCode = "6"
                    }
                    "Sun" -> {
                        dayCode = "7"
                    }
                }

                binding.tvDate.text = selectedate
                apiCall()
                Log.e(ContentValues.TAG, "selectedate:>>$selectedate")
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;

        binding.cardSelectDate.setOnClickListener {
            datePicker.show()
        }
    }
//    override fun onBackPressed() {
//        super.onBackPressed()
//        sessionManager.bookingType = null
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        sessionManager.bookingType = null
//
//    }
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

    private fun apiCall() {

        progressDialog = ProgressDialog(this@MyAvailableSlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        Log.e(ContentValues.TAG, "dayCode:>>$dayCode")
        Log.e(ContentValues.TAG, "selectedate:>>$selectedate")


        ApiClient.apiService.getTimeSlot(doctorId, dayCode, selectedate,sessionManager.bookingType)
            .enqueue(object : Callback<ModelSlotResNew> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelSlotResNew>,
                    response: Response<ModelSlotResNew>
                ) {
                    // binding.rvSlotTiming.invalidate();
                    if (response.body()!!.status==0){
                        myToast(this@MyAvailableSlot, "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }
                    else if (response.code()==500){
                        myToast(this@MyAvailableSlot, "Server Error")
                    }
                   else if (response.body()!!.result.isEmpty()) {
                        binding.rvSlotTiming.apply {
                            adapter = AdapterShuduleTimingNew(this@MyAvailableSlot, response.body()!!, this@MyAvailableSlot)
                            progressDialog!!.dismiss()
                            myToast(this@MyAvailableSlot, "No Slot Found")
                            progressDialog!!.dismiss()
                        }
                    } else {
                        binding.rvSlotTiming.apply {
                            //   adapter!!.notifyDataSetChanged();
                            //myToast(this@ShuduleTiming, response.body()!!.message)
                            adapter =
                                AdapterShuduleTimingNew(this@MyAvailableSlot, response.body()!!, this@MyAvailableSlot)

                            progressDialog!!.dismiss()
                        }

                    }
                }


                override fun onFailure(call: Call<ModelSlotResNew>, t: Throwable) {
                    progressDialog!!.dismiss()
                    myToast(this@MyAvailableSlot, "Something went wrong")
                }


            })
    }


    @SuppressLint("MissingInflatedId")
    override fun showPopup(slotTimeData: String, slotTimeValue: String, slotId: String) {
        val view = layoutInflater.inflate(R.layout.book_dialog, null)
        dialog = Dialog(this)
        val btnBookNowDilog = view.findViewById<Button>(R.id.btnBookNowDilog)
        val slotTime = view.findViewById<TextView>(R.id.tvSlotTime)
        val slotDate = view.findViewById<TextView>(R.id.tvSlotDate)
        val price = view.findViewById<TextView>(R.id.tvPrice)
        price.text=sessionManager.pricing
        startTime = slotTimeValue
        Log.e("startTimeNew", startTime)

        slotTime.text = slotTimeData
        slotDate.text = selectedate
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        // dialog?.setCancelable(false)
        // dialog?.setContentView(view)

        dialog?.show()
        btnBookNowDilog.setOnClickListener {
            val intent = Intent(context as Activity, PaymentMode::class.java)
                .putExtra("doctorId", doctorId)
                .putExtra("selecteDate", selectedate)
                .putExtra("startTime", startTime)
                .putExtra("slotId", slotId)
            context.startActivity(intent)
        }
    }

}