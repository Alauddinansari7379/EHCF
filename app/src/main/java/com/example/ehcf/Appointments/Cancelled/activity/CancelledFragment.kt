package com.example.ehcf.Appointments.Cancelled.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ehcf.Appointments.Cancelled.adapter.AdapterCancelled
import com.example.ehcf.Appointments.Cancelled.model.ModelCancelled
import com.example.ehcf.Appointments.UpComing.adapter.AdapterAppointments
import com.example.ehcf.Appointments.UpComing.adapter.AdapterAppointmentsAccepted
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Appointments.UpComing.model.ModelAppointments
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentCancelledBinding
import com.example.ehcf.retrofit.ApiInterface
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CancelledFragment : Fragment() {
    private lateinit var binding:FragmentCancelledBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog : ProgressDialog?=null
    var dialog: Dialog?= null
    private var tvTimeCounter: TextView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cancelled, container, false)
    }
    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCancelledBinding.bind(view)
        sessionManager = SessionManager(requireContext())
       // apiCall()
        apiCallGetConsultationRejected()
        val btnOkDialog = view.findViewById<Button>(R.id.btnOkDialog)
        val btnCheck = view.findViewById<Button>(R.id.btnCheck)
        tvTimeCounter = view.findViewById<TextView>(R.id.tvTimeCounter)
        binding.imgRefresh.setOnClickListener {
           // apiCall()
            apiCallGetConsultationRejected()

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

    private fun apiCallGetConsultationRejected() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.getConsultation(sessionManager.id.toString(),"rejected")
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    Log.e("Ala", "${response.body()!!}")
                    Log.e("Ala", "${response.body()!!.status}")
                    if (response.body()!!.result.isEmpty()){
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Appointment Found")
                        progressDialog!!.dismiss()

                    }else{
                        binding.rvCancled.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterCancelled(requireContext(), response.body()!!)
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
    private fun apiCallAppointments() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.appointments(sessionManager.id.toString())
            .enqueue(object : Callback<ModelAppointments> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointments>, response: Response<ModelAppointments>
                ) {
                    Log.e("Ala", "${response.body()!!}")
                    Log.e("Ala", "${response.body()!!.status}")
                    if (response.body()!!.result.isEmpty()){
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Appointment Found")
                        progressDialog!!.dismiss()

                    }else{
                        binding.rvCancled.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterCancelled(requireContext(), response.body()!!)
                            progressDialog!!.dismiss()

                        }
                    }


                }

                override fun onFailure(call: Call<ModelAppointments>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }
*/

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

        val retrofitData = retrofitBuilder.myAppointmentCancelled(sessionManager.id.toString())
        retrofitData.enqueue(object : Callback<ModelCancelled> {
            override fun onResponse(
                call: Call<ModelCancelled>,
                response: Response<ModelCancelled>
            ) {


                if (response.body()!!.result.cancelled.isEmpty()) {
                    binding.tvNoDataFound.visibility = View.VISIBLE
                  //   myToast(requireActivity(),"No Appointment Found")
                    progressDialog!!.dismiss()

                } else {
                    binding.rvCancled.apply {
                        binding.tvNoDataFound.visibility = View.GONE
                        adapter = AdapterCancelled(requireContext(), response.body()!!)
                        progressDialog!!.dismiss()

                    }
//                myToast(requireActivity(),response.body()!!.message)
//                progressDialog!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ModelCancelled>, t: Throwable) {
                t.message?.let { myToast(requireActivity(), it)
                    progressDialog!!.dismiss()

                }
            }
        })
    }
*/

}