package com.example.ehcf.report

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentViewReport2Binding
import com.example.ehcf.databinding.FragmentViewReportBinding
import com.example.ehcf.report.adapter.AdapterAppReport
import com.example.ehcf.report.adapter.AdapterViewReport
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewReportFragment : Fragment() {
    private lateinit var binding: FragmentViewReport2Binding
    private lateinit var sessionManager: SessionManager
    var image_viewAddRe: ImageView? = null
    var progressDialog: ProgressDialog? = null
    var imageView: ImageView? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_report2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewReport2Binding.bind(view)
        sessionManager=SessionManager(requireContext())
        apiCallGetPrePending()
    }

    private fun apiCallGetPrePending() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.prescribedList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPrescribed> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrescribed>, response: Response<ModelPrescribed>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.recyclerView.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterViewReport(requireContext(), response.body()!!
                            )
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }


}