package com.example.ehcf.Prescription

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionPending
import com.example.ehcf.Prescription.model.ModelPrePending
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentPrescriptionPendingBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrescriptionPendingFragment : Fragment() {
    private lateinit var binding:FragmentPrescriptionPendingBinding
    var progressDialog:ProgressDialog?=null
    private lateinit var sessionManager:SessionManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        inflater.inflate(R.layout.fragment_prescription_pending, container, false)!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentPrescriptionPendingBinding.bind(view)

        sessionManager= SessionManager(requireContext())
        apiCallGetPrePending()

    }

    private fun apiCallGetPrePending() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.pendingPreList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPrePending> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrePending>, response: Response<ModelPrePending>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.layoutNotFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.recyclerView.apply {
                            binding.layoutNotFound.visibility = View.GONE
                            adapter = AdapterPrescriptionPending(requireContext(), response.body()!!)
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelPrePending>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

}