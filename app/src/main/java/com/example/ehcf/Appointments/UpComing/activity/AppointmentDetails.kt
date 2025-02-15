package com.example.ehcf.Appointments.UpComing.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentsDetails
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityAppointmentDetailsBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentDetails : AppCompatActivity() {
    private val context: Context = this@AppointmentDetails
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
     var dialog: Dialog?= null
    var bookingId=""
    var count=0

    private lateinit var binding:ActivityAppointmentDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAppointmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        bookingId = intent.getStringExtra("bookingId").toString()
        Log.e("bookingId", bookingId)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallAppointmentsDetails()

    }

    private fun apiCallAppointmentsDetails() {
       AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.appointmentsDetails(bookingId)
            .enqueue(object : Callback<ModelAppointmentsDetails> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentsDetails>, response: Response<ModelAppointmentsDetails>
                ) {
                    try {
                        if (response.code()==500){
                            myToast(this@AppointmentDetails,"Server error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code()==404){
                            myToast(this@AppointmentDetails, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()
                        }else
                        Log.e("Ala", "${response.body()!!}")
                        Log.e("Ala", "${response.body()!!.status}")
                        AppProgressBar.hideLoaderDialog()

                        binding.tvDate.text = response.body()!!.result.date
                        binding.tvTime.text = convertTo12Hour(response.body()!!.result.start_time)
                        binding.tvDoctorName.text = response.body()!!.result.doctor_name

                        if (response.body()!!.result.member_name != null) {
                            binding.tvPatientName.text = response.body()!!.result.member_name
                        } else {
                            binding.tvPatientName.text = response.body()!!.result.customer_name

                        }
                        when (response.body()!!.result.consultation_type) {
                            "1" -> {
                                binding.tvConsultationType.text = "Tele-Consultation"
                            }
                            "2" -> {
                                binding.tvConsultationType.text = "Clinic-Visit"
                            }
                            "3" -> {
                                binding.tvConsultationType.text = "Home-Visit"
                            }
                        }
                        when (response.body()!!.result.payment_mode) {
                            "1" -> {
                                binding.tvPaymentMode.text = "Cash On Delivery"
                            }
                            "2" -> {
                                binding.tvPaymentMode.text = "Online"
                            }
                            "5" -> {
                                binding.tvPaymentMode.text = "Free"
                            }
                        }
                        binding.tvTotalAmount.text = response.body()!!.result.total
                        binding.tvEmail.text = response.body()!!.result.email
                        binding.tvPhoneNumber.text = response.body()!!.result.phone_number
                        binding.tvBookingId.text = response.body()!!.result.id
                        binding.tvStatus.text = response.body()!!.result.status_for_customer
                        if (response.body()!!.result.profile_image != null) {
                            Picasso.get()
                                .load("${sessionManager.imageurl}${response.body()!!.result.profile_image}")
                                .placeholder(
                                    R.drawable.profile
                                ).error(R.drawable.profile).into(binding.imgProfile);
                        }

                        AppProgressBar.hideLoaderDialog()

                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(this@AppointmentDetails, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelAppointmentsDetails>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallAppointmentsDetails()
                    } else {
                        myToast(this@AppointmentDetails, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }


}