package com.example.ehcf.Address.Activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Address.Adapter.AdapterAddressList
import com.example.ehcf.Address.ModelResponse.AddAddressResponse
import com.example.ehcf.Address.ModelResponse.AddressListResponse
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityAddressListBinding
import com.example.ehcf.retrofit.ApiInterface
import com.example.ehcf.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddressList : AppCompatActivity() {
    private val context: Context = this@AddressList
    private var recyclerView: RecyclerView? = null
    private lateinit var sessionManager: SessionManager
    var count = 0
    var countR = 0
    //  private var addressList = AddressListResponse("",0)

    private lateinit var binding: ActivityAddressListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        apiCallGetAddressList()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvAddAddress.setOnClickListener {
            binding.tvArrowHide.visibility = View.VISIBLE
            binding.layoutAddress.visibility = View.VISIBLE

        }

        binding.btnSubmit.setOnClickListener {
            if (binding.edtEnterAddress.text.isEmpty()) {
                binding.edtEnterAddress.error = "Address Required"
                binding.edtEnterAddress.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtLandmark.text.isEmpty()) {
                binding.edtLandmark.error = "Landmark Required"
                binding.edtLandmark.requestFocus()
                return@setOnClickListener
            }
            apiCallAddAddress()


        }

        binding.tvArrowHide.setOnClickListener {
            binding.layoutAddress.visibility = View.GONE
            binding.tvArrowHide.visibility = View.GONE

        }
    }

    private fun apiCallAddAddress() {
        val address = binding.edtEnterAddress.text.toString()
        val landmark = binding.edtLandmark.text.toString()

        AppProgressBar.showLoaderDialog(context)
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://ehcf.in/api/customer/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.addAddress(
            sessionManager.id,
            address,
            landmark,
            sessionManager.latitude.toString(),
            sessionManager.longitude.toString()
        )
        retrofitData.enqueue(
            object : Callback<AddAddressResponse> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<AddAddressResponse>, response: Response<AddAddressResponse>
                ) {

                    Log.e("Ala", response.body()!!.message)
                    Log.e("Ala", "${response.body()!!.status}")

                    if (response.body()!!.status == 1) {
                        countR = 0
                        myToast(this@AddressList, response.body()!!.message)
                        binding.layoutAddress.visibility = View.GONE
                        binding.tvArrowHide.visibility = View.GONE

                        apiCallGetAddressList()
                        binding.edtEnterAddress.text.clear()
                        binding.edtLandmark.text.clear()
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        myToast(this@AddressList, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()

                    }

                }

                override fun onFailure(call: Call<AddAddressResponse>, t: Throwable) {
                    countR++
                    if (countR <= 3) {
                        apiCallAddAddress()
                    } else {
                        myToast(this@AddressList, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    private fun apiCallGetAddressList() {

        AppProgressBar.showLoaderDialog(context)
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://ehcf.in/api/customer/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.allAddress(sessionManager.id)
        retrofitData.enqueue(
            object : Callback<AddressListResponse> {
                override fun onResponse(
                    call: Call<AddressListResponse>, response: Response<AddressListResponse>
                ) {
                    count = 0
                    val recyclerView = findViewById<RecyclerView>(R.id.rvAddressList)
                    recyclerView.apply {
                        adapter = AdapterAddressList(context, response.body()!!)

                    }
                    AppProgressBar.hideLoaderDialog()
                }

                override fun onFailure(call: Call<AddressListResponse>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallGetAddressList()
                    } else {
                        myToast(this@AddressList, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

}

