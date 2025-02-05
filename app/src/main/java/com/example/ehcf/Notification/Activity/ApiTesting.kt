package com.example.ehcf.Notification.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.Notification.Adapter.Adapter
import com.example.ehcf.Notification.Interface.apiInterface
import com.example.ehcf.Specialities.model.ModelSplic
import com.example.ehcf.databinding.ActivityApiTestingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiTesting : AppCompatActivity() {
    private lateinit var binding: ActivityApiTestingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiTestingBinding.inflate(layoutInflater)
        setContentView(binding.root)



            val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.in/api/customer/")
            .build()
            .create(apiInterface::class.java)

//        val retrofitData =retrofitBuilder.getUser()
//        retrofitData.enqueue(object : Callback<List<User>?> {
//            override fun onResponse(call: Call<List<User>?>, response: Response<List<User>?>) {
//                val recyclerView= findViewById<RecyclerView>(R.id.recyclerView)
//
//                recyclerView.apply {
//                    adapter=Adapter(context,response.body()!!)
//                }
//            }

        val retrofitData = retrofitBuilder.getPhotos()
        retrofitData.enqueue(object : Callback<ModelSplic> {
            override fun onResponse(
                call: Call<ModelSplic>,
                response: Response<ModelSplic>)
            {
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.apply {
                    adapter = Adapter(context, response.body()!!)
                }
            }

            override fun onFailure(call: Call<ModelSplic>, t: Throwable) {
                   t.message?.let { myToast(this@ApiTesting, it) }
            }
        })
    }
}