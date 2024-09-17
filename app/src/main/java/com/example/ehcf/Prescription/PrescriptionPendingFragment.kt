package com.example.ehcf.Prescription

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionPending
import com.example.ehcf.Prescription.model.ModelPreList
import com.example.ehcf.Prescription.model.ResultPrePending
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
     private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var doctorName = ""
    var countR1 = 0
     private var mainData = ArrayList<ResultPrePending>()

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
        apiCallGetPrePending1()

        binding.imgRefresh.setOnClickListener {
            binding.edtSearch.text.clear()
            apiCallGetPrePending1()
        }
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.doctor_name!!.contains(
                    str.toString(),
                    ignoreCase = true
                )
            } as ArrayList<ResultPrePending>)
        }


    }


    private fun setRecyclerViewAdapter(data: ArrayList<ResultPrePending>) {
        binding.recyclerView.apply {
            shimmerFrameLayout?.startShimmer()
            binding.recyclerView.visibility = View.VISIBLE
            binding.shimmerPrePending.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterPrescriptionPending(requireContext(), data)
        }
    }

    private fun apiCallGetPrePending1() {
        AppProgressBar.showLoaderDialog(requireContext())

        ApiClient.apiService.pendingPreList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPreList> {

                @SuppressLint("NotifyDataSetChanged")

                override fun onResponse(
                    //   Call<List<model>>
                    call: Call<ModelPreList>, response: Response<ModelPreList>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!

                        }
                        if (response.code() == 500) {
                            binding.shimmerPrePending.visibility = View.GONE
                            activity?.let {
                                myToast(it, "Server Error")
                                binding.tvNotFound.visibility = View.VISIBLE

                            }
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            Log.d("okhghghg", "" + response.body());
                            binding.shimmerPrePending.visibility = View.GONE
                            binding.tvNotFound.visibility = View.VISIBLE
                            activity?.let { myToast(it, "No Prescription Found") }
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }

                }

                override fun onFailure(call: Call<ModelPreList>, t: Throwable) {
                    countR1++
                    if (countR1 <= 3) {
                        apiCallGetPrePending1()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

}