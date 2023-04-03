package com.example.ehcf.MyDoctor

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Dashboard.modelResponse.SearchbyLocationRes
import com.example.ehcf.Helper.myToast
import com.example.ehcf.MyDoctor.Adapter.AdapterMyDoctors
import com.example.ehcf.MyDoctor.Model.ModelMyDoctor
import com.example.ehcf.databinding.ActivityMyDoctorsBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyDoctors : AppCompatActivity() {
    private lateinit var binding: ActivityMyDoctorsBinding
    private val context: Context = this@MyDoctors
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)


        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallMyDoctors()
    }

    private fun apiCallMyDoctors() {
        progressDialog = ProgressDialog(this@MyDoctors)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.myDoctors(sessionManager.id.toString())
            .enqueue(object :
                Callback<ModelMyDoctor> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyDoctor>,
                    response: Response<ModelMyDoctor>
                ) {
                    if (response.code()==500){
                        myToast(this@MyDoctors, "Server Error")
                    }
                   else if (response.body()!!.result.isNotEmpty()) {
                        binding.rvAllDoctor.apply {
                            adapter = AdapterMyDoctors(this@MyDoctors, response.body()!!)
                            progressDialog!!.dismiss()
                        }
                    } else {
                        myToast(this@MyDoctors, "No Doctor Found")
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelMyDoctor>, t: Throwable) {
                    myToast(this@MyDoctors, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })

    }

}