package com.example.ehcf.Dashboard.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.widget.addTextChangedListener
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Dashboard.adapter.AdapterFilteredDoctor
import com.example.ehcf.Dashboard.adapter.AdapterNearByDoctor
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.Dashboard.modelResponse.ModelScarchByLocationAndSpc
import com.example.ehcf.Dashboard.modelResponse.ModelSpecilList
import com.example.ehcf.Fragment.HomeFragment
import com.example.ehcf.Fragment.Model.ModelNearByDoctor
import com.example.ehcf.Fragment.Model.ResultX
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.Notification.Interface.apiInterface
import com.example.ehcf.databinding.ActivityDashboard2Binding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import kotlin.collections.ArrayList

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboard2Binding
    private val context: Context = this@Dashboard
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var specilistId = ""
    var count = 0
    var countN = 0
    var mainData = ArrayList<ResultX>()
    private var specilList = ModelSpecilList();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboard2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        shimmerFrameLayout = findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        try {
            binding.edtLocation.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.clinic_address != null && it.clinic_address!!.contains(
                        str.toString(),
                        ignoreCase = true
                    )
                } as ArrayList<ResultX>)
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }

        sessionManager = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed(200) {
            apiCallSpecialistSpinner1()
            apiCallNearByDoctor()


        }

        binding.btnSignIn.setOnClickListener {
            apiCallSearch()
        }
    }

    private fun apiCallSpecialistSpinner1() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.in/api/doctor/")
            .build()
            .create(apiInterface::class.java)
        val retrofitData = retrofitBuilder.specialistCategory()
        retrofitData.enqueue(object : Callback<ModelSpecilList> {
            override fun onResponse(
                call: Call<ModelSpecilList>,
                response: Response<ModelSpecilList>
            ) {

                specilList = response.body()!!;
                if (specilList != null) {

                    //spinner code start
                    val items = arrayOfNulls<String>(specilList.result!!.size)

                    for (i in specilList.result!!.indices) {

                        items[i] = specilList.result!![i].categoryName
                    }
                    val adapter: ArrayAdapter<String?> =
                        ArrayAdapter(this@Dashboard, android.R.layout.simple_list_item_1, items)
                    var spProvince: SmartMaterialSpinner<String>? = null
                    var spEmptyItem: SmartMaterialSpinner<String>? = null
                    binding.spinnerSpecialistTest.item = items.toMutableList() as List<Any>?

//                    progressDialog!!.dismiss()

                    binding.spinnerSpecialistTest.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View,
                                i: Int,
                                l: Long
                            ) {
                                val id = specilList.result!![i].id
                                specilistId = id.toString()
                                // Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                        }

                }
            }

            override fun onFailure(call: Call<ModelSpecilList>, t: Throwable) {
                t.message?.let { myToast(this@Dashboard, it) }
            }
        })
    }


    private fun apiCallSearch() {
        AppProgressBar.showLoaderDialog(context)
        val location = binding.edtLocation.text.toString().trim()

        ApiClient.apiService.searchByLocationAndSpec(location, specilistId)
            .enqueue(object :
                Callback<ModelScarchByLocationAndSpc> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelScarchByLocationAndSpc>,
                    response: Response<ModelScarchByLocationAndSpc>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.rvAllDoctor.adapter =
                            AdapterFilteredDoctor(this@Dashboard, response.body()!!)
                        //  binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                        binding.shimmer.visibility = View.GONE
                        myToast(this@Dashboard, "No Doctor Found")
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        binding.rvAllDoctor.adapter =
                            AdapterFilteredDoctor(this@Dashboard, response.body()!!)
                        binding.shimmer.visibility = View.GONE

                        AppProgressBar.hideLoaderDialog()
                        //   binding.rvManageSlot.adapter!!.notifyDataSetChanged()

                    }
                }


                override fun onFailure(
                    call: Call<ModelScarchByLocationAndSpc>,
                    t: Throwable
                ) {
                    count++
                    if (count <= 3) {
                        apiCallSearch()
                    } else {
                        myToast(this@Dashboard, "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultX>) {
        binding.rvAllDoctor.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvAllDoctor.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            adapter = AdapterNearByDoctor(this@Dashboard, data)

        }
    }

     override fun onStart() {
        super.onStart()
        CheckInternet().check { connected ->
            if (connected) {

                // myToast(requireActivity(),"Connected")
            } else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }

    private fun apiCallNearByDoctor() {

        ApiClient.apiService.getNearByDoctor(
            HomeFragment.postalCodeNew,

            ).enqueue(object :
            Callback<ModelNearByDoctor> {
            @SuppressLint("LogNotTimber", "SuspiciousIndentation")
            override fun onResponse(
                call: Call<ModelNearByDoctor>,
                response: Response<ModelNearByDoctor>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Dashboard, "Unable to fetch nearest doctor")
                    } else if (response.body()!!.status == 1) {
                        mainData = response.body()!!.result
                        setRecyclerViewAdapter(mainData)
                    } else {
                        binding.shimmer.visibility = View.GONE

                        myToast(this@Dashboard, response.body()!!.message.toString())
                        //  progressDialog!!.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("Exception", e.printStackTrace().toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ModelNearByDoctor>, t: Throwable) {
                myToast(this@Dashboard, "${t.message}")
                countN++
                if (countN <= 3) {
                    apiCallNearByDoctor()
                } else {
                    myToast(this@Dashboard, t.message.toString())
                    binding.shimmer.visibility = View.GONE
                    AppProgressBar.hideLoaderDialog()

                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }



}