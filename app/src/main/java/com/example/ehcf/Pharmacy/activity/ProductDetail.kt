package com.example.ehcf.Pharmacy.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Pharmacy.model.ModelAddToCart
import com.example.ehcf.Pharmacy.model.ModelMedicine
import com.example.ehcf.Pharmacy.model.ModelSpinner
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityProductDetialBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetail : AppCompatActivity() {
    val context = this@ProductDetail
    val binding by lazy {
        ActivityProductDetialBinding.inflate(layoutInflater)
    }
    lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var subCateId = ""
    var productId = ""
    var idP = ""
    var countN = 0
    var countN1 = 0
    var countN2 = 0
    var productQty = ""
    var qtyList = ArrayList<ModelSpinner>()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        shimmerFrameLayout = findViewById(R.id.shimmer_medicine_det)
        shimmerFrameLayout!!.startShimmer()

        idP = intent.getStringExtra("id").toString()
        productId = intent.getStringExtra("product_number").toString()
        subCateId = intent.getStringExtra("sub_category_id").toString()
        Log.e("productId", productId)
        Log.e("subCateId", subCateId)
        apiCallMedicineDetail()

        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            btnAddtoCart.setOnClickListener {
                addToCart()
            }
            radioPrice.setOnClickListener {
                if (radioPrice.isChecked) {
                    radioMarket.isChecked = false
                }
            }
            radioMarket.setOnClickListener {
                if (radioMarket.isChecked) {
                    radioPrice.isChecked = false
                }
            }


            qtyList.add(ModelSpinner("1", "1"))
            qtyList.add(ModelSpinner("2", "1"))
            qtyList.add(ModelSpinner("3", "1"))
            qtyList.add(ModelSpinner("4", "1"))
            qtyList.add(ModelSpinner("5", "1"))
            qtyList.add(ModelSpinner("6", "1"))
            qtyList.add(ModelSpinner("7", "1"))
            qtyList.add(ModelSpinner("8", "1"))
            qtyList.add(ModelSpinner("9", "1"))
            qtyList.add(ModelSpinner("10", "1"))
            val adapter: ArrayAdapter<ModelSpinner?> =
                ArrayAdapter(
                    context, R.layout.simple_list_item,
                    qtyList as List<ModelSpinner?>
                )
            spinnerQty.adapter = adapter
            spinnerQty.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (qtyList.size > 0) {
                            productQty = qtyList[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }

                }
        }
    }

    private fun apiCallMedicineDetail() {
        // AppProgressBar.showLoaderDialog(this@BrowseMedicine)

        ApiClient.apiService.productsDetail(
            idP,
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
                            binding.shimmerMedicineDet.visibility = View.GONE
                            response.body()!!.result.forEach {
                                productId = it.product_number.toString()
                                binding.tvProductName.text = it.product_name
                                binding.description.text = it.description
                                binding.marketPrice.text = "₹" + it.marked_price.toString()
                                binding.price.text = "₹" + it.price.toString()

                                if (it.image != null) {
                                    Glide.with(this@ProductDetail)
                                        .load(sessionManager.imageurl + it.image) // image url
                                        .placeholder(R.drawable.placeholder_n) // any placeholder to load at start
                                        .error(R.drawable.error_placeholder)  // any image in case of error
                                        .centerCrop()
                                        .into(binding.image);


//                                    Picasso.get().load(sessionManager.imageurl + it.image)
//                                        .placeholder(R.drawable.placeholder_n)
//                                        .error(R.drawable.error_placeholder)
//                                        .into(binding.image)
                                }
                            }

                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelMedicine>, t: Throwable) {
                    binding.shimmerMedicineDet.visibility = View.GONE
                    countN++
                    if (countN <= 3) {
                        apiCallMedicineDetail()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    private fun addToCart() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.addToCart(
            productId, sessionManager.id.toString(), productQty, "medicine"
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
                    countN1++
                    if (countN1 <= 3) {
                        addToCart()
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
                            onBackPressed()
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