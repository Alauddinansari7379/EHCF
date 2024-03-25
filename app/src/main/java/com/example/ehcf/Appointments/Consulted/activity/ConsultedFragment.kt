package com.example.ehcf.Appointments.Consulted.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.ehcf.Appointments.Cancelled.adapter.AdapterCancelled
import com.example.ehcf.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf.Appointments.Consulted.model.ModelConsultedResponse
import com.example.ehcf.Appointments.UpComing.adapter.AdapterAppointments
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingNew
import com.example.ehcf.Appointments.UpComing.model.ResultXXX
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentConsultedBinding
import com.example.ehcf.retrofit.ApiInterface
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class ConsultedFragment : Fragment() {
    private lateinit var binding: FragmentConsultedBinding
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var mainData = ArrayList<ResultXXX>()

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

        adapter="1"


        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationCompletedNew()
        }
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationCompleted()
        }
        binding.edtSearch.addTextChangedListener {str ->
            setRecyclerViewAdapter(mainData.filter {
                it.doctor_name!!.contains(str.toString(),ignoreCase = true)
            } as ArrayList<ResultXXX>)
        }

//        binding.imgSearch.setOnClickListener {
//            if (binding.edtSearch.text.toString().isEmpty()) {
//                binding.edtSearch.error = "Enter Doctor Name"
//                binding.edtSearch.requestFocus()
//            } else {
//                val search = binding.edtSearch.text.toString()
//                //apiCallSearchAppointments(search)
//            }
//        }

    }
/*    private fun apiCallSearchAppointments(doctorName: String) {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.searchAppointmentsCompleted(sessionManager.id.toString(),doctorName,"completed")
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    if (response.code() == 500) {
                        myToast(requireActivity(), "Server Error")
                        binding.shimmer.visibility = View.GONE
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.status == 0) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        binding.edtSearch.text.clear()
                        myToast(requireActivity(), "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvCancled.adapter =
                            activity?.let { AdapterConsulted(it, response.body()!!) }
                        binding.rvCancled.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        binding.edtSearch.text.clear()
                        myToast(requireActivity(), "No Appointment Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvCancled.adapter =
                            activity?.let { AdapterConsulted(it, response.body()!!) }
                        binding.rvCancled.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.GONE
                        shimmerFrameLayout?.startShimmer()
                        binding.rvCancled.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
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
                }

                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }*/


    private fun apiCallGetConsultationCompleted() {
        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "completed")
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    try {
                        if (response.code() == 200){
                            mainData = response.body()!!.result!!

                        }

                         if(response.code() == 500) {
                            myToast(requireActivity(), "Server Error")

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            // myToast(requireActivity(),"No Appointment Found")

                        } else {
                            setRecyclerViewAdapter(mainData)
                             binding.shimmer.visibility = View.GONE

                         }

                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }

                }

            })
    }
    private fun apiCallGetConsultationCompletedNew() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "completed")
            .enqueue(object : Callback<ModelAppointmentBySlag> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentBySlag>, response: Response<ModelAppointmentBySlag>
                ) {
                    try {
                        if (response.code() == 200){
                            mainData = response.body()!!.result!!
                            progressDialog!!.dismiss()
                        }
                         if(response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            // myToast(requireActivity(),"No Appointment Found")
                            progressDialog!!.dismiss()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            progressDialog!!.dismiss()

                        }

                    }catch (e:Exception){
                        e.printStackTrace()
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelAppointmentBySlag>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    progressDialog!!.dismiss()

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

    companion object{
         var adapter=""
    }

    /*
        private fun apiCall(){

            progressDialog = ProgressDialog(requireContext())
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()
            val id="20"

            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                //.baseUrl("https://jsonplaceholder.typicode.com/")
                .baseUrl("https://ehcf.thedemostore.in/api/customer/")
                .build()
                .create(ApiInterface::class.java)

            val retrofitData = retrofitBuilder.myAppointmentConsulted(sessionManager.id.toString())
            retrofitData.enqueue(object : Callback<ModelConsultedResponse> {
                override fun onResponse(
                    call: Call<ModelConsultedResponse>,
                    response: Response<ModelConsultedResponse>
                )
                {
                    if (response.body()!!.result.completed.isEmpty()){
                        binding.tvNoDataFound.visibility = View.VISIBLE
                       // myToast(requireActivity(),"No Appointment Found")
                        progressDialog!!.dismiss()

                    }else{
                        binding.rvCancled.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterConsulted(requireContext(), response.body()!!)
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelConsultedResponse>, t: Throwable) {
                    myToast(requireActivity(),"Something went wrong")
                        progressDialog!!.dismiss()


                }
            })
        }
    */
    override fun onStart() {
        super.onStart()
        if (isOnline(requireContext())) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
//        CheckInternet().check { connected ->
//            if (connected) {
//             //    myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
    }

}