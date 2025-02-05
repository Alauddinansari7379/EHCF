package com.example.ehcf.report

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentViewReport2Binding
import com.example.ehcf.databinding.FragmentViewReportBinding
import com.example.ehcf.report.activity.AddReport
import com.example.ehcf.report.activity.ReportMain
import com.example.ehcf.report.adapter.AdapterAppReport
import com.example.ehcf.report.adapter.AdapterViewReport
import com.example.ehcf.report.model.ModelGetTest
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewReportFragment : Fragment() {
    private lateinit var binding: FragmentViewReport2Binding
    private lateinit var sessionManager: SessionManager
    var image_viewAddRe: ImageView? = null
    var imageView: ImageView? = null
    var countR2 = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_report2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewReport2Binding.bind(view)
        sessionManager = SessionManager(requireContext())
        // apiCallGetPrePending()
        apiCallGetTest()
    }

    private fun apiCallGetTest() {
        AppProgressBar.showLoaderDialog(requireContext())

        ApiClient.apiService.getTest(ReportMain.prescriptionId)
            .enqueue(object : Callback<ModelGetTest> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetTest>, response: Response<ModelGetTest>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        binding.recyclerView.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = activity?.let {
                                AdapterViewReport(
                                    it, response.body()!!
                                )
                            }
                            AppProgressBar.hideLoaderDialog()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetTest>, t: Throwable) {
                    countR2++
                    if (countR2 <= 3) {
                        apiCallGetTest()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }


}