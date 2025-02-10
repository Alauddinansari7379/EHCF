package com.example.ehcf.Appointments.Cancelled.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ehcf.Appointments.Cancelled.adapter.AdapterCancelled
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Appointments.UpComing.model.ResultXXX
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentCancelledBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CancelledFragment : Fragment() {
    private lateinit var binding:FragmentCancelledBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
     var dialog: Dialog?= null
    private var tvTimeCounter: TextView?=null
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var mainData = ArrayList<ResultXXX>()
var count=0
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
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();
         apiCallGetConsultationRejected()


        binding.imgRefresh.setOnClickListener {
             apiCallGetConsultationRejected()
        }
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationRejected()
        }
    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultXXX>) {
        binding.rvCancled.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvCancled.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterCancelled(requireContext(), data)
        }
    }

    private fun apiCallGetConsultationRejected() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getConsultation(sessionManager.id.toString(),"rejected")
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    if (response.code() == 200) {
                        mainData = response.body()!!.result!!

                    }
                    if (response.code() == 500) {
                        myToast(requireActivity(), "Server Error")
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()

                    }
                   else if (response.body()!!.result.isEmpty()){
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        // myToast(requireActivity(),"No Appointment Found")
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        setRecyclerViewAdapter(mainData)
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()

                    }


                }

                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallGetConsultationRejected()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }


}