package com.example.ehcf.Upload.activity

import android.R
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf.Appointments.UpComing.model.ResultXXX
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Upload.adapter.AdapterUploadReport
import com.example.ehcf.Upload.model.ModelDeleteRep
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.Upload.model.ResultX
import com.example.ehcf.databinding.ActivityReportListBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportList : AppCompatActivity(), AdapterUploadReport.DeleteReport {
    private lateinit var binding: ActivityReportListBinding
    private val context = this@ReportList
    private lateinit var sessionManager: SessionManager
    var data = ArrayList<ResultX>()
    var count1 = 0
    var count2 = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@ReportList)

        apiCallGetAllReport()
        //apiCallFamilyList()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(data.filter {
                it.member_name != null && it.member_name.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultX>)
        }

    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }



    private fun setRecyclerViewAdapter(data: ArrayList<ResultX>) {
        binding.recyclerView.apply {
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterUploadReport(data, this@ReportList, this@ReportList)
        }
    }

    private fun deleteReportNew(id: String) {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.deleteReport(id)
            .enqueue(object : Callback<ModelDeleteRep> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDeleteRep>, response: Response<ModelDeleteRep>
                ) {
                    try {


                        if (response.body()!!.status == 1) {
                            myToast(this@ReportList, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            refresh()

                        } else {
                            myToast(this@ReportList, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        myToast(this@ReportList, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }


                }

                override fun onFailure(call: Call<ModelDeleteRep>, t: Throwable) {
                    count1++
                    if (count1 <= 3) {
                        deleteReportNew(id)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    private fun apiCallGetAllReport() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getReport(sessionManager.id.toString())
            .enqueue(object : Callback<ModelGetAllReport> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetAllReport>, response: Response<ModelGetAllReport>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            binding.btbAddMoreReport.text = "Add Report"
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            data = response.body()!!.result
                            setRecyclerViewAdapter(data)

                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        myToast(this@ReportList, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }


                }

                override fun onFailure(call: Call<ModelGetAllReport>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        apiCallGetAllReport()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }


    override fun deleteReport(id: String) {
        SweetAlertDialog(this@ReportList, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Delete Report?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                deleteReportNew(id)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }

}