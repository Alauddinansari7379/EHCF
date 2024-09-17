package com.example.ehcf.Pharmacy.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Pharmacy.adapter.AdapterListing
import com.example.ehcf.Pharmacy.model.ModelAddToCart
import com.example.ehcf.Pharmacy.model.ModelMedicine
import com.example.ehcf.Pharmacy.model.Result
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityProductListingBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListing : AppCompatActivity(), AdapterListing.AddToCart {
    private val context = this@ProductListing
    val binding by lazy {
        ActivityProductListingBinding.inflate(layoutInflater)
    }
    var countN = 0
    var countN1 = 0
    var countN2 = 0
    lateinit var sessionManager: SessionManager
    private var click = ""
    lateinit var AdapterListing: AdapterListing
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private lateinit var mainData: ArrayList<Result>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        shimmerFrameLayout = findViewById(R.id.shimmer_listing)
        click = intent.getStringExtra("Click").toString()

        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            appCompatImageView.setOnClickListener {
                startActivity(Intent(context, CartMedicine::class.java))
            }
            try {
                binding.edtSearch.addTextChangedListener { str ->
                    setRecyclerViewAdapter(mainData.filter {
                        it.product_name != null && it.product_name!!.contains(
                            str.toString(),
                            ignoreCase = true
                        )
                    } as ArrayList<Result>)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }

            when (click) {
                "Diabetic" -> tvName.text = "Diabetic Medicine"
                "Heart" -> tvName.text = "Heart Medicine"
                "Kidney" -> tvName.text = "Kidney Medicine"
                "Beauty" -> tvName.text = "Beauty Medicine"
            }
        }
        apiCallAllMedicineList()
        apiCallCartList()
    }

    @SuppressLint("SetTextI18n")
    private fun setRecyclerViewAdapter(data: ArrayList<Result>) {
        binding.recyclerview.apply {
            binding.recyclerview.apply {
                shimmerFrameLayout?.startShimmer()
                adapter =
                    AdapterListing(context, data, this@ProductListing)
                binding.recyclerview.layoutManager = GridLayoutManager(context, 2)
                binding.tvQty.text = data.size.toString() + " Products"

                binding.shimmerListing.visibility = View.GONE
                AppProgressBar.hideLoaderDialog()

            }
        }
    }

    private fun apiCallAllMedicineList() {
        // AppProgressBar.showLoaderDialog(this@BrowseMedicine)

        ApiClient.apiService.allMedicine(

        )
            .enqueue(object :
                Callback<ModelMedicine> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMedicine>,
                    response: Response<ModelMedicine>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!.result
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelMedicine>, t: Throwable) {
                    binding.shimmerListing.visibility = View.GONE
                    countN++
                    if (countN <= 3) {
                        apiCallAllMedicineList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    override fun addToCart(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.addToCart(
            id, sessionManager.id.toString(), "1", "medicine"

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
                            AppProgressBar.hideLoaderDialog()
                            apiCallCartList()
                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelAddToCart>, t: Throwable) {
                    countN1++
                    if (countN1 <= 3) {
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

        ApiClient.apiService.cartList(
            sessionManager.id.toString(), "medicine"

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

    override fun onResume() {
        super.onResume()
        binding.qty.text = BrowseMedicine.cartQty
    }

}