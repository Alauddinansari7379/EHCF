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
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
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
        apiCallFamilyListNew()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnAppointment.setOnClickListener {
            if (AdapterFamilyListView.memberID.isEmpty()) {
                myToast(this@FamilyMemberHistory, "Please Select Family Member")
            } else {
                apiCallViewAppointment()

            }
        }
        binding.btnReport.setOnClickListener {
            if (AdapterFamilyListView.memberID.isEmpty()) {
                myToast(this@FamilyMemberHistory, "Please Select Family Member")
            } else {
                apiCallViewReport()

            }
        }

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
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            // myToast(requireActivity(),"No Data Found")
                            binding.rvCancled.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            progressDialog!!.dismiss()

                        } else {
                            binding.recyclerView.apply {
                                binding.tvNoDataFound.visibility = View.GONE
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
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@FamilyMemberHistory, "Server Error")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            // myToast(requireActivity(),"No Appointment Found")
                            binding.rvCancled.apply {
                                binding.rvCancled.visibility = View.VISIBLE
                                binding.recyclerView.visibility = View.GONE
                                adapter =
                                    AdapterConsulted(this@FamilyMemberHistory, response.body()!!)
                                progressDialog!!.dismiss()

                            }
                            progressDialog!!.dismiss()

                        } else {
                            binding.rvCancled.apply {
                                binding.rvCancled.visibility = View.VISIBLE
                                binding.tvNoDataFound.visibility = View.GONE
                                binding.recyclerView.visibility = View.GONE
                                adapter =
                                    AdapterConsulted(this@FamilyMemberHistory, response.body()!!)
                                progressDialog!!.dismiss()

                            }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@FamilyMemberHistory, "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
                    myToast(this@FamilyMemberHistory, "Something went wrong Pls Try Again")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallFamilyListNew() {

        progressDialog = ProgressDialog(this@FamilyMemberHistory)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelFamilyList>,
                    response: Response<ModelFamilyList>
                ) {
                    // binding.rvSlotTiming.invalidate();
                    if (response.body()!!.status == 0) {
                        myToast(this@FamilyMemberHistory, "${response.body()!!.message}")
                        progressDialog!!.dismiss()
                    } else if (response.code() == 500) {
                        myToast(this@FamilyMemberHistory, "Server Error")
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvSlotTimingFamily.apply {
                            adapter =
                                AdapterFamilyListView(this@FamilyMemberHistory, response.body()!!)
                            progressDialog!!.dismiss()
                        }
                    } else {
                        binding.rvSlotTimingFamily.apply {
                            //   adapter!!.notifyDataSetChanged();
                            //myToast(this@ShuduleTiming, response.body()!!.message)
                            adapter =
                                AdapterFamilyListView(this@FamilyMemberHistory, response.body()!!)
                            binding.rvSlotTimingFamily.layoutManager =
                                GridLayoutManager(this@FamilyMemberHistory, 3)
                            //    binding.layoutFamilyMemeber.visibility=View.VISIBLE

                            progressDialog!!.dismiss()
                        }

                    }
                }


                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    progressDialog!!.dismiss()
                    myToast(this@FamilyMemberHistory, "Something went wrong")
                }


            })
    }

}