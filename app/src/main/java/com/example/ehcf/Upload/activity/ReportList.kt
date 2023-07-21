package com.example.ehcf.Upload.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Upload.adapter.AdapterUploadReport
import com.example.ehcf.Upload.model.ModelDeleteRep
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.databinding.ActivityReportListBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportList : AppCompatActivity(),AdapterUploadReport.DeleteReport {
    private lateinit var binding: ActivityReportListBinding
    private val context: Context = this@ReportList
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@ReportList)

        apiCallGetAllReport()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }




     private fun deleteReportNew(id: String) {
        progressDialog = ProgressDialog(this@ReportList)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.deleteReport(id)
            .enqueue(object : Callback<ModelDeleteRep> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDeleteRep>, response: Response<ModelDeleteRep>
                ) {
                    if (response.body()!!.status == 1) {
                        myToast(this@ReportList, response.body()!!.message)
                        progressDialog!!.dismiss()
                        refresh()

                    } else {
                        myToast(this@ReportList, response.body()!!.message)
                        progressDialog!!.dismiss()

                    }


                }

                override fun onFailure(call: Call<ModelDeleteRep>, t: Throwable) {
                    myToast(this@ReportList, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })

    }

    private fun apiCallGetAllReport() {
        progressDialog = ProgressDialog(this@ReportList)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getReport(sessionManager.id.toString())
            .enqueue(object : Callback<ModelGetAllReport> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetAllReport>, response: Response<ModelGetAllReport>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.btbAddMoreReport.text = "Add Report"
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.recyclerView.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterUploadReport(
                                response.body()!!,
                                this@ReportList, this@ReportList)
                        }
                        progressDialog!!.dismiss()

                    }


                }

                override fun onFailure(call: Call<ModelGetAllReport>, t: Throwable) {
                    myToast(this@ReportList, "Something went wrong")
                    progressDialog!!.dismiss()

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