package com.example.ehcf.Diagnostic

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehcf.Diagnostic.adapter.AdapterTestList
import com.example.ehcf.Diagnostic.model.ModelTestList
import com.example.ehcf.Diagnostic.model.Result
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Pharmacy.activity.BrowseMedicine
import com.example.ehcf.Pharmacy.activity.Orders
import com.example.ehcf.Pharmacy.model.ModelAddToCart
import com.example.ehcf.Pharmacy.model.ModelMedicine
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityCallForTestBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestList : AppCompatActivity(), AdapterTestList.AddToCart {
    var context = this@TestList
    val binding by lazy {
        ActivityCallForTestBinding.inflate(layoutInflater)
    }
    var countN3=0
    var countN2=0
    var countN=0
    private lateinit var mainData: ArrayList<Result>
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        mainData = ArrayList<Result>()

        Log.i("TestList", "TestList")
        shimmerFrameLayout = findViewById(R.id.shimmer_test)
        shimmerFrameLayout!!.startShimmer()
        apiCallTestList()
        apiCallCartList()

        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            appCompatImageView.setOnClickListener {
                startActivity(Intent(context, CartDiagnostic::class.java))
            }

            cardMyCart.setOnClickListener {
                BrowseMedicine.slug="test"
                startActivity(Intent(context, CartDiagnostic::class.java))
            }

            cardMyOrder.setOnClickListener {
                BrowseMedicine.slug="test"
                startActivity(Intent(context, Orders::class.java))
            }
            try {
                binding.edtSearch.addTextChangedListener { str ->
                    setRecyclerViewAdapter(mainData.filter {
                        it.Test_Name != null && it.Test_Name!!.contains(
                            str.toString(),
                            ignoreCase = true
                        )
                    } as ArrayList<Result>)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }

        }
    }

    private fun apiCallTestList() {
       // AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.testList().enqueue(object : Callback<ModelTestList> {
            override fun onResponse(call: Call<ModelTestList>, response: Response<ModelTestList>) {
                try {
                    if (response.code() == 500) {
                        myToast(context, "Server Error")
                        AppProgressBar.hideLoaderDialog()
                    } else if (response.code() == 404) {
                        myToast(context, "Something Went Wrong")
                        AppProgressBar.hideLoaderDialog()
                    } else if (response.body()!!.Result.size==0) {
                        myToast(context, "No Test Found")
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        mainData = response.body()!!.Result
                        setRecyclerViewAdapter(response.body()!!.Result)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    AppProgressBar.hideLoaderDialog()
                    myToast(context, "Something Went Wrong")
                }
            }

            override fun onFailure(call: Call<ModelTestList>, t: Throwable) {
                 binding.shimmerTest.visibility = View.GONE
                countN++
                if (countN <= 3) {
                    apiCallTestList()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }            }

        })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Result>) {
        binding.recyclerView.apply {
            adapter=AdapterTestList(context, data, this@TestList)
            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
            AppProgressBar.hideLoaderDialog()
            binding.shimmerTest.visibility = View.GONE
            AppProgressBar.hideLoaderDialog()
        }
     }


    override fun addToCart(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.addToCart(
            id, sessionManager.id.toString(), "1","test"
        )
            .enqueue(object :
                Callback<ModelAddToCart> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAddToCart>,
                    response: Response<ModelAddToCart>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(context, response.body()!!.message)
                            apiCallCartList()
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelAddToCart>, t: Throwable) {
                    countN3++
                    if (countN3 <= 3) {
                        addToCart(id)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    private fun apiCallCartList() {
        // AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.cartListTest(
            sessionManager.id.toString(),"test"
        )
            .enqueue(object :
                Callback<ModelMedicine> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelMedicine>,
                    response: Response<ModelMedicine>
                ) {
                    try {
                        if (response.code() == 500) {
                            // myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            //  myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            //  myToast(context, "No Product Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val qty = ArrayList<Int>()
                            response.body()!!.result.forEach {
                                qty.add(it.quantity.toInt())
                            }
                            BrowseMedicine.cartQty = qty.sum().toInt().toString()
                            binding.qty.text = BrowseMedicine.cartQty
                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelMedicine>, t: Throwable) {
                    countN2++
                    if (countN2 <= 3) {
                        apiCallCartList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

}