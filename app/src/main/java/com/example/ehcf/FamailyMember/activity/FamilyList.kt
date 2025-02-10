package com.example.ehcf.FamailyMember.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.FamailyMember.Adapter.AdapterFamilyList
import com.example.ehcf.FamailyMember.Model.ModelDelete
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityFamilyListBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamilyList : AppCompatActivity(), AdapterFamilyList.EditFamilyMember {
    private lateinit var binding: ActivityFamilyListBinding
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    val context = this@FamilyList
    var countN3 = 0
    var countN = 0
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
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamilyList>, response: Response<ModelFamilyList>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@FamilyList, "Server error")
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            binding.recyclerView.apply {
                                binding.tvNoDataFound.visibility = View.GONE
                                binding.shimmer.visibility = View.GONE
                                adapter = AdapterFamilyList(
                                    this@FamilyList,
                                    response.body()!!,
                                    this@FamilyList
                                )
                                AppProgressBar.hideLoaderDialog()
                            }
                        }
                    } catch (e: Exception) {
                        myToast(this@FamilyList, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    countN3++
                    if (countN3 <= 3) {
                        apiCallFamilyList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    private fun apiCallDelete(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.deleteFamily(id)
            .enqueue(object : Callback<ModelDelete> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDelete>, response: Response<ModelDelete>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@FamilyList, "Server error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.status == 1) {
                            myToast(this@FamilyList, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            refresh()
                        } else {
                            myToast(this@FamilyList, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()

                        }

                    } catch (e: Exception) {
                        myToast(this@FamilyList, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelDelete>, t: Throwable) {
                    countN++
                    if (countN <= 3) {
                        apiCallFamilyList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
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