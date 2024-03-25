package com.example.ehcf.Appointments.UpComing.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.wearable.view.RecyclerViewMergeAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Appointments.Appointments
import com.example.ehcf.Appointments.UpComing.adapter.AdapterAppointments
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingNew
import com.example.ehcf.Appointments.UpComing.model.ResultXXXX
import com.example.ehcf.Fragment.HomeFragment
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.RatingAndReviews.Rating
import com.example.ehcf.databinding.FragmentUpComingBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class UpComingFragment : Fragment(), AdapterAppointments.ShowPopUp {
    private lateinit var binding: FragmentUpComingBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog: ProgressDialog? = null
    var dialog: Dialog? = null
    private var currentTime = ""
    var endTime = ""
    var hours = ""
    var minutes = ""
    var secondsNew = ""
    private var diffTime: Long? = null
    private var diffTimeSeconds: Long? = null
    var bookingId = ""
    var d1: Date? = null
    var d2: Date? = null
    var ratingPage = false
    var refresh = false
    private var tvTimeCounter: TextView? = null
    var meetingId = ""
    var mergeAdapter = RecyclerViewMergeAdapter()
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private val handler = Handler()
    private var mainData = ArrayList<ResultXXXX>()

    //    private val broadcastReceiver = object : BroadcastReceiver(requireContext()) {
////        override fun onReceive(context: this?, intent: Intent?) {
////            onBroadcastReceived(intent)
////        }
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_up_coming, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpComingBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();

        apiCallGetConsultationAccepted()
       // handler.post(updateTimeRunnable)

//        GlobalScope.launch {
//            // Run the function every 5 seconds
//            while (true) {
//                apiCallGetConsultationAccepted()
//                delay(5000) // Delay for 5 seconds
//            }
//        }



        val btnCheck = view.findViewById<Button>(R.id.btnCheck)
        // tvTimeCounter = view.findViewById<TextView>(R.id.tvTimeCounter)

        binding.imgRefresh.setOnClickListener {
            // apiCall()
            //  apiCallAppointmentsWaiting()
            //  apiCallGetConsultationAccepted()
            (activity as Appointments).refresh()
        }
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationAccepted()
        }
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.doctor_name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultXXXX>)
        }


//
//        binding.imgSearch.setOnClickListener {
//            if (binding.edtSearch.text.toString().isEmpty()) {
//                binding.edtSearch.error = "Enter Doctor Name"
//                binding.edtSearch.requestFocus()
//            } else {
//                val search = binding.edtSearch.text.toString()
//                apiCallSearchAppointments(search)
//            }
//        }
    }
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
       apiCallGetConsultationAccepted()
            handler.postDelayed(this, 10000) // Update every 1000 milliseconds (1 second)
        }

    }

    private fun remainingTime() {
        val format = SimpleDateFormat("yy/MM/dd HH:m:ss")

        try {
            d1  = format.parse(currentTime)
            d2 = format.parse(endTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.e("d1", d1!!.time.toString())
        Log.e("d2", d2!!.time.toString())

        diffTime = (d2!!.time - d1!!.time)
        diffTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(diffTime!!)

        Log.e("minnew1", diffTimeSeconds.toString())

    }

    override fun popupRemainingTime(startTime: String) {
        var view = layoutInflater.inflate(R.layout.time_dialognew, null)
        dialog = Dialog(requireContext())
        endTime = startTime
        val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialogNew)
        val hour = view!!.findViewById<TextView>(R.id.tvHourTime)
        val minute = view!!.findViewById<TextView>(R.id.tvMinuteTime)
        val second = view!!.findViewById<TextView>(R.id.tvSecondTime)
        dialog = Dialog(requireContext())

        currentTime = SimpleDateFormat("yy/MM/dd HH:m:ss", Locale.getDefault()).format(Date())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)
        // val d1 = format.parse("2023/03/29 11:04:00")
//        Log.e("currentDate", currentTime)
//        Log.e("EndTime", startTime)

        remainingTime()
        fun timeCalculator(seconds: Long) {
            print(seconds)
            hours = (seconds / 3600).toInt().toString()
            minutes = (seconds % 3600 / 60).toInt().toString()
            secondsNew = (seconds % 3600 % 60).toInt().toString()

            hour.text = hours
            minute.text = minutes
            second.text = secondsNew

            if (second.text.contains("-")){
                apiCallGetConsultationAccepted()

            }
            println("Hours: $hours")
            println("Minutes: $minutes")
            println("Seconds: $seconds")
        }

        timeCalculator(diffTimeSeconds!!)

        dialog?.show()
        btnOkDialog.setOnClickListener {
            dialog?.dismiss()
        }
    }
//    private fun apiCallSearchAppointments(doctorName: String) {
//        progressDialog = ProgressDialog(requireContext())
//        progressDialog!!.setMessage("Loading..")
//        progressDialog!!.setTitle("Please Wait")
//        progressDialog!!.isIndeterminate = false
//        progressDialog!!.setCancelable(true)
//        progressDialog!!.show()
//
//
//        ApiClient.apiService.searchAppointments(sessionManager.id.toString(),doctorName,"accepted")
//            .enqueue(object : Callback<ModelUpComingNew> {
//                @SuppressLint("LogNotTimber")
//                override fun onResponse(
//                    call: Call<ModelUpComingNew>, response: Response<ModelUpComingNew>
//                ) {
//                    if (response.code() == 500) {
//                        myToast(requireActivity(), "Server Error")
//                        binding.shimmer.visibility = View.GONE
//                    } else if (response.body()!!.status == 0) {
//                        binding.tvNoDataFound.visibility = View.VISIBLE
//                        binding.shimmer.visibility = View.GONE
//                        binding.edtSearch.text.clear()
//                        myToast(requireActivity(), "${response.body()!!.message}")
//                        progressDialog!!.dismiss()
//
//                    } else if (response.body()!!.result.isEmpty()) {
//                        binding.rvCancled.adapter =
//                            activity?.let { AdapterAppointments(it, response.body()!!,this@UpComingFragment) }
//                        binding.rvCancled.adapter!!.notifyDataSetChanged()
//                        binding.tvNoDataFound.visibility = View.VISIBLE
//                        binding.shimmer.visibility = View.GONE
//                        binding.edtSearch.text.clear()
//                        myToast(requireActivity(), "No Appointment Found")
//                        progressDialog!!.dismiss()
//
//                    } else {
//                        binding.rvCancled.adapter =
//                            activity?.let { AdapterAppointments(it, response.body()!!,this@UpComingFragment) }
//                        binding.rvCancled.adapter!!.notifyDataSetChanged()
//                        binding.tvNoDataFound.visibility = View.GONE
//                        shimmerFrameLayout?.startShimmer()
//                        binding.rvCancled.visibility = View.VISIBLE
//                        binding.shimmer.visibility = View.GONE
//                        binding.edtSearch.text.clear()
//                        progressDialog!!.dismiss()
////                        binding.rvManageSlot.apply {
////                            binding.tvNoDataFound.visibility = View.GONE
////                            shimmerFrameLayout?.startShimmer()
////                            binding.rvManageSlot.visibility = View.VISIBLE
////                            binding.shimmerMySlot.visibility = View.GONE
////                            // myToast(this@ShuduleTiming, response.body()!!.message)
////                            adapter = AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
////                            progressDialog!!.dismiss()
////
////                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<ModelUpComingNew>, t: Throwable) {
//                    myToast(requireActivity(), "Something went wrong")
//                    binding.shimmer.visibility = View.GONE
//                    progressDialog!!.dismiss()
//
//                }
//
//            })
//    }

    private fun videoCallFun(startTime: String, id: String) {
         meetingId = id
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = sessionManager.customerName
        jitsiMeetUserInfo.email = sessionManager.email
        try {
            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://jvc.ethicalhealthcare.in/"))
                .setRoom(startTime)
                .setAudioMuted(false)
                .setVideoMuted(true)
                .setAudioOnly(false)
                .setUserInfo(jitsiMeetUserInfo)
                .setConfigOverride("enableInsecureRoomNameWarning", false)
                .setFeatureFlag("readOnlyName", true)
                .setFeatureFlag("prejoinpage.enabled", false)
              //  .setFeatureFlag("lobby-mode.enabled", false)
               // .setToken("123") // Set the meeting password
                //.setFeatureFlag("autoKnockLobby", false) // Disable lobby mode
                //.setFeatureFlag("disableModeratorIndicator", false)
                //.setFeatureFlag("chat.enabled",false)
                .setConfigOverride("requireDisplayName", true)
                .build()
            JitsiMeetActivity.launch(requireContext(), defaultOptions)
            ratingPage = true
            //  startActivity(Intent(requireContext(),Rating::class.java))
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
    }

    override fun onResume() {
        super.onResume()
        if (ratingPage) {
            apiCallGetConsultationAccepted()
            //  (activity as Appointments).refresh()
            val intent = Intent(context as Activity, Rating::class.java)
                .putExtra("meetingId", meetingId)
            (context as Activity).startActivity(intent)
            ratingPage = false
        }
        if (refresh){
            refresh=false
           // myToast(requireActivity(),"ONResume")
            if (Appointments.tabIndex==0){
                apiCallGetConsultationAccepted()
            }

        }

    }

    override fun onStop() {
        super.onStop()
        refresh=true
      //  myToast(requireActivity(),"ONStop")
    }

    override fun videoCall(startTime: String, id: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Join Meeting?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)

                videoCallFun(startTime, id)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }


//    override fun dismissPopup(){
//        val view = layoutInflater.inflate(R.layout.time_dialognew, null)
//        val btnOkDialog = view.findViewById<Button>(R.id.btnOkDialog)
//        btnOkDialog.setOnClickListener {
//            dialog?.dismiss()
//        }


    private fun timeCounter() {
        object : CountDownTimer(30000, 1000) {
            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tvTimeCounter!!.text = "00" + ":05:" + millisUntilFinished / 1000 + " " + ""
            }

            override fun onFinish() {
//                binding.tvResend.isClickable = true
//                binding.tvResend.setTextColor(Color.parseColor("#9F367A"))

            }
        }.start()
    }

    /*    private fun apiCall(){

            progressDialog = ProgressDialog(requireContext())
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()
            val id="20"

            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                //.baseUrl("https://jsonplaceholder.typicode.com/")
                .baseUrl("https://ehcf.thedemostore.in/api/customer/")
                .build()
                .create(ApiInterface::class.java)

            val retrofitData = retrofitBuilder.myAppointmentUpComing(sessionManager.id.toString())
            retrofitData.enqueue(object : Callback<ModelUpComingResponse> {
                override fun onResponse(
                    call: Call<ModelUpComingResponse>,
                    response: Response<ModelUpComingResponse>
                )
                {
                    if (response.body()!!.result.upcoming.isEmpty()){
                        binding.tvNoDataFound.visibility = View.VISIBLE
                       // myToast(requireActivity(),"No Appointment Found")
                        progressDialog!!.dismiss()

                    }else{
                        binding.rvCancled.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterUpComing(requireContext(), response.body()!!,this@UpComingFragment)
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelUpComingResponse>, t: Throwable) {
                    t.message?.let { myToast(requireActivity(), it)
                        progressDialog!!.dismiss()

                    }
                }
            })
        }*/
//    private fun apiCallAppointmentsWaiting() {
//
//
//        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "waiting_for_accept")
//            .enqueue(object : Callback<ModelAppointmentBySlag> {
//                @SuppressLint("LogNotTimber")
//                override fun onResponse(
//                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
//                ) {
//                    Log.e("Ala", "${response.body()!!}")
//                    Log.e("Ala", "${response.body()!!.status}")
//                    if (response.code() == 500) {
//                        myToast(requireActivity(), "Server error")
//                    } else if (response.body()!!.result.isEmpty()) {
//                        // myToast(requireActivity(),"No Data Found")
//                    } else {
//                        mergeAdapter.removeAdapter(
//                            AdapterAppointments(
//                                requireContext(),
//                                response.body()!!,
//                                this@UpComingFragment
//                            )
//                        )
//
//                        val adapter1 = AdapterAppointments(
//                            requireContext(),
//                            response.body()!!,
//                            this@UpComingFragment
//                        )
//
//                        // Setting the Adapter with the recyclerview
//                        // binding.rvUpcoming.adapter = adapter
//
//                        mergeAdapter.addAdapter(adapter1)
////                        binding.rvUpcoming.apply {
////                            binding.tvNoDataFound.visibility = View.GONE
////                            adapter = AdapterUpComing(requireContext(), response.body()!!,this@UpComingFragment)
////                            progressDialog!!.dismiss()
////
////                        }
//                    }
//
//
//                }
//
//                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
//                    myToast(requireActivity(), "Something went wrong")
//                    progressDialog!!.dismiss()
//
//                }
//
//            })
//    }

    private fun apiCallGetConsultationAccepted() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.getConsultationAccpted(sessionManager.id.toString(), "accepted")
            .enqueue(object : Callback<ModelUpComingNew> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelUpComingNew>, response: Response<ModelUpComingNew>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!
                            progressDialog!!.dismiss()

                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server error")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            progressDialog!!.dismiss()
                        } else {
                            HomeFragment.homeCall=""
                            setRecyclerViewAdapter(mainData)
                            binding.shimmer.visibility = View.GONE
                            progressDialog!!.dismiss()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelUpComingNew>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultXXXX>) {
        binding.rvCancled.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvCancled.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterAppointments(requireContext(), data, this@UpComingFragment)
        }
    }

    /*//    private fun apiCallAppointments() {
    //        progressDialog = ProgressDialog(requireContext())
    //        progressDialog!!.setMessage("Loading...")
    //        progressDialog!!.setTitle("Please Wait")
    //        progressDialog!!.isIndeterminate = false
    //        progressDialog!!.setCancelable(true)
    //        progressDialog!!.show()
    //
    //        ApiClient.apiService.appointments(sessionManager.id.toString())
    //            .enqueue(object : Callback<ModelAppointments> {
    //                @SuppressLint("LogNotTimber")
    //                override fun onResponse(
    //                    call: Call<ModelAppointments>, response: Response<ModelAppointments>
    //                ) {
    //                    Log.e("Ala", "${response.body()!!}")
    //                    Log.e("Ala", "${response.body()!!.status}")
    //                    if (response.body()!!.result.isEmpty()){
    //                        binding.tvNoDataFound.visibility = View.VISIBLE
    //                        // myToast(requireActivity(),"No Appointment Found")
    //                        progressDialog!!.dismiss()
    //
    //                    }else if (response.code()==500){
    //                        myToast(requireActivity(),"Server Error")
    //                        progressDialog!!.dismiss()
    //                    }
    //
    //                    else{
    //                        binding.rvCancled.apply {
    //                            binding.tvNoDataFound.visibility = View.GONE
    //                            adapter = AdapterAppointments(requireContext(), response.body()!!,this@UpComingFragment)
    //                            progressDialog!!.dismiss()


        //                        binding.rvCancled.apply {
    ////                            binding.rvUpcoming.adapter = null;
    //                            shimmerFrameLayout?.startShimmer()
    //                            binding.rvCancled.visibility = View.VISIBLE
    //                            binding.shimmer.visibility = View.GONE
    //                            binding.tvNoDataFound.visibility = View.GONE
    //                            val adapter = AdapterAppointmentsAccepted(requireContext(), response.body()!!, this@UpComingFragment)
    //
    //                            mergeAdapter?.addAdapter(adapter)
    //                            //binding.rvUpcoming.adapter = adapter
    //
    //                            binding.rvCancled.adapter = mergeAdapter
        //   progressDialog!!.dismiss()
        // adapter = mergeAdapter
    //
    //                        }
    //                    }
    //
    //
    //                }
    //
    //                override fun onFailure(call: Call<ModelAppointments>, t: Throwable) {
    //                    myToast(requireActivity(), "Something went wrong")
    //                    progressDialog!!.dismiss()
    //
    //                }
    //
    //            })
    //    }*/
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        if (isOnline(requireContext())) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
//        CheckInternet().check { connected ->
//            if (connected) {
//             //    myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
    }


}