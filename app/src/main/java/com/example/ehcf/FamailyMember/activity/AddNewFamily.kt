package com.example.ehcf.FamailyMember.activity

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.ehcf.FamailyMember.Model.ModelFamilyMember
import com.example.ehcf.FamailyMember.Model.ModelFamilyNew
import com.example.ehcf.FamailyMember.activity.AddNewFamily.Data.Companion.refreshValue
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.databinding.ActivityAddNewFamilyBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddNewFamily : AppCompatActivity() {
    private val context: Context = this@AddNewFamily
    var mydilaog: Dialog? = null
    var progressDialog: ProgressDialog? = null
    private var relationListNew = ModelFamilyNew()
    var firstName = ""
    private lateinit var sessionManager: SessionManager
    var lastName = ""
    var dob = ""
    var gender = ""
    var relationId = ""
    var email = ""
    var description = ""
    var id = ""
    var edit = ""
    private lateinit var binding: ActivityAddNewFamilyBinding

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        id = intent.getStringExtra("id").toString()
        gender = intent.getStringExtra("gender").toString()
        edit = intent.getStringExtra("Edit").toString()
        if (edit == "1") {
            binding.importent.visibility=View.GONE
            binding.appCompatTextView2.text = "Edit Family Member"
            binding.btnSave.text = "Confirm"
            //val membername=intent.getStringExtra("first_name").toString()
           // val lasName= membername.split(" ")
            binding.edtFirstName.setText(intent.getStringExtra("first_name").toString())
            binding.edtLastName.setText(intent.getStringExtra("first_name").toString())
            binding.edtDescription.setText(intent.getStringExtra("email").toString())
            binding.tvDate.text = intent.getStringExtra("dob").toString()
            relationId = intent.getStringExtra("relation").toString()
            Log.e("gender",gender)
        }




        when (gender) {
            "1" -> {
                binding.tvMale.setTextColor(Color.parseColor("#A19398"))
                binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
                binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
            }
            "2" -> {
                binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
                binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
                binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
            }
            "3" -> {
                binding.tvOther.setTextColor(Color.parseColor("#A19398"))
                binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
                binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
            }
        }



        sessionManager = SessionManager(this@AddNewFamily)
        apiCallGetRelation()

        binding.btnCancle.setOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {

            if (binding.edtFirstName.text.isEmpty()) {
                binding.edtFirstName.error = "Enter First Name"
                binding.edtFirstName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtLastName.text.isEmpty()) {
                binding.edtLastName.error = "Enter Last Name"
                binding.edtLastName.requestFocus()
                return@setOnClickListener

            }
            if (binding.tvDate.text.isEmpty()) {
                binding.tvDate.error = "Select Date"
                binding.tvDate.requestFocus()
                return@setOnClickListener
            }
            if (gender.isEmpty()) {
                myToast(this, "Select Gender")
                return@setOnClickListener
            }
            if (binding.edtDescription.text.isEmpty()) {
                binding.edtDescription.error = "Enter Description"
                binding.edtDescription.requestFocus()
                return@setOnClickListener
            }

            firstName = binding.edtFirstName.text.toString().trim()
            lastName = binding.edtLastName.text.toString().trim()
            dob = binding.tvDate.text.toString().trim()
             description = binding.edtDescription.text.toString().trim()

            if (edit == "1") {
                apiCallEditFamily()
            }else{
                apiCallAddMember()

            }


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
            gender = "1"
            binding.tvMale.setTextColor(Color.parseColor("#A19398"))
            binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
        }
        binding.tvFemale.setOnClickListener {
            gender = "2"
            binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
            binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
            binding.tvMale.setTextColor(Color.parseColor("#9F367A"))

        }
        binding.tvOther.setOnClickListener {
            gender = "3"
            binding.tvOther.setTextColor(Color.parseColor("#A19398"))
            binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun apiCallGetRelation() {
        progressDialog = ProgressDialog(this@AddNewFamily)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        //  progressDialog!!.show()

        ApiClient.apiService.getRelation()
            .enqueue(object : Callback<ModelFamilyNew> {
                @SuppressLint("LogNotTimber", "SuspiciousIndentation")
                override fun onResponse(
                    call: Call<ModelFamilyNew>, response: Response<ModelFamilyNew>
                )

                {


                    relationListNew = response.body()!!;
                    if (relationListNew != null) {

                        //spinner code start
                        val items = arrayOfNulls<String>(relationListNew.result!!.size)

                        for (i in relationListNew.result!!.indices) {
                            items[i] = relationListNew.result!![i].name
                        }
                        val adapter: ArrayAdapter<String?> =
                            ArrayAdapter(this@AddNewFamily, R.layout.simple_list_item_1, items)
                        binding.spinnerFamily.adapter = adapter
                            binding.spinnerFamily.setSelection(items.indexOf(relationId));
                        Log.e("relaytion",relationId)


                        progressDialog!!.dismiss()


                        binding.spinnerFamily.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    adapterView: AdapterView<*>?,
                                    view: View,
                                    i: Int,
                                    l: Long
                                ) {
                                    val id = relationListNew.result!![i].stateId
                                    relationId = id.toString()
                                    //   Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                }

                                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                            }

                    }
                }

                override fun onFailure(call: Call<ModelFamilyNew>, t: Throwable) {
                    myToast(this@AddNewFamily, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallAddMember() {
        progressDialog = ProgressDialog(this@AddNewFamily)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.addFamily(
            sessionManager.id.toString(), firstName, lastName,
            dob, gender, relationId, email,description)
            .enqueue(object : Callback<ModelFamilyMember> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamilyMember>, response: Response<ModelFamilyMember>
                ) {

                    if (response.body()!!.status == 1) {
                        myToast(this@AddNewFamily, response.body()!!.message)
                        progressDialog!!.dismiss()
                        refresh()

                    } else {
                        myToast(this@AddNewFamily, "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelFamilyMember>, t: Throwable) {
                    myToast(this@AddNewFamily, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallEditFamily() {
        progressDialog = ProgressDialog(this@AddNewFamily)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.editFamily(id, firstName, lastName, dob, gender, relationId, email,description)
            .enqueue(object : Callback<ModelFamilyMember> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamilyMember>, response: Response<ModelFamilyMember>
                ) {

                    if (response.body()!!.status == 1) {
                        myToast(this@AddNewFamily, response.body()!!.message)
                        progressDialog!!.dismiss()
                        refreshValue="1"
                        onBackPressed()
                        //refresh()

                    } else {
                        myToast(this@AddNewFamily, "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelFamilyMember>, t: Throwable) {
                    myToast(this@AddNewFamily, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
    class Data() {

        // fields which are common across class objects
        companion object {
            var refreshValue = ""
         }
    }
    override fun onStart() {
        super.onStart()
        if (isOnline(this)) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
    }
}
