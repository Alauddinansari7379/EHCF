package com.example.ehcf.Pharmacy.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Pharmacy.adapter.AdapterMedicine
import com.example.ehcf.Pharmacy.model.ModelMedicine
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityBrowseMedicineBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BrowseMedicine : AppCompatActivity() {
    var context = this@BrowseMedicine
    val binding by lazy {
        ActivityBrowseMedicineBinding.inflate(layoutInflater)
    }
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var countN3=0
    var countN=0
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        shimmerFrameLayout = findViewById(R.id.shimmer_medicine)
        shimmerFrameLayout!!.startShimmer()
        apiCallAllMedicine()
        apiCallCartList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()

            }
            cardMyOrder.setOnClickListener {
                slug = "medicine"
                val intent = Intent(context as Activity, Orders::class.java)
                context.startActivity(intent)
            }

            cardMyCart.setOnClickListener {
                startActivity(Intent(context, CartMedicine::class.java))
            }

            appCompatImageView.setOnClickListener {
                startActivity(Intent(context, CartMedicine::class.java))
            }
            cardDiabetic.setOnClickListener {
                val intent = Intent(context as Activity, ProductListing::class.java)
                    .putExtra("Click", "Diabetic")
                context.startActivity(intent)
            }
            cardHeart.setOnClickListener {
                val intent = Intent(context as Activity, ProductListing::class.java)
                    .putExtra("Click", "Heart")
                context.startActivity(intent)
            }
            cardKidney.setOnClickListener {
                val intent = Intent(context as Activity, ProductListing::class.java)
                    .putExtra("Click", "Kidney")
                context.startActivity(intent)
            }
            cardBeauty.setOnClickListener {
                val intent = Intent(context as Activity, ProductListing::class.java)
                    .putExtra("Click", "Beauty")
                context.startActivity(intent)
            }


        }
    }

    private fun apiCallAllMedicine() {
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
                            myToast(this@BrowseMedicine, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(this@BrowseMedicine, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.recyclerview.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.shimmerMedicine.visibility = View.GONE
                                adapter = AdapterMedicine(this@BrowseMedicine, response.body()!!)
                                binding.shimmerMedicine.visibility = View.GONE

                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelMedicine>, t: Throwable) {
                    binding.shimmerMedicine.visibility = View.GONE
                    countN++
                    if (countN <= 3) {
                        apiCallAllMedicine()
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
                            cartQty = "0"
                            binding.qty.text = cartQty
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val qty = ArrayList<Int>()
                            response.body()!!.result.forEach {
                                qty.add(it.quantity.toInt())
                            }
                            cartQty = qty.sum().toInt().toString()
                            binding.qty.text = cartQty
                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelMedicine>, t: Throwable) {
                    countN3++
                    if (countN3 <= 3) {
                        apiCallCartList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })

    }

    companion object {
        var cartQty = ""
        var slug = ""
    }

    override fun onResume() {
        super.onResume()
        binding.qty.text = cartQty
    }

}