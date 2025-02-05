package com.example.ehcf.Prescription

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.databinding.ActivityAddPrescriptionBinding
import com.giphy.sdk.analytics.GiphyPingbacks.context
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddPrescription : AppCompatActivity() {
    private val context: Context = this@AddPrescription
    var mydilaog: Dialog? = null
    var recordList = ArrayList<String>()

    private lateinit var binding: ActivityAddPrescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        recordList.add("abc")
        recordList.add("abc1")
        recordList.add("abc2")
        recordList.add("abc3")
        recordList.add("abc4")
        binding.spinnerRecord.adapter =
            ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, recordList)

        binding.cardUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            val uri: Uri = Uri.parse("/whatever/path/you/want/") // a directory
            intent.setDataAndType(uri, "*/*")
            startActivity(Intent.createChooser(intent, "Open folder"))

        }



        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar1 = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                DateFormat.getDateInstance().format(newDate.time)
                // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                val Date1 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text = Date1
                Log.e(ContentValues.TAG, "onCreate: >>>>>>>>>>>>>>>>>>>>>>$Date1")
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )
        // Productiondate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        binding.tvDate.setOnClickListener {
            datePicker.show()

        }
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