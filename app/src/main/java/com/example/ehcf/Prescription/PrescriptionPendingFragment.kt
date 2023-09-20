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
import androidx.core.widget.addTextChangedListener
import com.example.ehcf.Appointments.Cancelled.adapter.AdapterCancelled
import com.example.ehcf.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf.Appointments.UpComing.model.ResultXXX
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
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var doctorName = ""
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
        binding.edtSearch.addTextChangedListener {str ->
            setRecyclerViewAdapter(mainData.filter { it.doctor_name!!.contains(str.toString(),ignoreCase = true)} as ArrayList<ResultPrePending>)
        }
//        binding.imgSearch.setOnClickListener {
//            if (binding.edtSearch.text.toString().isEmpty()) {
//                binding.edtSearch.error = "Enter Doctor Name"
//                binding.edtSearch.requestFocus()
//            } else {
//                doctorName = binding.edtSearch.text.toString()
//                apiCallSearchPrescription(doctorName)
//            }
//
//        }

    }

/*
    private fun apiCallSearchPrescription(doctorName: String) {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.searchPrescription(sessionManager.id.toString(), doctorName)
            .enqueue(object : Callback<ModelPreList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPreList>, response: Response<ModelPreList>
                ) {
                    try {


                        if (response.code() == 500) {
                            activity?.let { myToast(it, "Server Error") }
                            binding.shimmerPrePending.visibility = View.GONE
                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerPrePending.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            activity?.let { myToast(it, "${response.body()!!.message}") }
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.recyclerView.adapter =
                                activity?.let { AdapterPrescriptionPending(it, response.body()!!) }
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerPrePending.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            //activity?.let { myToast(it, "No Prescription Found") }
                            progressDialog!!.dismiss()

                        } else {
                            binding.recyclerView.adapter =
                                activity?.let { AdapterPrescriptionPending(it, response.body()!!) }
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.shimmerPrePending.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            progressDialog!!.dismiss()
//                        binding.rvManageSlot.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            shimmerFrameLayout?.startShimmer()
//                            binding.rvManageSlot.visibility = View.VISIBLE
//                            binding.shimmerMySlot.visibility = View.GONE
//                            // myToast(this@ShuduleTiming, response.body()!!.message)
//                            adapter = AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
//                            progressDialog!!.dismiss()
//
//                        }
                        }
                    } catch (e: Exception) {
                        activity?.let { myToast(it, "Something went wrong") }
                        binding.shimmerPrePending.visibility = View.GONE
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelPreList>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    binding.shimmerPrePending.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }
*/

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
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.pendingPreList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPreList> {

                @SuppressLint("NotifyDataSetChanged")

                override fun onResponse(
                    //   Call<List<model>>
                    call: Call<ModelPreList>, response: Response<ModelPreList>
                ) {
                    try {
                        if (response.code() == 200){
                            mainData = response.body()!!.result!!

                        }
                        if (response.code() == 500) {
                            binding.shimmerPrePending.visibility = View.GONE
                            activity?.let { myToast(it, "Server Error")
                                binding.tvNotFound.visibility = View.VISIBLE

                            }
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            Log.d("okhghghg", "" + response.body());
                            binding.shimmerPrePending.visibility = View.GONE
                            binding.tvNotFound.visibility = View.VISIBLE
                            activity?.let { myToast(it, "No Prescription Found") }
                            progressDialog!!.dismiss()

                        } else {
                            setRecyclerViewAdapter(mainData)
                                progressDialog!!.dismiss()
                            }

                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ModelPreList>, t: Throwable) {
                    // myToast(requireActivity(), "Something went wrong")
                    Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                    // myToast(requireActivity(), "${t.message}")
                    progressDialog!!.dismiss()

                }

            })
    }

}