package com.example.ehcf.Prescription

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.ehcf.Dashboard.modelResponse.ModelSpecilList
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescribed
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.Prescription.model.ResultPrePending
import com.example.ehcf.Prescription.model.ResultPrescribed
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
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var doctorName = ""
    var specilistName = ""
    private var specilList = ModelSpecilList();
    private var mainData = ArrayList<ResultPrescribed>()

    var con = true

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
        sessionManager = SessionManager(requireContext())
        mainData = ArrayList<ResultPrescribed>()
//        binding.btnAddReport.setOnClickListener {
//            startActivity(Intent(requireContext(),ReportMain::class.java))
//            sessionManager= SessionManager(requireContext())
//
//        }
        apiCallGetPrePending()
        apiCallSpecialistSpinner()
//        Handler().postDelayed({
//
//                              }, 1000)

        binding.imgRefresh.setOnClickListener {
            binding.edtSearch.text.clear()
            binding.layoutFilter.visibility = View.GONE
            apiCallGetPrePending()


        }

        binding.btnSearchFilter.setOnClickListener {
            apiCallFilterPrescription(binding.edtPatientName.text.toString().trim())
        }


        binding.imgFilter.setOnClickListener {
            if (con) {
                binding.layoutFilter.visibility = View.VISIBLE
                binding.imgFilterDowan.visibility = View.VISIBLE
                binding.imgFilter.visibility = View.GONE
                // binding.cardActiveInactive.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EC4C4C"))
                con = false
            } else {
                binding.imgFilterDowan.visibility = View.GONE
                binding.imgFilter.visibility = View.VISIBLE
                binding.layoutFilter.visibility = View.GONE
                // binding.cardActiveInactive.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
                con = true
            }

        }
        binding.imgFilterDowan.setOnClickListener {
            if (con) {
                binding.layoutFilter.visibility = View.VISIBLE
                binding.imgFilterDowan.visibility = View.VISIBLE
                binding.imgFilter.visibility = View.GONE
                // binding.cardActiveInactive.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EC4C4C"))
                con = false
            } else {
                binding.imgFilterDowan.visibility = View.GONE
                binding.imgFilter.visibility = View.VISIBLE
                binding.layoutFilter.visibility = View.GONE
                // binding.cardActiveInactive.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
                con = true
            }

        }

      /*  binding.imgSearch.setOnClickListener {
            binding.layoutFilter.visibility = View.GONE

            if (binding.edtSearch.text.toString().isEmpty()) {
                binding.edtSearch.error = "Enter Doctor Name"
                binding.edtSearch.requestFocus()
            } else {
                doctorName = binding.edtSearch.text.toString()
                apiCallSearchPrescription(doctorName)
            }

        }*/

    }

    private fun apiCallSpecialistSpinner() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()

        ApiClient.apiService.specialistCategoryTest()
            .enqueue(object : Callback<ModelSpecilList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelSpecilList>, response: Response<ModelSpecilList>
                ) {
                    try {
                        specilList = response.body()!!;
                        if (specilList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(specilList.result!!.size)

                            for (i in specilList.result!!.indices) {
                                items[i] = specilList.result!![i].categoryName
                            }

                            val adapter: ArrayAdapter<String?>? = activity?.let {
                                ArrayAdapter(it, android.R.layout.simple_list_item_1, items)
                            }
                            //  adapter!!.notifyDataSetChanged();
                            // adapter!!.insert("New Value",0);

                            binding.spinnerSpecialist.adapter = adapter
                            progressDialog!!.dismiss()

                            // binding.spinnerSpecialist.setSelection(sessionManager.specialist.toString().toInt()-1)


                            binding.spinnerSpecialist.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {
                                        val name = specilList.result!![i].categoryName
                                        specilistName = name.toString()
                                        //   Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        activity?.let { myToast(it, "Something went wrong") }

                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelSpecilList>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    progressDialog!!.dismiss()

                }

            })
    }

/*
    private fun apiCallSearchPrescription(doctorName: String) {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.searchPrescribed(sessionManager.id.toString(), doctorName)
            .enqueue(object : Callback<ModelPrescribed> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrescribed>, response: Response<ModelPrescribed>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            progressDialog!!.dismiss()
                            binding.shimmerPrescribed.visibility = View.GONE
                        } else if (response.body()!!.status == 0) {
                            binding.tvNotFound.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(requireActivity(), "${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.recyclerView.adapter =
                                AdapterPrescribed(requireContext(), response.body()!!)
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNotFound.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(requireActivity(), "No Prescription Found")
                            progressDialog!!.dismiss()

                        } else {
                            binding.recyclerView.adapter =
                                AdapterPrescribed(requireContext(), response.body()!!)
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNotFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
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
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmerPrescribed.visibility = View.GONE
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmerPrescribed.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }
*/

    private fun apiCallFilterPrescription(customer_name: String) {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.searchFilterDoctor(sessionManager.id.toString(),customer_name, specilistName)
            .enqueue(object : Callback<ModelPrescribed> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrescribed>, response: Response<ModelPrescribed>
                ) {
                    try {
                        if (response.code() == 200){
                            mainData = response.body()!!.result!!

                        }

                        if (response.code() == 500) {
                            binding.shimmerPrescribed.visibility = View.GONE
                            activity?.let { myToast(it, "Server Error") }
                            progressDialog!!.dismiss()

                        }
                        else  if (response.body()!!.result.isEmpty()) {

                            // myToast(requireActivity(),"No Data Found")
                            binding.shimmerPrescribed.visibility = View.GONE

                            progressDialog!!.dismiss()

                        } else {
                            mainData = response.body()!!.result!!

                        }

                    } catch (e: Exception) {
                        activity?.let { myToast(it, "Something went wrong") }
                        progressDialog!!.dismiss()
                        binding.shimmerPrescribed.visibility = View.GONE
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmerPrescribed.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
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
                    try {
                        if (response.code() == 200){
                            mainData = response.body()!!.result!!

                        }

                        if (response.code() == 500) {
                            binding.shimmerPrescribed.visibility = View.GONE
                            activity?.let { myToast(it, "Server Error") }
                            progressDialog!!.dismiss()

                        }
                       else  if (response.body()!!.result.isEmpty()) {

                            // myToast(requireActivity(),"No Data Found")
                            binding.shimmerPrescribed.visibility = View.GONE
                            binding.tvNotFound.visibility = View.VISIBLE
                            progressDialog!!.dismiss()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            progressDialog!!.dismiss()
                        }

                    } catch (e: Exception) {
                        activity?.let { myToast(it, "Something went wrong") }
                        progressDialog!!.dismiss()
                        binding.shimmerPrescribed.visibility = View.GONE
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()
                    binding.shimmerPrescribed.visibility = View.GONE


                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultPrescribed>) {
        binding.recyclerView.apply {
            shimmerFrameLayout?.startShimmer()
            binding.recyclerView.visibility = View.VISIBLE
            binding.shimmerPrescribed.visibility = View.GONE
            binding.tvNotFound.visibility = View.GONE
            adapter = AdapterPrescribed(requireContext(), data)

            progressDialog!!.dismiss()


        }
    }


}
