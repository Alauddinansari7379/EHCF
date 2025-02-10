package com.example.ehcf.Appointments.Consulted.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.ehcf.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Appointments.UpComing.model.ResultXXX
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentConsultedBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver


class ConsultedFragment : Fragment() {
    private lateinit var binding: FragmentConsultedBinding
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var mainData = ArrayList<ResultXXX>()
    var count = 0
    var countR = 0
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
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        //  shimmerFrameLayout!!.startShimmer();

        apiCallGetConsultationCompleted()
        adapter = "1"
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationCompletedNew()
        }
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationCompleted()
        }
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.doctor_name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultXXX>)
        }


    }


    private fun apiCallGetConsultationCompleted() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "completed")
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!
                            count = 0
                        }

                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            binding.shimmer.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallGetConsultationCompleted()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    private fun apiCallGetConsultationCompletedNew() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "completed")
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!
                            AppProgressBar.hideLoaderDialog()
                            countR = 0
                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            // myToast(requireActivity(),"No Appointment Found")
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

                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
                    countR++
                    if (countR <= 3) {
                        apiCallGetConsultationCompletedNew()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultXXX>) {
        binding.rvCancled.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvCancled.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterConsulted(requireContext(), data)
        }
    }

    companion object {
        var adapter = ""
    }

    override fun onStart() {
        super.onStart()
        if (isOnline(requireContext())) {
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()

        }

    }

}