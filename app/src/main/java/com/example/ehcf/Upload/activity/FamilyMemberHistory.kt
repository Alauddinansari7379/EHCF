package com.example.ehcf.Upload.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.FamailyMember.Adapter.AdapterFamilyList
import com.example.ehcf.FamailyMember.Model.ModelFamily
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Upload.adapter.AdapterFamilyMember
import com.example.ehcf.Upload.adapter.AdapterReportHistory
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.databinding.ActivityFamilyMemberHistoryBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamilyMemberHistory : AppCompatActivity() {
    private lateinit var binding: ActivityFamilyMemberHistoryBinding
    private lateinit var sessionManager: SessionManager
    val context = this@FamilyMemberHistory
    var countR = 0
    var countR1 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilyMemberHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@FamilyMemberHistory)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        if (AdapterFamilyList.familyMemberList == "1") {
            AdapterFamilyList.familyMemberList = ""
            apiCallViewAppointment()

        }

        if (AdapterFamilyList.familyMemberList == "2") {
            AdapterFamilyList.familyMemberList = ""
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
        AppProgressBar.showLoaderDialog(context)

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
                            binding.tvNoDataFoundReport.visibility =
                                View.VISIBLE                            // myToast(requireActivity(),"No Data Found")
                            binding.rvCancled.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()


                        } else {
                            binding.recyclerView.apply {
                                binding.tvNoDataFoundApp.visibility = View.GONE
                                binding.tvNoDataFoundReport.visibility = View.GONE
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.rvCancled.visibility = View.GONE
                                adapter = AdapterReportHistory(
                                    response.body()!!, this@FamilyMemberHistory,
                                )
                            }
                            AppProgressBar.hideLoaderDialog()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@FamilyMemberHistory, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelGetAllReport>, t: Throwable) {
                    countR++
                    if (countR <= 3) {
                        apiCallViewReport()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    private fun apiCallViewAppointment() {
        AppProgressBar.showLoaderDialog(context)
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
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFoundApp.visibility = View.VISIBLE
                            binding.tvNoDataFoundReport.visibility = View.GONE
                            // myToast(requireActivity(),"No Appointment Found")
                            binding.rvCancled.apply {
                                binding.recyclerView.visibility = View.GONE
                                adapter =
                                    AdapterFamilyMember(this@FamilyMemberHistory, response.body()!!)
                                AppProgressBar.hideLoaderDialog()

                            }

                        } else {
                            binding.rvCancled.apply {
                                binding.rvCancled.visibility = View.VISIBLE
                                binding.tvNoDataFoundApp.visibility = View.GONE
                                binding.tvNoDataFoundReport.visibility = View.GONE
                                binding.recyclerView.visibility = View.GONE
                                adapter =
                                    AdapterFamilyMember(this@FamilyMemberHistory, response.body()!!)
                                AppProgressBar.hideLoaderDialog()

                            }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@FamilyMemberHistory, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelFamily>, t: Throwable) {
                    countR1++
                    if (countR1 <= 3) {
                        apiCallViewAppointment()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })
    }


    override fun onResume() {
        super.onResume()
        AdapterFamilyListView.memberID = ""

    }

}