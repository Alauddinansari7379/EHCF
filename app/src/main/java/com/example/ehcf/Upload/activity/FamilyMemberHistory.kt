package com.example.ehcf.Upload.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehcf.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.FamailyMember.Adapter.AdapterFamilyList
import com.example.ehcf.FamailyMember.Model.ModelFamily
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.Upload.adapter.AdapterFamilyMember
import com.example.ehcf.Upload.adapter.AdapterReportHistory
import com.example.ehcf.Upload.adapter.AdapterUploadReport
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.databinding.ActivityFamilyMemberHistoryBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamilyMemberHistory : AppCompatActivity() {
    private lateinit var binding: ActivityFamilyMemberHistoryBinding
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilyMemberHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@FamilyMemberHistory)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        if(AdapterFamilyList.familyMemberList=="1"){
            AdapterFamilyList.familyMemberList=""
            apiCallViewAppointment()

        }

        if(AdapterFamilyList.familyMemberList=="2"){
            AdapterFamilyList.familyMemberList=""
            apiCallViewReport()

        }
//        binding.btnReport.setOnClickListener {
//            if (AdapterFamilyListView.memberID.isEmpty()) {
//                myToast(this@FamilyMemberHistory, "Please Select Family Member")
//            } else {
//                apiCallViewReport()
//
//            }
//        }

    }



    private fun apiCallViewReport() {
         progressDialog = ProgressDialog(this@FamilyMemberHistory)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.reportHistory(
            sessionManager.id.toString(),
            AdapterFamilyListView.memberID
        )
            .enqueue(object : Callback<ModelGetAllReport> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetAllReport>, response: Response<ModelGetAllReport>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFoundApp.visibility = View.GONE
                            binding.tvNoDataFoundReport.visibility = View.VISIBLE                            // myToast(requireActivity(),"No Data Found")
                            binding.rvCancled.visibility = View.GONE
                             progressDialog!!.dismiss()


                        } else {
                            binding.recyclerView.apply {
                                binding.tvNoDataFoundApp.visibility = View.GONE
                                binding.tvNoDataFoundReport.visibility = View.GONE
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.rvCancled.visibility = View.GONE
                                adapter = AdapterReportHistory(response.body()!!, this@FamilyMemberHistory,
                                )
                            }
                            progressDialog!!.dismiss()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@FamilyMemberHistory, "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }


                override fun onFailure(call: Call<ModelGetAllReport>, t: Throwable) {
                    myToast(this@FamilyMemberHistory, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallViewAppointment() {
        progressDialog = ProgressDialog(this@FamilyMemberHistory)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
        ApiClient.apiService.memberAppointmentHistory(
            sessionManager.id.toString(),
            AdapterFamilyListView.memberID
        )
            .enqueue(object : Callback<ModelFamily> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamily>, response: Response<ModelFamily>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@FamilyMemberHistory, "Server Error")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFoundApp.visibility = View.VISIBLE
                            binding.tvNoDataFoundReport.visibility = View.GONE
                            // myToast(requireActivity(),"No Appointment Found")
                            binding.rvCancled.apply {
                                 binding.recyclerView.visibility = View.GONE
                                adapter = AdapterFamilyMember(this@FamilyMemberHistory, response.body()!!)
                                progressDialog!!.dismiss()

                            }
                            progressDialog!!.dismiss()

                        } else {
                            binding.rvCancled.apply {
                                binding.rvCancled.visibility = View.VISIBLE
                                binding.tvNoDataFoundApp.visibility = View.GONE
                                binding.tvNoDataFoundReport.visibility = View.GONE
                                binding.recyclerView.visibility = View.GONE
                                adapter =
                                    AdapterFamilyMember(this@FamilyMemberHistory, response.body()!!)
                                progressDialog!!.dismiss()

                            }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@FamilyMemberHistory, "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelFamily>, t: Throwable) {
                    myToast(this@FamilyMemberHistory, "Something went wrong Pls Try Again")
                    progressDialog!!.dismiss()

                }

            })
    }


    override fun onResume() {
        super.onResume()
        AdapterFamilyListView.memberID=""

    }

}