package com.example.ehcf.MyDoctor

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Dashboard.modelResponse.SearchbyLocationRes
import com.example.ehcf.Helper.myToast
import com.example.ehcf.MyDoctor.Adapter.AdapterMyDoctors
import com.example.ehcf.MyDoctor.Model.ModelMyDoctor
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityMyDoctorsBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyDoctors : AppCompatActivity() {
    private lateinit var binding: ActivityMyDoctorsBinding
    private val context: Context = this@MyDoctors
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        shimmerFrameLayout = findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.imgRefresh.setOnClickListener {
            apiCallMyDoctors1()
        }
        apiCallMyDoctors()

        binding.imgSearch.setOnClickListener {
            if (binding.edtSearch.text.isEmpty()){
                binding.edtSearch.error="Enter Doctor Name"
                binding.edtSearch.requestFocus()
            }else{
                apiCallSearchDoctor()

            }
        }

    }
    private fun apiCallSearchDoctor() {
        progressDialog = ProgressDialog(this@MyDoctors)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
        val doctorName = binding.edtSearch.text.toString()
        ApiClient.apiService.searchDoctor(sessionManager.id.toString(),doctorName)
            .enqueue(object : Callback<ModelMyDoctor> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyDoctor>, response: Response<ModelMyDoctor>
                ) {
                    if (response.code() == 500) {
                        myToast(this@MyDoctors, "Server Error")
                        binding.shimmer.visibility = View.GONE
                        progressDialog!!.dismiss()

                    }
                    else if (response.body()!!.status == 0) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        myToast(this@MyDoctors, "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvAllDoctor.adapter =
                            AdapterMyDoctors(this@MyDoctors, response.body()!!)
                        binding.rvAllDoctor.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        myToast(this@MyDoctors, "No Doctor Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvAllDoctor.adapter =
                            AdapterMyDoctors(this@MyDoctors, response.body()!!, )
                        binding.rvAllDoctor.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.GONE
                        shimmerFrameLayout?.startShimmer()
                        binding.rvAllDoctor.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        progressDialog!!.dismiss()
//                        binding.rvManageSlot.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            shimmerFrameLayout?.startShimmer()
//                            binding.rvManageSlot.visibility = View.VISIBLE
//                            binding.shimmerMySlot.visibility = View.GONE
//                            // myToast(this@ShuduleTiming, response.body()!!.message)
//                            adapter = AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
//                            progressDialog!!.dismiss()
//
//                        }
                    }
                }

                override fun onFailure(call: Call<ModelMyDoctor>, t: Throwable) {
                    myToast(this@MyDoctors, "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallMyDoctors() {
        progressDialog = ProgressDialog(this@MyDoctors)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
       // progressDialog!!.show()


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
                            binding.rvAllDoctor.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
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
    private fun apiCallMyDoctors1() {
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
                            binding.rvAllDoctor.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
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