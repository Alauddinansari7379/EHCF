package com.example.ehcf.Appointments.UpComing.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ehcf.Appointments.UpComing.adapter.AdapterUpComing
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingResponse
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentUpComingBinding
import com.example.ehcf.retrofit.ApiInterface
import com.example.ehcf.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UpComingFragment : Fragment(),AdapterUpComing.ShowPopUp {
    private lateinit var binding:FragmentUpComingBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog : ProgressDialog?=null
    var dialog: Dialog?= null
    private var tvTimeCounter: TextView?=null

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

        apiCall()
        val btnOkDialog = view.findViewById<Button>(R.id.btnOkDialog)
        val btnCheck = view.findViewById<Button>(R.id.btnCheck)
        tvTimeCounter = view.findViewById<TextView>(R.id.tvTimeCounter)
        binding.imgRefresh.setOnClickListener {
            apiCall()

            var view = layoutInflater.inflate(R.layout.time_dialognew, null)

            val btnOkDialog = view.findViewById<Button>(R.id.btnOkDialog)

        btnOkDialog.setOnClickListener {
            dialog?.dismiss()
        }

        }
//       btnCheck.setOnClickListener {
//            dialog=   Dialog(requireContext())
//            val btnOkDialog = view.findViewById<Button>(R.id.btnOkDialog)
//            if (view.parent != null) {
//                (view.parent as ViewGroup).removeView(view) // <- fix
//            }
//            dialog!!.setContentView(view)
//            dialog?.setCancelable(false)
//            // dialog?.setContentView(view)
//
//            dialog?.show()
//            timeCounter()
//        }


    }
    override fun showPopup(){
        var view = layoutInflater.inflate(R.layout.time_dialognew, null)
        dialog = Dialog(requireContext())

        val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialog)

        dialog=   Dialog(requireContext())
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)

        dialog?.show()
        btnOkDialog.setOnClickListener {
            dialog?.dismiss()
        }
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
                tvTimeCounter!!.text = "00"+":05:" + millisUntilFinished / 1000 + " " + ""
            }
            override fun onFinish() {
//                binding.tvResend.isClickable = true
//                binding.tvResend.setTextColor(Color.parseColor("#9F367A"))

            }
        }.start()
    }
    private fun apiCall(){

        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val id="20"

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.thedemostore.in/api/customer/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.myAppointmentUpComing(id)
        retrofitData.enqueue(object : Callback<ModelUpComingResponse> {
            override fun onResponse(
                call: Call<ModelUpComingResponse>,
                response: Response<ModelUpComingResponse>
            )
            {
//                myToast(requireActivity(),response.body()!!.message)
//                progressDialog!!.dismiss()

                // val recyclerView = findViewById<RecyclerView>(R.id.rvCancled)
                binding.rvCancled.apply {
                    adapter = AdapterUpComing(requireContext(), response.body()!!,this@UpComingFragment)
                    progressDialog!!.dismiss()

                }
            }

            override fun onFailure(call: Call<ModelUpComingResponse>, t: Throwable) {
                t.message?.let { myToast(requireActivity(), it)
                    progressDialog!!.dismiss()

                }
            }
        })
    }

}