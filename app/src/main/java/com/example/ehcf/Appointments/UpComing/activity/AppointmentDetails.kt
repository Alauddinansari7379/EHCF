package com.example.ehcf.Appointments.UpComing.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentsDetails
import com.example.ehcf.Helper.myToast
import com.example.ehcf.databinding.ActivityAppointmentDetailsBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentDetails : AppCompatActivity() {
    private val context: Context = this@AppointmentDetails
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog : ProgressDialog?=null
    var dialog: Dialog?= null
    var bookingId=""

    private lateinit var binding:ActivityAppointmentDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAppointmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookingId = intent.getStringExtra("bookingId").toString()
        Log.e("bookingId", bookingId)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallAppointmentsDetails()

    }

    private fun apiCallAppointmentsDetails() {
        progressDialog = ProgressDialog(this@AppointmentDetails)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.appointmentsDetails(bookingId)
            .enqueue(object : Callback<ModelAppointmentsDetails> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentsDetails>, response: Response<ModelAppointmentsDetails>
                ) {
                    Log.e("Ala", "${response.body()!!}")
                    Log.e("Ala", "${response.body()!!.status}")

                   binding.tvDate.text= response.body()!!.result.date
                   binding.tvTime.text= response.body()!!.result.time
                   binding.tvDoctorName.text= response.body()!!.result.doctor_name
                    when(response.body()!!.result.consultation_type){
                        "1"->{
                            binding.tvConsultationType.text="Tele-Consultation"
                        }
                        "2"->{
                            binding.tvConsultationType.text="Clinic-Visit"
                        }
                        "3"->{
                            binding.tvConsultationType.text="Home-Visit"
                        }
                    }
                    when(response.body()!!.result.payment_mode){
                        "1"->{
                            binding.tvPaymentMode.text="Cash On Delivery"
                        }
                        "2"->{
                            binding.tvPaymentMode.text="Online"
                        }
                    }
                   binding.tvTotalAmount.text= response.body()!!.result.total
                   binding.tvEmail.text= response.body()!!.result.email
                   binding.tvPhoneNumber.text= response.body()!!.result.phone_number
                   binding.tvBookingId.text= response.body()!!.result.id
                   binding.tvStatus.text= response.body()!!.result.status_for_customer
                    progressDialog!!.dismiss()

//                    if (response.body()!!.result!=null){
//                        binding.tvNoDataFound.visibility = View.VISIBLE
//                        // myToast(requireActivity(),"No Appointment Found")
//                        progressDialog!!.dismiss()
//
//                    }else{
//                        binding.recyclerView.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            adapter = AdapterAppointmentsDetails(this@AppointmentDetails, response.body()!!)
//                            progressDialog!!.dismiss()
//
//                        }
//                    }


                }

                override fun onFailure(call: Call<ModelAppointmentsDetails>, t: Throwable) {
                    myToast(this@AppointmentDetails, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }


}