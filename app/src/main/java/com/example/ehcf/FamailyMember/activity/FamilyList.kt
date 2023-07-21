package com.example.ehcf.FamailyMember.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.FamailyMember.Adapter.AdapterFamilyList
import com.example.ehcf.FamailyMember.Model.ModelDelete
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityFamilyListBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamilyList : AppCompatActivity(), AdapterFamilyList.EditFamilyMember {
    private lateinit var binding: ActivityFamilyListBinding
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shimmerFrameLayout = findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();


         sessionManager = SessionManager(this@FamilyList)
        apiCallFamilyList()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }


    private fun apiCallFamilyList() {
        progressDialog = ProgressDialog(this@FamilyList)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamilyList>, response: Response<ModelFamilyList>
                ) {
                    if (response.code() == 500) {
                        myToast(this@FamilyList, "Server error")
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()
                    } else {
                        binding.recyclerView.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterFamilyList(
                                this@FamilyList,
                                response.body()!!,
                                this@FamilyList
                            )
                            progressDialog!!.dismiss()
                        }
                    }

                }

                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    myToast(this@FamilyList, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallDelete(id: String) {
        progressDialog = ProgressDialog(this@FamilyList)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.deleteFamily(id)
            .enqueue(object : Callback<ModelDelete> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDelete>, response: Response<ModelDelete>
                ) {
                    if (response.code() == 500) {
                        myToast(this@FamilyList, "Server error")
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.status == 1) {
                        myToast(this@FamilyList, response.body()!!.message)
                        progressDialog!!.dismiss()
                        refresh()
                    } else {
                        myToast(this@FamilyList, response.body()!!.message)
                        progressDialog!!.dismiss()

                    }


                }

                override fun onFailure(call: Call<ModelDelete>, t: Throwable) {
                    myToast(this@FamilyList, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    override fun delete(id: String) {
        SweetAlertDialog(this@FamilyList, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Delete?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                apiCallDelete(id)

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }

    override fun onResume() {
        super.onResume()
        if (AddNewFamily.Data.refreshValue == "1") {
            AddNewFamily.Data.refreshValue = ""
            refresh()
        }
    }
    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

}