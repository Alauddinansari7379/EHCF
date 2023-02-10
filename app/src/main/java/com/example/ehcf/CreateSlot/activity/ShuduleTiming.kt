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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ShuduleTiming : AppCompatActivity(),AdapterShuduleTimingNew.dilog {
    private val context: Context = this@ShuduleTiming
    var progressDialog: ProgressDialog? = null
    var mydilaog: Dialog? = null
    var selectedate = ""
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
                binding.tvDate.text = selectedate
                apiCall()
                Log.e(ContentValues.TAG, "selectedate: >>>>>>>>>>>>>>>>>>>>>>$selectedate")
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;

        binding.tvSelectDate.setOnClickListener {
            datePicker.show()
        }
    }

    private fun apiCall() {

        progressDialog = ProgressDialog(this@ShuduleTiming)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val doctorid = "54"
        val date = "2023-02-04"
        doctorId
        ApiClient.apiService.getTimeSlot(doctorId, selectedate).enqueue(object :Callback<ModelSlotResNew>
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelSlotResNew>,
                response: Response<ModelSlotResNew>
            ) {
               // binding.rvSlotTiming.invalidate();
                if (response.body()!!.result.isEmpty()) {
                    binding.rvSlotTiming.apply {
                        adapter = AdapterShuduleTimingNew(this@ShuduleTiming, response.body()!!, this@ShuduleTiming)
                        progressDialog!!.dismiss()
                        myToast(this@ShuduleTiming, "No Slot Found")
                        progressDialog!!.dismiss()
                    }
                } else {
                    binding.rvSlotTiming.apply {
                     //   adapter!!.notifyDataSetChanged();
                        //myToast(this@ShuduleTiming, response.body()!!.message)
                        adapter = AdapterShuduleTimingNew(this@ShuduleTiming, response.body()!!, this@ShuduleTiming)
                        progressDialog!!.dismiss()
                    }

                }
            }


            override fun onFailure(call: Call<ModelSlotResNew>, t: Throwable) {

            }


        })
    }


    override fun showPopup(slotTimeData:String,slotTimeValue:String)  {
        val view = layoutInflater.inflate(R.layout.book_dialog, null)
        dialog = Dialog(this)
        val btnBookNowDilog = view.findViewById<Button>(R.id.btnBookNowDilog)
        val slotTime = view.findViewById<TextView>(R.id.tvSlotTime)
        val slotDate = view.findViewById<TextView>(R.id.tvSlotDate)
        startTime= slotTimeValue
        Log.e("startTimeNew", startTime)

        slotTime.text=slotTimeData
        slotDate.text=selectedate
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        // dialog?.setCancelable(false)
        // dialog?.setContentView(view)

        dialog?.show()
        btnBookNowDilog.setOnClickListener {
            val intent = Intent(context as Activity, BookingSlot::class.java)
                .putExtra("doctorId",doctorId)
                .putExtra("selecteDate",selectedate)
                .putExtra("startTime",startTime)
            context.startActivity(intent)
        }
    }

}