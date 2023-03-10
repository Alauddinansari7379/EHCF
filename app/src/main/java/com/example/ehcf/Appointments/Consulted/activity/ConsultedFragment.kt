package com.example.ehcf.Appointments.Consulted.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ehcf.Appointments.Cancelled.adapter.AdapterCancelled
import com.example.ehcf.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf.Appointments.Consulted.model.ModelConsultedResponse
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentConsultedBinding
import com.example.ehcf.retrofit.ApiInterface
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class ConsultedFragment : Fragment() {
    private lateinit var binding: FragmentConsultedBinding
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consulted, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConsultedBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        apiCallGetConsultationCompleted()
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationCompleted()
        }
    }


    private fun apiCallGetConsultationCompleted() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "completed")
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    Log.e("Ala", "${response.body()!!}")
                    Log.e("Ala", "${response.body()!!.status}")
                    if (response.code() == 500) {
                        myToast(requireActivity(), "Server Error")
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Appointment Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvCancled.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterConsulted(requireContext(), response.body()!!)
                            progressDialog!!.dismiss()

                        }
                    }


                }

                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    /*
        private fun apiCall(){

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

            val retrofitData = retrofitBuilder.myAppointmentConsulted(sessionManager.id.toString())
            retrofitData.enqueue(object : Callback<ModelConsultedResponse> {
                override fun onResponse(
                    call: Call<ModelConsultedResponse>,
                    response: Response<ModelConsultedResponse>
                )
                {
                    if (response.body()!!.result.completed.isEmpty()){
                        binding.tvNoDataFound.visibility = View.VISIBLE
                       // myToast(requireActivity(),"No Appointment Found")
                        progressDialog!!.dismiss()

                    }else{
                        binding.rvCancled.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterConsulted(requireContext(), response.body()!!)
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelConsultedResponse>, t: Throwable) {
                    myToast(requireActivity(),"Something went wrong")
                        progressDialog!!.dismiss()


                }
            })
        }
    */
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