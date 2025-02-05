package com.example.ehcf.Pharmacy.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehcf.Diagnostic.adapter.AdapterOrderListTest
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Pharmacy.activity.BrowseMedicine.Companion.slug
import com.example.ehcf.Pharmacy.adapter.AdapterOrderDetail
import com.example.ehcf.Pharmacy.model.ModelOrderDetMed
import com.example.ehcf.Pharmacy.model.ModelOrderDetail
import com.example.ehcf.Pharmacy.model.ModelTestODet
import com.example.ehcf.databinding.ActivityOrderDetialBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetail : AppCompatActivity() {
    val binding by lazy {
        ActivityOrderDetialBinding.inflate(layoutInflater)
    }
    val context = this@OrderDetail
    lateinit var sessionManager: SessionManager
    var orderId = ""
    var countN = 0
    var countN1 = 0

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        orderId = intent.getStringExtra("order_id").toString()
        Log.e("orderId", orderId)

        if (slug == "test") apiCallOrderDetailTest() else apiCallOrderDetail()


        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }
        //apiCallOrderDetail()

    }

    private fun apiCallOrderDetail() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.orderDetail(orderId, slug)
            .enqueue(object : Callback<ModelOrderDetMed> {
                override fun onResponse(
                    call: Call<ModelOrderDetMed>,
                    response: Response<ModelOrderDetMed>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            binding.recyclerView.apply {
                                adapter = AdapterOrderDetail(
                                    context,
                                    response.body()!!.result
                                )
                                binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
                                AppProgressBar.hideLoaderDialog()
                            }
                            response.body()!!.result.forEach {
                                binding.orderNo.text = "EHCFOrder" + it.order_id.toString()
                                binding.cost.text = it.totalCoast.toString()
                                binding.statues.text = it.status
                                binding.payment.text = it.payment_name

                            }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went Wrong")
                        AppProgressBar.hideLoaderDialog()
                    }


                }

                override fun onFailure(call: Call<ModelOrderDetMed>, t: Throwable) {
                    countN++
                    if (countN <= 3) {
                        apiCallOrderDetail()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    private fun apiCallOrderDetailTest() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.orderDetailTest(orderId, slug)
            .enqueue(object : Callback<ModelOrderDetMed> {
                override fun onResponse(
                    call: Call<ModelOrderDetMed>,
                    response: Response<ModelOrderDetMed>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            binding.recyclerView.apply {
                                adapter = AdapterOrderDetail(
                                    context,
                                    response.body()!!.result
                                )
                                binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
                                AppProgressBar.hideLoaderDialog()
                            }
                            response.body()!!.result.forEach { it ->
                                binding.orderNo.text = "EHCFOrder" + it.order_id.toString()
                                binding.cost.text = it.totalCoast.toString()
                                binding.statues.text = it.status
                                binding.payment.text = it.payment_name

                            }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went Wrong")
                        AppProgressBar.hideLoaderDialog()
                    }


                }

                override fun onFailure(call: Call<ModelOrderDetMed>, t: Throwable) {
                    countN1++
                    if (countN1 <= 3) {
                        apiCallOrderDetailTest()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }


}