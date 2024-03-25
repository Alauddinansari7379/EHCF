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

class ReportList : AppCompatActivity(),AdapterUploadReport.DeleteReport {
    private lateinit var binding: ActivityReportListBinding
    private val context: Context = this@ReportList
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    var data=ArrayList<ResultX>()


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
        binding.edtSearch.addTextChangedListener {str ->
            setRecyclerViewAdapter(data.filter {
                it.member_name!=null && it.member_name.contains(str.toString(),ignoreCase = true)
            } as ArrayList<ResultX>)
        }

    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }


    private fun apiCallFamilyList() {



        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamilyList>, response: Response<ModelFamilyList>
                ) {


                    try {
                        val familyList = response.body()!!
                        if (familyList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(familyList.result!!.size)

                            for (i in familyList.result!!.indices) {
                                items[i] = familyList.result!![i].member_name
                            }
                          //  progressDialog!!.dismiss()

                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    context,
                                    R.layout.simple_list_item_1,
                                    items
                                )
                            binding.spinnerFamily.adapter = adapter
                            //   binding.spinnerFamily.setSelection(items.indexOf(relationId));
                            //   Log.e("relaytion",relationId)




                            binding.spinnerFamily.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        val id = familyList.result!![i].id
                                        val relationId = id.toString()
                                        //   Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }
                        }


                    }catch(e:Exception){
                        e.printStackTrace()
                        myToast(this@ReportList, "Something went wrong")
                    }
                }

                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    myToast(this@ReportList, "Something went wrong")
                  //  progressDialog!!.dismiss()

                }

            })
    }
    private fun setRecyclerViewAdapter(data: ArrayList<ResultX>) {
        binding.recyclerView.apply {
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterUploadReport(data, this@ReportList, this@ReportList)
        }
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
                    try {


                        if (response.body()!!.status == 1) {
                            myToast(this@ReportList, response.body()!!.message)
                            progressDialog!!.dismiss()
                            refresh()

                        } else {
                            myToast(this@ReportList, response.body()!!.message)
                            progressDialog!!.dismiss()

                        }
                    }catch (e:Exception){
                        myToast(this@ReportList, "Something went wrong")
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
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            binding.btbAddMoreReport.text = "Add Report"
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            // myToast(requireActivity(),"No Data Found")
                            progressDialog!!.dismiss()

                        } else {
                            data= response.body()!!.result
                            setRecyclerViewAdapter(data)

                            progressDialog!!.dismiss()

                        }
                    }catch (e:Exception){
                        myToast(this@ReportList, "Something went wrong")
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