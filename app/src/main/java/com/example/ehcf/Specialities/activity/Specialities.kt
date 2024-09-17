package com.example.ehcf.Specialities.activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.R
import com.example.ehcf.Specialities.adapter.AdapterSpecialities
import com.example.ehcf.Specialities.model.ModelSplic
import com.example.ehcf.Notification.Interface.apiInterface
import com.example.ehcf.databinding.ActivitySpecialitiesBinding
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class Specialities : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    private val context: Context = this@Specialities
    var bookingType = ""
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    private lateinit var binding: ActivitySpecialitiesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecialitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shimmerFrameLayout = findViewById(R.id.shimmer_specialities)
        shimmerFrameLayout!!.startShimmer();

        bookingType = intent.getStringExtra("bookingType").toString()
        Log.e("BookingType", "$bookingType")
        PrescriptionDetails.FollowUP = ""
        apiCall()


//        binding.cardDermatology.setOnClickListener {
//            startActivity(Intent(this, DateForConsultation::class.java))
//        }
//
//        binding.cardGenralPhysician.setOnClickListener {
//            startActivity(Intent(this, DateForConsultation::class.java))
//        }
//        binding.cardSexologist.setOnClickListener {
//            startActivity(Intent(this, DateForConsultation::class.java))
//        }
//        binding.cardPsychologist.setOnClickListener {
//            startActivity(Intent(this, DateForConsultation::class.java))
//        }
//        binding.cardOnlineConsultant.setOnClickListener {
//            startActivity(Intent(this, FindYourDoctor::class.java))
//        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun apiCall() {

        progressDialog = ProgressDialog(this@Specialities)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        //    progressDialog!!.show()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.in/api/customer/")
            .build()
            .create(apiInterface::class.java)
        val retrofitData = retrofitBuilder.getPhotos()
        retrofitData.enqueue(object : Callback<ModelSplic> {
            override fun onResponse(
                call: Call<ModelSplic>,
                response: Response<ModelSplic>
            ) {
                try {
                    if (response.code() == 200) {
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSpeci)
                        recyclerView.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.recyclerViewSpeci.visibility = View.VISIBLE
                            binding.shimmerSpecialities.visibility = View.GONE
                            adapter =
                                AdapterSpecialities(context, response.body()!!, DoctorProfile())
                            progressDialog!!.dismiss()
                        }
                    } else if (response.code() == 500) {
                        myToast(this@Specialities, "Server Error")
                    } else {
                        myToast(this@Specialities, "Something went wrong")

                    }
                } catch (e: Exception) {
                    myToast(this@Specialities, "Something went wrong")
                    progressDialog!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ModelSplic>, t: Throwable) {
                myToast(this@Specialities, "Something went wrong")
                progressDialog!!.dismiss()


            }
        })
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