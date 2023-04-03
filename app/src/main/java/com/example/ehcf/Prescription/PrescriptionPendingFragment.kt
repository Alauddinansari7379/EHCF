package com.example.ehcf.Prescription

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ehcf.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescribed
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionPending
import com.example.ehcf.Prescription.model.My_model
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentPrescriptionPendingBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrescriptionPendingFragment : Fragment() {
    private lateinit var binding: FragmentPrescriptionPendingBinding
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        inflater.inflate(R.layout.fragment_prescription_pending, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionPendingBinding.bind(view)
        shimmerFrameLayout = view.findViewById(R.id.shimmerPrePending)
        shimmerFrameLayout!!.startShimmer();

        sessionManager = SessionManager(requireContext())
      //  apiCallGetPrePending()
        apiCallGetConsultationCompleted()
        //  apiCall()

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

                    if (response.code() == 500) {
                        myToast(requireActivity(), "Server Error")
                    }
                    else   if (response.body()!!.result.isEmpty()) {
                        Log.d("okhghghg", "" + response.body());
                        binding.shimmerPrePending.visibility = View.GONE
                        myToast(requireActivity(), "No Prescription Found")
                        progressDialog!!.dismiss()

                    } else  {
                        binding.recyclerView.apply {
                            binding.recyclerView.visibility = View.VISIBLE
                            shimmerFrameLayout?.startShimmer()
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.shimmerPrePending.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter =
                                AdapterPrescriptionPending(requireContext(), response.body()!!)
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

//    private fun apiCallGetPrePending() {
//        progressDialog = ProgressDialog(requireContext())
//        progressDialog!!.setMessage("Loading..")
//        progressDialog!!.setTitle("Please Wait")
//        progressDialog!!.isIndeterminate = false
//        progressDialog!!.setCancelable(true)
//        progressDialog!!.show()
//
//        ApiClient.apiService.pendingPreList(sessionManager.id.toString())
//            .enqueue(object : Callback<My_model> {
//
//                @SuppressLint("NotifyDataSetChanged")
//
//                override fun onResponse(
//                    //   Call<List<model>>
//                    call: Call<My_model>, response: Response<My_model>
//                ) {
//
//
//
//                    if (response.body()!!.result.isEmpty()) {
//                        Log.d("okhghghg", "" + response.body());
//                        binding.shimmerPrePending.visibility = View.GONE
//                        myToast(requireActivity(), "No Prescription Found")
//                        progressDialog!!.dismiss()
//
//                    } else  {
//                        binding.recyclerView.apply {
//                            binding.recyclerView.visibility = View.VISIBLE
//                            shimmerFrameLayout?.startShimmer()
//                            binding.recyclerView.visibility = View.VISIBLE
//                            binding.shimmerPrePending.visibility = View.GONE
//                            binding.tvNoDataFound.visibility = View.GONE
//                            adapter =
//                                AdapterPrescriptionPending(requireContext(), response.body()!!)
//                            progressDialog!!.dismiss()
//                        }
//                    }
//                    if (response.code() == 500) {
//                        binding.shimmerPrePending.visibility = View.GONE
//                        Toast.makeText(requireContext(), "Server Error", Toast.LENGTH_SHORT).show()
//                    } else {
//
//
////
//                    }
//
//                }
//
//                override fun onFailure(call: Call<My_model>, t: Throwable) {
//                    // myToast(requireActivity(), "Something went wrong")
//                    Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()
//                    // myToast(requireActivity(), "${t.message}")
//                    progressDialog!!.dismiss()
//
//                }
//
//            })
//    }

}