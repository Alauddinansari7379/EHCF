package com.example.ehcf.MyDoctor.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.MyDoctor.Adapter.AdapterMyDoctors
import com.example.ehcf.MyDoctor.Model.ModelMyDoctor
import com.example.ehcf.MyDoctor.Model.ResultMyDoctor
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityMyDoctorsBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL

class MyDoctors : AppCompatActivity(), AdapterMyDoctors.VideoCall {
    private lateinit var binding: ActivityMyDoctorsBinding
    private val context: Context = this@MyDoctors
     private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var mainData = ArrayList<ResultMyDoctor>()
    var countR = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        AdapterAllDoctor.dashboard = "1"
        shimmerFrameLayout = findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.imgRefresh.setOnClickListener {
            apiCallMyDoctors()
        }
        apiCallMyDoctors()


        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.doctor_name!!.contains(
                    str.toString(),
                    ignoreCase = true
                )
            } as ArrayList<ResultMyDoctor>)
        }
    }

    private fun videoCallFun(id: String) {
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = sessionManager.customerName
        jitsiMeetUserInfo.email = sessionManager.email
        try {
            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://jitsi.ethicalhealthcare.in/"))
                .setRoom(id)
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
            JitsiMeetActivity.launch(this@MyDoctors, defaultOptions)
            //  startActivity(Intent(requireContext(),Rating::class.java))
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
    }

    private fun apiCallMyDoctors() {



        ApiClient.apiService.myDoctors(sessionManager.id.toString())
            .enqueue(object :
                Callback<ModelMyDoctor> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyDoctor>,
                    response: Response<ModelMyDoctor>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!

                        }
                        if (response.code() == 500) {
                            myToast(this@MyDoctors, "Server Error")
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE

                        } else if (response.body()!!.result.isNotEmpty()) {
                            binding.rvAllDoctor.apply {
                                setRecyclerViewAdapter(mainData)
                                 binding.rvAllDoctor.adapter!!.notifyDataSetChanged()
                                binding.tvNoDataFound.visibility = View.GONE
                                shimmerFrameLayout?.startShimmer()
                                binding.rvAllDoctor.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                             }
                        } else {
                            setRecyclerViewAdapter(mainData)
                            myToast(this@MyDoctors, "No Doctor Found")
                         }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@MyDoctors, "Something went wrong")
                     }
                }

                override fun onFailure(call: Call<ModelMyDoctor>, t: Throwable) {
                    countR++
                    if (countR <= 3) {
                        apiCallMyDoctors()
                    } else {
                        myToast(this@MyDoctors, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }
            })

    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultMyDoctor>) {
        binding.rvAllDoctor.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvAllDoctor.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterMyDoctors(this@MyDoctors, data, this@MyDoctors)
        }
    }

    override fun videoCall(id: String) {
        SweetAlertDialog(this@MyDoctors, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Join?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)

                videoCallFun(id)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }
}