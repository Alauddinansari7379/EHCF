package com.example.ehcf.Specialities.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.RatingAndReviews.adapter.AdapterCommentList
import com.example.ehcf.Specialities.adapter.AdapterDoctorProfile
import com.example.ehcf.Specialities.model.ModelCommentList
import com.example.ehcf.Specialities.model.ModelConsaltation
import com.example.ehcf.Specialities.model.ModelDoctorProfile
import com.example.ehcf.databinding.ActivityDoctorProfileBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.util.ArrayList

class DoctorProfile : AppCompatActivity(), AdapterDoctorProfile.CommentList {
    private lateinit var binding: ActivityDoctorProfileBinding
    private var context = this@DoctorProfile
    var doctorId = ""
    var dashboard = ""
    var countR = 0
    var countC = 0
    var dialog: Dialog? = null


    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        if (DoctorProfile.consaltationList.size == 0) {
            DoctorProfile.consaltationList.add(ModelConsaltation("Tele Consultation", "1"))
            DoctorProfile.consaltationList.add(ModelConsaltation("Clinic Visit ", "2"))
            DoctorProfile.consaltationList.add(ModelConsaltation("Home Visit", "3"))
        }

        //         doctorId = intent.getStringExtra("doctorId").toString()

        AdapterFamilyListView.memberID = ""

        doctorId = intent.getStringExtra("doctorId").toString()
        //  dashboard = intent.getStringExtra("dashboard").toString()

//
//        if (AdapterAllDoctor.dashboard == "1") {
//            AdapterAllDoctor.dashboard == ""
//            binding.spinnerBookingType.visibility = View.VISIBLE
//        }
        Log.e("doctorId", "$doctorId")
        Log.e("dashboard", "$dashboard")
        shimmerFrameLayout = findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();


        binding.spinnerBookingType.adapter = ArrayAdapter<ModelConsaltation>(
            context,
            android.R.layout.simple_list_item_1,
            DoctorProfile.consaltationList
        )


        binding.spinnerBookingType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    if (DoctorProfile.consaltationList.size > 0) {
                        consultationTypeId = DoctorProfile.consaltationList[i].id
                        sessionManager.bookingType = consultationTypeId
                        Log.e(ContentValues.TAG, "consultationTypeId: $consultationTypeId")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }



        apiCallDoctorProfile()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }

    companion object {
        var consaltationList = ArrayList<ModelConsaltation>()

        var consultationTypeId = ""

    }

    private fun apiCallDoctorProfile() {


        Log.e("doctorId", "$doctorId")

        ApiClient.apiService.doctorProfile(doctorId)
            .enqueue(object :
                Callback<ModelDoctorProfile> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDoctorProfile>,
                    response: Response<ModelDoctorProfile>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            myToast(this@DoctorProfile, "No Doctor Found")
                        } else {


                            binding.rvAllDoctor.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.rvAllDoctor.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                adapter = AdapterDoctorProfile(
                                    this@DoctorProfile,
                                    response.body()!!,
                                    this@DoctorProfile, this@DoctorProfile
                                )
                            }


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@DoctorProfile, "Something went wrong")
                    }
                }

                override fun onFailure(call: Call<ModelDoctorProfile>, t: Throwable) {
                    binding.shimmer.visibility = View.GONE
                    countR++
                    if (countR <= 3) {
                        apiCallDoctorProfile()
                    } else {
                        myToast(this@DoctorProfile, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })

    }

    private fun apiCallCommentList() {
        AppProgressBar.showLoaderDialog(context)


        ApiClient.apiService.doctorAllComments(doctorId)
            .enqueue(object :
                Callback<ModelCommentList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelCommentList>,
                    response: Response<ModelCommentList>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            myToast(this@DoctorProfile, "No Review Found")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            binding.recyclerViewComment.apply {
                                shimmerFrameLayout?.startShimmer()
                                adapter = AdapterCommentList(
                                    this@DoctorProfile,
                                    response.body()!!,
                                    this@DoctorProfile
                                )
                                AppProgressBar.hideLoaderDialog()
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@DoctorProfile, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelCommentList>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    countC++
                    if (countC <= 3) {
                        apiCallCommentList()
                    } else {
                        myToast(this@DoctorProfile, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })

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

    override fun commentList() {
        apiCallCommentList()
    }


}