package com.example.ehcf.Diagnostic

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Pharmacy.adapter.AdapterAddress
import com.example.ehcf.Pharmacy.model.ModelAddAddress
import com.example.ehcf.Pharmacy.model.ModelAddToCart
import com.example.ehcf.Pharmacy.model.ModelMedicine
import com.example.ehcf.PaymentMode
import com.example.ehcf.Pharmacy.adapter.AdapterCartDiagnostic
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityCartDiagnosticBinding
import com.example.ehcf.databinding.ActivityCartMedicineBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartDiagnostic : AppCompatActivity(), AdapterCartDiagnostic.Cart, AdapterAddress.AddressID,
    AdapterFamilyListView.CheckBox {
    private var context = this@CartDiagnostic
    val binding by lazy {
        ActivityCartDiagnosticBinding.inflate(layoutInflater)
    }
    var count = 0
    var countN = 0
    var countN1 = 0
    var countN2 = 0
    var countN3 = 0
    var dialog: Dialog? = null

    lateinit var sessionManager: SessionManager
    var totalPriceValue = ""
    var addressId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        apiCallTestCartList()
        apiCallAddressList()
        apiCallFamilyListNew()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            layoutAdd.setOnClickListener {
                startActivity(Intent(context, TestList::class.java))
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
                        .putExtra("slug", "test")
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

    private fun apiCallFamilyListNew() {
        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelFamilyList>,
                    response: Response<ModelFamilyList>
                ) {
                    // binding.rvSlotTiming.invalidate();
                    if (response.body()!!.status == 0) {
                        //myToast(context, "${response.body()!!.message}")
                    } else if (response.code() == 500) {
                        // myToast(this@MyAvailableSlot, "Server Error")
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.recyclerViewFamily.apply {
                            adapter = AdapterFamilyListView(
                                context,
                                response.body()!!,
                                this@CartDiagnostic
                            )
                        }
                    } else {
                        binding.recyclerViewFamily.apply {
                            //   adapter!!.notifyDataSetChanged();
                            //myToast(this@ShuduleTiming, response.body()!!.message)
                            adapter = AdapterFamilyListView(
                                context,
                                response.body()!!,
                                this@CartDiagnostic
                            )
                            binding.recyclerViewFamily.layoutManager = GridLayoutManager(context, 3)
                            //    binding.layoutFamilyMemeber.visibility=View.VISIBLE

                        }

                    }
                }


                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    myToast(context, "Something went wrong")
                }


            })
    }

    private fun apiCallTestCartList() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.cartListTest(
            sessionManager.id.toString(), "test"
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
                                adapter = AdapterCartDiagnostic(
                                    context,
                                    response.body()!!,
                                    this@CartDiagnostic
                                )
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
                            }
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.recyclerView.apply {
                                adapter = AdapterCartDiagnostic(
                                    context,
                                    response.body()!!,
                                    this@CartDiagnostic
                                )
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
                        apiCallTestCartList()
                    } else {
                        myToast(this@CartDiagnostic, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
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
                                    AdapterAddress(context, response.body()!!, this@CartDiagnostic)
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
                        myToast(this@CartDiagnostic, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()

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
                            pinCode,
                        )
                    } else {
                        myToast(this@CartDiagnostic, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })

    }

    override fun addToCart(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.addToCart(
            id, sessionManager.id.toString(), "1", "test"
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
                            apiCallTestCartList()
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
                        addToCart(id)
                    } else {
                        myToast(this@CartDiagnostic, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    override fun removeToCart(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.removeToCart(
            id, sessionManager.id.toString(), "test"

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
                            apiCallTestCartList()
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
                        myToast(this@CartDiagnostic, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    override fun addressId(id: String) {

        addressId = id

    }

    override fun checkBox(id: Int) {

    }

}