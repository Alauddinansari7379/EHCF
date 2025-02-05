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
import com.example.ehcf.Helper.AppProgressBar
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
     var dialog: Dialog? = null
    private var currentTime = ""
    val context=this@UpComingFragment
    var endTime = ""
    var hours = ""
    var minutes = ""
    var count = 0
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
     var shimmerFrameLayout: ShimmerFrameLayout? = null
    private val handler = Handler()
    private var mainData = ArrayList<ResultXXXX>()

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


        binding.imgRefresh.setOnClickListener {
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


    private fun videoCallFun(startTime: String, id: String) {
         meetingId = id
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = sessionManager.customerName
        jitsiMeetUserInfo.email = sessionManager.email
        try {
            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://jitsi.ethicalhealthcare.in/"))
                //.setServerURL(URL("https://jvc.ethicalhealthcare.in/"))
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
            Log.e("JitsiMeet", "Error launching Jitsi Meet", e)
            e.printStackTrace();
        }
    }

    override fun onResume() {
        super.onResume()
        if (ratingPage) {
            apiCallGetConsultationAccepted()
            //  (activity as Appointments).refresh()
            val intent = Intent(requireContext(), Rating::class.java)
                .putExtra("meetingId", meetingId)
            (requireContext()).startActivity(intent)
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



    private fun apiCallGetConsultationAccepted() {
       AppProgressBar.showLoaderDialog(requireContext())

        ApiClient.apiService.getConsultationAccpted(sessionManager.id.toString(), "accepted")
            .enqueue(object : Callback<ModelUpComingNew> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelUpComingNew>, response: Response<ModelUpComingNew>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!
                            AppProgressBar.hideLoaderDialog()
                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            HomeFragment.homeCall=""
                            setRecyclerViewAdapter(mainData)
                            binding.shimmer.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        }

                    } catch (e: Exception) {
                        AppProgressBar.hideLoaderDialog()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelUpComingNew>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallGetConsultationAccepted()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()

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

     @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        if (isOnline(requireContext())) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(requireContext())
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }

    }


}