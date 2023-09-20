package com.example.ehcf.Specialities.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
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

class DoctorProfile : AppCompatActivity(),AdapterDoctorProfile.CommentList {
    private lateinit var binding: ActivityDoctorProfileBinding
    var progressDialog: ProgressDialog? = null
    private var context: Context = this@DoctorProfile
    var doctorId = ""
    var dashboard = ""
    var consultationTypeId = ""
    var dialog: Dialog? = null
    var consaltationList = ArrayList<ModelConsaltation>()


    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        //         doctorId = intent.getStringExtra("doctorId").toString()

        AdapterFamilyListView.memberID=""

        doctorId = intent.getStringExtra("doctorId").toString()
       //  dashboard = intent.getStringExtra("dashboard").toString()

        if (AdapterAllDoctor.dashboard == "1") {
            AdapterAllDoctor.dashboard == ""
        binding.spinnerBookingType.visibility=View.VISIBLE
        }
        Log.e("doctorId", "$doctorId")
        Log.e("dashboard", "$dashboard")
        shimmerFrameLayout = findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();

        apiCallDoctorProfile()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
//        binding.bt.setOnClickListener{
//            val intent = Intent(context as Activity, ShuduleTiming::class.java)
//                .putExtra("doctorId",doctorId)
//            context.startActivity(intent)
//        }
        consaltationList.add(ModelConsaltation("Tele Consultation", "1"))
        consaltationList.add(ModelConsaltation("Clinic Visit ", "2"))
        consaltationList.add(ModelConsaltation("Home Visit", "3"))
        binding.spinnerBookingType.adapter = ArrayAdapter<ModelConsaltation>(
            context,
            android.R.layout.simple_list_item_1,
            consaltationList
        )


        binding.spinnerBookingType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    if (consaltationList.size > 0) {
                        consultationTypeId = consaltationList[i].id
                        sessionManager.bookingType = consultationTypeId
                        Log.e(ContentValues.TAG, "bloodGroup: $consultationTypeId")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }


    private fun apiCallDoctorProfile() {

        progressDialog = ProgressDialog(this@DoctorProfile)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        //progressDialog!!.show()

        Log.e("doctorId", "$doctorId")

        ApiClient.apiService.doctorProfile(doctorId)
            .enqueue(object :
                Callback<ModelDoctorProfile> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDoctorProfile>,
                    response: Response<ModelDoctorProfile>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.shimmer.visibility = View.GONE
                        myToast(this@DoctorProfile, "No Doctor Found")
                        progressDialog!!.dismiss()
                    } else {
                        binding.rvAllDoctor.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvAllDoctor.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            adapter = AdapterDoctorProfile(
                                this@DoctorProfile,
                                response.body()!!,
                                this@DoctorProfile
                            ,this@DoctorProfile)
                            progressDialog!!.dismiss()
                        }

                    }
                }

                override fun onFailure(call: Call<ModelDoctorProfile>, t: Throwable) {
                    binding.shimmer.visibility = View.GONE
                    myToast(this@DoctorProfile, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })

    }
    private fun apiCallCommentList() {
        progressDialog = ProgressDialog(this@DoctorProfile)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.doctorAllComments(doctorId)
            .enqueue(object :
                Callback<ModelCommentList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelCommentList>,
                    response: Response<ModelCommentList>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.shimmer.visibility = View.GONE
                        myToast(this@DoctorProfile, "No Review Found")
                        progressDialog!!.dismiss()
                    } else {
                        binding.recyclerViewComment.apply {
                            shimmerFrameLayout?.startShimmer()
                              adapter = AdapterCommentList(
                                this@DoctorProfile,
                                response.body()!!,
                                this@DoctorProfile
                            )
                            progressDialog!!.dismiss()
                        }

                    }
                }

                override fun onFailure(call: Call<ModelCommentList>, t: Throwable) {
                     myToast(this@DoctorProfile, "Something went wrong")
                    progressDialog!!.dismiss()

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

//    override fun consaltationType(doctorid: String) {
//        consaltationList.add(ModelConsaltation("Select Consultation Type ", "0"))
//        consaltationList.add(ModelConsaltation("Tele Consultation", "1"))
//        consaltationList.add(ModelConsaltation("Home Visit", "2"))
//        consaltationList.add(ModelConsaltation("Clinic Visit", "3"))
//
//        val view = layoutInflater.inflate(R.layout.dialog_consaltation_type, null)
//        dialog = Dialog(this@DoctorProfile)
//        val consaltationType = view!!.findViewById<Spinner>(R.id.spinnerConsaltaionTDilog)
//        val ok = view!!.findViewById<Button>(R.id.btnOkDialogConsaltation)
//        dialog = Dialog(this@DoctorProfile)
//
//        consaltationType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
//                if (consaltationList.size > 0) {
//                    consultationTypeId = consaltationList[i].id.toString()
//                    Log.e(ContentValues.TAG, "consultationTypeId: $consultationTypeId")
//                }
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        }
//        consaltationType.adapter = ArrayAdapter<ModelConsaltation>(
//            context,
//            android.R.layout.simple_list_item_1,
//            consaltationList
//        )
//
//        if (view.parent != null) {
//            (view.parent as ViewGroup).removeView(view) // <- fix
//        }
//        dialog!!.setContentView(view)
//        dialog?.setCancelable(true)
//
//
//        dialog?.show()
//        ok.setOnClickListener {
//            sessionManager.bookingType = consultationTypeId
//            val intent = Intent(context as Activity, DoctorProfileNew::class.java)
//                .putExtra("doctorId", doctorid)
//            context.startActivity(intent)
//            dialog?.dismiss()
//        }
//    }


}