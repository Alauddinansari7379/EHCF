package com.example.ehcf.Prescription

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescribed
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentPrescribedBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PrescribedFragment : Fragment() {
    private lateinit var binding: FragmentPrescribedBinding
    var progressDialog:ProgressDialog?=null
    private lateinit var sessionManager:SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prescribed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescribedBinding.bind(view)
        shimmerFrameLayout = view.findViewById(R.id.shimmerPrescribed)
        shimmerFrameLayout!!.startShimmer();
        sessionManager= SessionManager(requireContext())
//        binding.btnAddReport.setOnClickListener {
//            startActivity(Intent(requireContext(),ReportMain::class.java))
//            sessionManager= SessionManager(requireContext())
//
//        }

        apiCallGetPrePending()
    }

    private fun apiCallGetPrePending() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
      //  progressDialog!!.show()

        ApiClient.apiService.prescribedList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPrescribed> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrescribed>, response: Response<ModelPrescribed>
                ) {
                    if (response.body()!!.result.isEmpty()) {

                        // myToast(requireActivity(),"No Data Found")
                        binding.shimmerPrescribed.visibility = View.GONE

                        progressDialog!!.dismiss()

                    } else {
                        binding.recyclerView.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterPrescribed(requireContext(), response.body()!!)
                            progressDialog!!.dismiss()


                        }
                    }

                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()
                    binding.shimmerPrescribed.visibility = View.GONE


                }

            })
    }


}
