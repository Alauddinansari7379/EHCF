package com.example.ehcf.Prescription

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.ehcf.Dashboard.modelResponse.ModelSpecilList
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescribed
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.Prescription.model.ResultPrescribed
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentPrescribedBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PrescribedFragment : Fragment() {
    private lateinit var binding: FragmentPrescribedBinding
     private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var doctorName = ""
    var specilistName = ""
    var countR1 = 0
    var countR2 = 0
    var countR3 = 0
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

        apiCallGetPrePending()
        apiCallSpecialistSpinner()

        binding.imgRefresh.setOnClickListener {
            binding.edtSearch.text.clear()
            binding.layoutFilter.visibility = View.GONE
            apiCallGetPrePending()


        }

        binding.btnSearchFilter.setOnClickListener {
            apiCallFilterPrescription(binding.edtPatientName.text.toString().trim())
        }
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.doctor_name!!.contains(
                    str.toString(),
                    ignoreCase = true
                )
            } as ArrayList<ResultPrescribed>)
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

    }

    private fun apiCallSpecialistSpinner() {
       AppProgressBar.showLoaderDialog(requireContext())

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
                            AppProgressBar.hideLoaderDialog()

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

                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelSpecilList>, t: Throwable) {
                    countR1++
                    if (countR1 <= 3) {
                        apiCallSpecialistSpinner()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }


    private fun apiCallFilterPrescription(customer_name: String) {
       AppProgressBar.showLoaderDialog(requireContext())

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
                            AppProgressBar.hideLoaderDialog()

                        }
                        else  if (response.body()!!.result.isEmpty()) {

                            // myToast(requireActivity(),"No Data Found")
                            binding.shimmerPrescribed.visibility = View.GONE

                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!.result!!

                        }

                    } catch (e: Exception) {
                        activity?.let { myToast(it, "Something went wrong") }
                        AppProgressBar.hideLoaderDialog()
                        binding.shimmerPrescribed.visibility = View.GONE
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                     binding.shimmerPrescribed.visibility = View.GONE
                    countR2++
                    if (countR2 <= 3) {
                        apiCallFilterPrescription(customer_name)
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })
    }

    private fun apiCallGetPrePending() {

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

                        }
                       else  if (response.body()!!.result.isEmpty()) {

                            // myToast(requireActivity(),"No Data Found")
                            binding.shimmerPrescribed.visibility = View.GONE
                            binding.tvNotFound.visibility = View.VISIBLE

                        } else {
                            setRecyclerViewAdapter(mainData)
                         }

                    } catch (e: Exception) {
                        activity?.let { myToast(it, "Something went wrong") }
                         binding.shimmerPrescribed.visibility = View.GONE
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                    binding.shimmerPrescribed.visibility = View.GONE
                    countR3++
                    if (countR3 <= 3) {
                        apiCallGetPrePending()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
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

        }
    }


}
