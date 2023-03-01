package com.example.ehcf

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.ehcf.Helper.myToast
import com.example.ehcf.databinding.ActivityAddNewFamilyBinding
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNewFamily : AppCompatActivity() {
    private val context: Context = this@AddNewFamily
    var mydilaog: Dialog? = null

    var relationList = ArrayList<String>()
    private lateinit var binding: ActivityAddNewFamilyBinding

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        relationList.add("Grandfather")
        relationList.add("Grandmother")
        relationList.add("Grandson")
        relationList.add("Husband")
        relationList.add("Brother")
        binding.spinnerRelation.adapter = ArrayAdapter<String>(context, R.layout.simple_list_item_1, relationList)


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
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text = date
                Log.e(ContentValues.TAG, "onCreate: >>>>>>>>>>>>>>>>>>>>>>$date")
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )

        // Productiondate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        binding.tvDate.setOnClickListener {
            datePicker.show()

        }

        binding.tvMale.setOnClickListener {
            binding.tvMale.setTextColor(Color.parseColor("#A19398"))
            binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
        }
        binding.tvFemale.setOnClickListener {
            binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
            binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
            binding.tvMale.setTextColor(Color.parseColor("#9F367A"))

        }
        binding.tvOther.setOnClickListener {
            binding.tvOther.setTextColor(Color.parseColor("#A19398"))
            binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            myToast(this, "Add Successfully")
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