package com.example.ehcf.Pharmacy.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.PaymentMode
import com.example.ehcf.Pharmacy.adapter.AdapterAddress
import com.example.ehcf.Pharmacy.adapter.AdapterCart
import com.example.ehcf.Pharmacy.model.ModelAddAddress
import com.example.ehcf.Pharmacy.model.ModelAddToCart
import com.example.ehcf.Pharmacy.model.ModelMedicine
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityCartMedicineBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartMedicine : AppCompatActivity(), AdapterCart.Cart, AdapterAddress.AddressID {
    private var context = this@CartMedicine
    val binding by lazy {
        ActivityCartMedicineBinding.inflate(layoutInflater)
    }
    var dialog: Dialog? = null

    lateinit var sessionManager: SessionManager
    var totalPriceValue = ""
    var addressId = ""
    var countN2 = 0
    var countN3 = 0
    var countN1 = 0
    var countN = 0
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        apiCallCartList()
        apiCallAddressList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            layoutAdd.setOnClickListener {
                startActivity(Intent(context, ProductListing::class.java))
            }

            btnGoToCheckout.setOnClickListener {
                if (addressId.isEmpty()) {
                    myToast(context, "Please Select Devivery Address")
                    return@setOnClickListener
                } else if (totalPriceValue.isNotEmpty()) {
                    sessionManager.bookingType = ""
                    PrescriptionDetails.FollowUP = ""
                    val intent = Intent(context as Activity, PaymentMode::class.java)
                        .putExtra("Medicine", "1")
                        .putExtra("slug", "medicine")
                        .putExtra("addressId", addressId)
                    context.startActivity(intent)
                } else {
                    myToast(context, "Cart Empty")
                    return@setOnClickListener
                }
            }
            tvAddAddress.setOnClickListener {
                val view =
                    layoutInflater.inflate(R.layout.dialog_add_address, null)
                dialog = Dialog(context)
                val address = view!!.findViewById<EditText>(R.id.edtAddress)
                val landmark = view.findViewById<EditText>(R.id.edtLandMark)
                val city = view.findViewById<EditText>(R.id.edtCity)
                val pinCode = view.findViewById<EditText>(R.id.edtPinCode)
                val imgClose = view.findViewById<ImageView>(R.id.imgCloseDil)
                val btnSubmitDil = view!!.findViewById<Button>(R.id.btnSubmitDil)
                dialog = Dialog(context)
                if (view.parent != null) {
                    (view.parent as ViewGroup).removeView(view) // <- fix
                }
                dialog!!.setContentView(view)
                dialog?.setCancelable(true)

                dialog?.show()

                imgClose.setOnClickListener {
                    dialog?.dismiss()
                }
                btnSubmitDil.setOnClickListener {
                    if (address.text.toString().isEmpty()) {
                        address.error = "Enter Address"
                        address.requestFocus()
                        return@setOnClickListener
                    }
                    if (landmark.text.toString().isEmpty()) {
                        landmark.error = "Enter Landmark"
                        landmark.requestFocus()
                        return@setOnClickListener
                    }
                    if (city.text.toString().isEmpty()) {
                        city.error = "Enter City"
                        city.requestFocus()
                        return@setOnClickListener
                    }
                    if (pinCode.text.toString().isEmpty()) {
                        pinCode.error = "Enter PinCode"
                        pinCode.requestFocus()
                        return@setOnClickListener
                    }
                    dialog?.dismiss()

                    apiCallAddAddress(
                        address.text.toString().trim(),
                        landmark.text.toString().trim(),
                        city.text.toString().trim(),
                        pinCode.text.toString().trim(),
                    )
                }
            }


        }

    }

    private fun apiCallCartList() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.cartList(
            sessionManager.id.toString(), "medicine"
        )
            .enqueue(object : Callback<ModelMedicine> {
                override fun onResponse(
                    call: Call<ModelMedicine>, response: Response<ModelMedicine>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            myToast(context, "No Product Found")

                            binding.recyclerView.apply {
                                adapter = AdapterCart(context, response.body()!!, this@CartMedicine)
                                binding.totalQty.text = response.body()!!.result.size.toString()
                                val price = ArrayList<Float>()
                                response.body()!!.result.forEach {
                                    price.add(it.total_price.toFloat())
                                }
                                binding.subTotalPrice.text = "₹ " + price.sum().toString()
                                binding.delivery.text = "₹ 100"
                                val sum = price.sum() + 100
                                binding.totalPrice.text = "₹ $sum".toString()
                                sessionManager.pricing = sum.toInt().toString()
                                totalPriceValue = sum.toString()
                                AppProgressBar.hideLoaderDialog()

                                val qty = ArrayList<Int>()
                                response.body()!!.result.forEach {
                                    qty.add(it.quantity.toInt())
                                }
                                BrowseMedicine.cartQty = qty.sum().toString()
                            }
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.recyclerView.apply {
                                adapter = AdapterCart(context, response.body()!!, this@CartMedicine)
                                binding.totalQty.text = response.body()!!.result.size.toString()
                                val price = ArrayList<Float>()
                                response.body()!!.result.forEach {
                                    price.add(it.total_price.toFloat())
                                }
                                binding.subTotalPrice.text = "₹ " + price.sum().toString()
                                binding.delivery.text = "₹ 100"
                                val sum = price.sum() + 100
                                binding.totalPrice.text = "₹ $sum".toString()
                                sessionManager.pricing = sum.toInt().toString()
                                totalPriceValue = sum.toString()
                                AppProgressBar.hideLoaderDialog()

                                val qty = ArrayList<Int>()
                                response.body()!!.result.forEach {
                                    qty.add(it.quantity.toInt())
                                }
                                BrowseMedicine.cartQty = qty.sum().toString()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelMedicine>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallCartList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun apiCallAddressList() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getAddress(
            sessionManager.id.toString()

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
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            myToast(context, "No Address Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.recyclerViewAddress.apply {
                                adapter =
                                    AdapterAddress(context, response.body()!!, this@CartMedicine)
                                AppProgressBar.hideLoaderDialog()

                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelMedicine>, t: Throwable) {
                    countN++
                    if (countN <= 3) {
                        apiCallAddressList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    private fun apiCallAddAddress(
        address: String,
        landmark: String,
        city: String,
        pinCode: String,

        ) {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.addAddress(
            sessionManager.id.toString(),
            address,
            landmark,
            city,
            pinCode,
        )
            .enqueue(object :
                Callback<ModelAddAddress> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelAddAddress>,
                    response: Response<ModelAddAddress>
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
                            refresh()
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelAddAddress>, t: Throwable) {
                    countN1++
                    if (countN1 <= 3) {
                        apiCallAddAddress(
                            address,
                            landmark,
                            city,
                            pinCode
                        )
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    override fun addToCart(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.incrementQuantity(
            id
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
                        addToCart(id)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    override fun removeToCart(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.removeToCart(
            id, sessionManager.id.toString(), "medicine"

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
                        removeToCart(id)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    override fun decreaseToCart(id: String) {

        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.decreamentQuantity(
            id
        ).enqueue(object :
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
                countN2++
                if (countN2 <= 3) {
                    decreaseToCart(id)
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
            }

        })
    }

    override fun addressId(id: String) {
        addressId = id
    }

}