package com.example.ehcf.Specialities.activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.Specialities.adapter.AdapterSpecialities
import com.example.ehcf.Specialities.model.ModelSplic
import com.example.ehcf.Testing.Interface.apiInterface
import com.example.ehcf.databinding.ActivitySpecialitiesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Specialities : AppCompatActivity() {
   private var progressDialog: ProgressDialog? =null
    private val context: Context = this@Specialities

    private lateinit var binding: ActivitySpecialitiesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecialitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    private fun apiCall(){

        progressDialog = ProgressDialog(this@Specialities)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.thedemostore.in/api/customer/")
            .build()
            .create(apiInterface::class.java)
        val retrofitData = retrofitBuilder.getPhotos()
        retrofitData.enqueue(object : Callback<ModelSplic> {
            override fun onResponse(
                call: Call<ModelSplic>,
                response: Response<ModelSplic>)
            {
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSpeci)
                recyclerView.apply {
                    adapter = AdapterSpecialities(context, response.body()!!)
                    progressDialog!!.dismiss()

                }
            }

            override fun onFailure(call: Call<ModelSplic>, t: Throwable) {
                t.message?.let { myToast(this@Specialities, it)
                    progressDialog!!.dismiss()

                }
            }
        })
    }
//    }
}