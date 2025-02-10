package com.example.ehcf.CreateSlot.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.CreateSlot.Adapter.AdapterShuduleTimingNew
import com.example.ehcf.CreateSlot.model.ModelSlotResNew
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.FamailyMember.Model.ModelFamilyListJava
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.Util
import com.example.ehcf.Helper.changeDateFormat5
import com.example.ehcf.Helper.myToast
import com.example.ehcf.PaymentMode
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityShuduleTimingBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Helper.DatePickerDialogWithMaxMinRange
import com.example.ehcf.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MyAvailableSlot : AppCompatActivity(), AdapterShuduleTimingNew.dilog,
    AdapterFamilyListView.CheckBox {
    private val context: Context = this@MyAvailableSlot
    var mydilaog: Dialog? = null
    private var selectedate = ""
    private var dayCode = ""
    private var calendar: Calendar? = null
    private var familyList = ModelFamilyListJava()

    var day = ""
    var startTime = ""
    var doctorId = ""
    var online = ""
    var count = 0
    var countN = 0
    var countR = 0
    var countR1 = 0

    // private var arrayList = ModelSlotResNew();
    var dialog: Dialog? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityShuduleTimingBinding
    private var maxDate: String? = null
    private var minDate: String? = null
    private var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShuduleTimingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        calendar = Calendar.getInstance()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        // apiCallFamilyList()
        selectedate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val selectedDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        sessionManager.selectedDate = selectedDate1
        apiCallFamilyListNew()


        // currentDate =

        if (PrescriptionDetails.FollowUP == "1") {
            binding.cardSelectDateFollowUp.visibility = View.VISIBLE
            binding.cardSelectDate.visibility = View.GONE
            binding.layoutFamilyMemeber.visibility = View.GONE
        } else {
            binding.layoutFamilyMemeber.visibility = View.VISIBLE
        }


        binding.cardSelectDateFollowUp.setOnClickListener {
            maxDate = PrescriptionDetails.Maxdate
            minDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            openDatePickerWithMaxAndMindate(minDate!!, maxDate!!)
        }
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
        online = intent.getStringExtra("Online").toString()

        if (online == "1") {
            apiCallOnlineSlot()
            binding.cardSelectDate.visibility = View.GONE
            binding.appCompatTextView2.text = "Today's Available Slots"
        } else {
            apiCall(selectedate)

        }

        Log.e("DoctorId", doctorId)
        Log.e("startTimeNew", startTime)

        val view = layoutInflater.inflate(R.layout.book_dialog, null)

        val btnBookNow = view.findViewById<Button>(R.id.btnBookNowDilog)

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
                selectedate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)
                val selectedDate =
                    SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                sessionManager.selectedDate = selectedDate
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

                //  binding.tvDate.text = selectedate
                apiCall(selectedate)
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

    private var datePickerListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar?.let {
                it[Calendar.YEAR] = year
                it[Calendar.MONTH] = monthOfYear
                it[Calendar.DAY_OF_MONTH] = dayOfMonth
            }
            binding.tvDate!!.text = Util.getDate(calendar!!.time)
            dayCode = SimpleDateFormat("E", Locale.getDefault()).format(calendar!!.time)

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
            val selectedDatenew = changeDateFormat5(binding.tvDate!!.text.toString())
            apiCall(selectedDatenew)
        }

    private fun openDatePickerWithMaxAndMindate(minDate: String, maxDate: String) {

        try {
            val minYear: Int
            val minMonth: Int
            val minDay: Int
            val maxYear: Int
            val maxMonth: Int
            val maxDay: Int

            minDay = minDate.split("-").toTypedArray()[0].toInt()
            minMonth = minDate.split("-").toTypedArray()[1].toInt() - 1
            minYear = minDate.split("-").toTypedArray()[2].toInt()
            maxDay = maxDate.split("-").toTypedArray()[0].toInt()
            maxMonth = maxDate.split("-").toTypedArray()[1].toInt() - 1
            maxYear = maxDate.split("-").toTypedArray()[2].toInt()

            val maxDateCalendar = Calendar.getInstance().also {
                it[Calendar.YEAR] = maxYear
                it[Calendar.MONTH] = maxMonth
                it[Calendar.DAY_OF_MONTH] = maxDay
            }

            val minDateCalendar = Calendar.getInstance().also {
                it[Calendar.YEAR] = minYear
                it[Calendar.MONTH] = minMonth
                it[Calendar.DAY_OF_MONTH] = minDay
            }

            if (binding.tvDate!!.text.toString()
                    .equals(getString(R.string.lbl_select_date), ignoreCase = true)
            ) {
                calendar = Calendar.getInstance()
                Log.e("dfd1", binding.tvDate!!.text.toString())

            } else {
                val selectedDate = binding.tvDate!!.text.toString()
                Log.e("dfd2", binding.tvDate!!.text.toString())

                calendar?.let {
                    it[Calendar.DAY_OF_MONTH] = selectedDate.split("-").toTypedArray()[0].toInt()
                    it[Calendar.MONTH] = selectedDate.split("-").toTypedArray()[1].toInt() - 1
                    it[Calendar.YEAR] = selectedDate.split("-").toTypedArray()[2].toInt()

                }

            }



            if (minDateCalendar.after(calendar)) {
                DatePickerDialogWithMaxMinRange(
                    context,
                    datePickerListener,
                    minDateCalendar,
                    maxDateCalendar,
                    minDateCalendar
                ).show()

            } else if (maxDateCalendar.before(calendar)) {
                DatePickerDialogWithMaxMinRange(
                    context,
                    datePickerListener,
                    minDateCalendar,
                    maxDateCalendar,
                    maxDateCalendar
                ).show()
            } else {
                DatePickerDialogWithMaxMinRange(
                    context,
                    datePickerListener,
                    minDateCalendar,
                    maxDateCalendar,
                    calendar!!
                ).show()
            }

        } catch (e: Throwable) {
            // Have suppressed the exception
            e.printStackTrace()
        }
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

    private fun apiCall(selectedate: String) {

        AppProgressBar.showLoaderDialog(context)

        Log.e(ContentValues.TAG, "dayCode:>>$dayCode")
        Log.e(ContentValues.TAG, "selectedate:>>${this.selectedate}")


        ApiClient.apiService.getTimeSlot(doctorId, dayCode, selectedate, sessionManager.bookingType)
            .enqueue(object : Callback<ModelSlotResNew> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelSlotResNew>,
                    response: Response<ModelSlotResNew>
                ) {
                    // binding.rvSlotTiming.invalidate();
                    if (response.body()!!.status == 0) {
                        myToast(this@MyAvailableSlot, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 500) {
                        myToast(this@MyAvailableSlot, "Server Error")
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvSlotTiming.apply {
                            adapter = AdapterShuduleTimingNew(
                                this@MyAvailableSlot,
                                response.body()!!,
                                this@MyAvailableSlot
                            )

                            myToast(this@MyAvailableSlot, "No Slot Found")
                            binding.layoutFamilyMemeber.visibility = View.GONE

                            AppProgressBar.hideLoaderDialog()
                        }
                    } else {
                        binding.rvSlotTiming.apply {
                            //   adapter!!.notifyDataSetChanged();
                            //myToast(this@ShuduleTiming, response.body()!!.message)
                            adapter =
                                AdapterShuduleTimingNew(
                                    this@MyAvailableSlot,
                                    response.body()!!,
                                    this@MyAvailableSlot
                                )
                            if (PrescriptionDetails.FollowUP == "1") {
                                binding.layoutFamilyMemeber.visibility = View.GONE
                            }

                            AppProgressBar.hideLoaderDialog()
                        }

                    }
                }


                override fun onFailure(call: Call<ModelSlotResNew>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCall(selectedate)
                    } else {
                        myToast(this@MyAvailableSlot, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }


            })
    }

    private fun apiCallFamilyListNew() {

        AppProgressBar.showLoaderDialog(context)

        Log.e(ContentValues.TAG, "dayCode:>>$dayCode")
        Log.e(ContentValues.TAG, "selectedate:>>$selectedate")


        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelFamilyList>,
                    response: Response<ModelFamilyList>
                ) {
                    // binding.rvSlotTiming.invalidate();
                    if (response.body()!!.status == 0) {
                        myToast(this@MyAvailableSlot, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        binding.layoutFamilyMemeber.visibility = View.GONE
                    } else if (response.code() == 500) {
                        myToast(this@MyAvailableSlot, "Server Error")
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvSlotTimingFamily.apply {
                            adapter = AdapterFamilyListView(
                                this@MyAvailableSlot,
                                response.body()!!,
                                this@MyAvailableSlot
                            )
                            AppProgressBar.hideLoaderDialog()
                            binding.layoutFamilyMemeber.visibility=View.GONE

                        }
                    } else {
                        countN = 0
                        binding.rvSlotTimingFamily.apply {
                            //   adapter!!.notifyDataSetChanged();
                            //myToast(this@ShuduleTiming, response.body()!!.message)
                            adapter = AdapterFamilyListView(
                                this@MyAvailableSlot,
                                response.body()!!,
                                this@MyAvailableSlot
                            )
                            binding.rvSlotTimingFamily.layoutManager = GridLayoutManager(context, 3)

                            countR=0
                            AppProgressBar.hideLoaderDialog()
                        }

                    }
                }


                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    countN++
                    if (countN <= 3) {
                        apiCallFamilyListNew()
                    } else {
                        myToast(this@MyAvailableSlot, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }


            })
    }

    private fun apiCallOnlineSlot() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getOnlineSlot(doctorId, dayCode)
            .enqueue(object : Callback<ModelSlotResNew> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelSlotResNew>,
                    response: Response<ModelSlotResNew>
                ) {
                    // binding.rvSlotTiming.invalidate();
                    if (response.body()!!.status == 0) {
                        myToast(this@MyAvailableSlot, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    } else if (response.code() == 500) {
                        myToast(this@MyAvailableSlot, "Server Error")
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvSlotTiming.apply {
                            adapter = AdapterShuduleTimingNew(
                                this@MyAvailableSlot,
                                response.body()!!,
                                this@MyAvailableSlot
                            )
                             myToast(this@MyAvailableSlot, "No Slot Found")
                            AppProgressBar.hideLoaderDialog()
                        }
                    } else {
                        binding.rvSlotTiming.apply {
                            //   adapter!!.notifyDataSetChanged();
                            //myToast(this@ShuduleTiming, response.body()!!.message)
                            adapter =
                                AdapterShuduleTimingNew(
                                    this@MyAvailableSlot,
                                    response.body()!!,
                                    this@MyAvailableSlot
                                )

                            AppProgressBar.hideLoaderDialog()
                        }

                    }
                }


                override fun onFailure(call: Call<ModelSlotResNew>, t: Throwable) {
                    countR1++
                    if (countR1 <= 3) {
                        apiCallOnlineSlot()
                    } else {
                        myToast(this@MyAvailableSlot, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
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
        if (PrescriptionDetails.FollowUP == "1") {
            price.text = "Free"
            sessionManager.pricing = "Free"
        } else {
            price.text = sessionManager.pricing

        }
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

    override fun onDestroy() {
        super.onDestroy()
        PrescriptionDetails.FollowUP = ""
    }

    override fun checkBox(id: Int) {

    }

}