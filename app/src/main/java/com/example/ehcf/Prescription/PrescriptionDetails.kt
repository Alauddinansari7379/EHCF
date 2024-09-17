package com.example.ehcf.Prescription

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.currentDate
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetial
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetialDiagonsis
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetialDoctorNote
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetialLabTest
import com.example.ehcf.Prescription.model.ModelPreDetJava
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.databinding.ActivityPrescriptionDetailsBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.giphy.sdk.analytics.GiphyPingbacks.context
import com.rajat.pdfviewer.PdfViewerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PrescriptionDetails : AppCompatActivity() {
    private lateinit var binding: ActivityPrescriptionDetailsBinding
    private val context: Context = this@PrescriptionDetails
    var id = ""
    var doctorName = ""
    var date = ""
    var customerName = ""
    var titleName = ""
    var resultDate = ""
    var objective = ""
    var subjectiv = ""
    var doctorNote = ""
    var assesment = ""
    var plan = ""
    var doctorId = ""
    var followUp = ""
    var countR4 = 0
    var dataList: ArrayList<String>? = null
    private lateinit var sessionManager: SessionManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@PrescriptionDetails)
        val month = currentDate
        FollowUPMemberName = ""

        id = intent.getStringExtra("id").toString()
        doctorName = intent.getStringExtra("doctorName").toString()
        date = intent.getStringExtra("date").toString()
        PrescriptionDetails.FollowUP == ""
        val memberName = intent.getStringExtra("member_name").toString()
        val customerName = intent.getStringExtra("customer_name").toString()
        FollowUPMemberName = intent.getStringExtra("member_id").toString()

        Log.e("customerName", customerName)
        Log.e("memberName", memberName)
        Log.e("id", id)

        if (customerName != "") {
            binding.tvCoustmorNamePreDet.text = customerName
        } else {
            FollowUPMemberName = intent.getStringExtra("member_id").toString()
            Log.e("FollowUPMemberNameID", FollowUPMemberName)
        }

        //  specialitiesID = intent.getStringExtra("specialitiesID").toString()

        changeDateFormatFromAnother(date)
        Log.e("NewDate", resultDate)

        binding.tvDatePreDetialPreDetial.text = resultDate.toString()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallPreDet()


        binding.btnBookAppointmnet.setOnClickListener {
            FollowUP = "1"
            val intent = Intent(context as Activity, DoctorProfile::class.java)
                .putExtra("doctorId", doctorId)
                .putExtra("dashboard", "1")
            context.startActivity(intent)
        }
        binding.btnDownloadPrescription.setOnClickListener {
            if (customerName != null) {
                titleName = customerName
            } else {
                titleName = memberName
            }

            // completeSlot(bookingId)

            startActivity(
                // Use 'launchPdfFromPath' if you want to use assets file (enable "fromAssets" flag) / internal directory
                PdfViewerActivity.launchPdfFromUrl(           //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
                    context,
                    "https://ehcf.in/print/$id",                                // PDF URL in String format
                    titleName + "_Prescription",                        // PDF Name/Title in String format
                    "Prescription Save to directory",                  // If nothing specific, Put "" it will save to Downloads
                    enableDownload = true                    // This param is true by defualt.
                )
            )


//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ehcf.in/print/$id"))
//                    startActivity(browserIntent)


            //  startActivity(Intent(this@PrescriptionDetails,DownloadPrescription::class.java))
        }
        binding.tvDoctorNamePreDetial.text = doctorName
        binding.UHID.text = id
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    companion object {
        var Maxdate = ""
        var FollowUP = ""
        var FollowUPMemberName = ""
    }

    private fun changeDateFormatFromAnother(date: String?): String? {
        @SuppressLint("SimpleDateFormat")
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        @SuppressLint("SimpleDateFormat")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        resultDate = ""
        try {
            resultDate = outputFormat.format(inputFormat.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return resultDate
    }

    private fun apiCallPreDet() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.viewPrescriptionDetial(id)
            .enqueue(object : Callback<ModelPreDetJava> {
                @SuppressLint("LogNotTimber", "ResourceAsColor")
                override fun onResponse(
                    call: Call<ModelPreDetJava>, response: Response<ModelPreDetJava>
                ) {


                    try {

                        if (response.code() == 500) {
                            myToast(this@PrescriptionDetails, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.recyclerView.apply {
                                adapter = AdapterPrescriptionDetial(
                                    this@PrescriptionDetails,
                                    response.body()!!
                                )

                                for (i in response.body()!!.doctorNotes) {
                                    doctorNote = i.doctorNotes
                                    date = i.date
                                    assesment = i.assessment
                                    subjectiv = i.subjectiveInformation
                                    objective = i.objectiveInformation
                                    plan = i.plan
                                    doctorId = i.doctor_id
                                    if (i.end_follow_up_date != null) {
                                        followUp = i.end_follow_up_date
                                        Maxdate = followUp
                                    }
                                    val currentDate =
                                        SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(
                                            Date()
                                        )
                                    val followDate = followUp
                                    val followNew = followDate.replace("-", "")
                                    Log.e("followUp", followNew)
                                    Log.e("currentDate", currentDate)

                                    if (currentDate.toInt() > followNew.toInt()) {
                                        binding.btnBookAppointmnet.visibility = View.GONE
                                        binding.followUpdate.setTextColor(Color.parseColor("#F44336"))

                                    }
//                                 if (followDate=="10072023"){
//                                    binding.btnBookAppointmnet.visibility=View.VISIBLE
//                                }


                                }
                                changeDateFormatFromAnother(date)
                                binding.tvDatePreDetialPreDetial.text = resultDate
                                binding.tvPublicNote.text = doctorNote
                                binding.tvSubjective.text = subjectiv
                                binding.tvObjective.text = objective
                                binding.Plan.text = plan
                                binding.Assessment.text = assesment
                                binding.followUpdate.text = followUp


                            }
                            binding.recyclerViewDiagonosis.apply {
                                adapter = AdapterPrescriptionDetialDiagonsis(
                                    this@PrescriptionDetails,
                                    response.body()!!
                                )

                                AppProgressBar.hideLoaderDialog()

                            }
                            binding.recyclerViewLabTest.apply {
                                adapter = AdapterPrescriptionDetialLabTest(
                                    this@PrescriptionDetails,
                                    response.body()!!
                                )
//

                            }


                        }


                    } catch (e: java.lang.Exception) {
                        myToast(this@PrescriptionDetails, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelPreDetJava>, t: Throwable) {
                    countR4++
                    if (countR4 <= 3) {
                        apiCallPreDet()
                    } else {
                        myToast(this@PrescriptionDetails, t.message.toString())
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

}