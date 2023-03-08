package com.example.ehcf.Profile.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Profile.modelResponse.ResetPassResponse
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentProfile2Binding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class ProfileFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentProfile2Binding
    var progressDialog: ProgressDialog? =null
    var doctorname =""
    var email =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfile2Binding.bind(view)
        sessionManager = SessionManager(requireContext())

        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)


        doctorname= sessionManager.customerName.toString()
        email= sessionManager.email.toString()

        binding.tvFirstName.text= doctorname
        binding.tvLastName.text= doctorname
        binding.tvEmail.text= email

        binding.btnChangePassword.setOnClickListener {
            binding.llChangePassword.visibility = View.VISIBLE

        }
        binding.llArrowDown.setOnClickListener {
            binding.edtNewPassword.text.clear()
            binding.edtConfirmPassword.text.clear()
            binding.llChangePassword.visibility = View.GONE

        }
        binding.btnSubmit.setOnClickListener {
            if (binding.edtNewPassword.text!!.isEmpty()) {
                binding.edtNewPassword.error = "Enter New Password"
                binding.edtNewPassword.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtConfirmPassword.text!!.isEmpty()) {
                binding.edtConfirmPassword.error = "Enter Confirm Password"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            val password = binding.edtNewPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

            if (password != confirmPassword) {
                binding.edtConfirmPassword.error = "Password Miss Match"
                binding.edtConfirmPassword.requestFocus()
            } else {

                progressDialog!!.show()
                ApiClient.apiService.resetPassword(sessionManager.id.toString(),confirmPassword).enqueue(object :Callback<ResetPassResponse>
                {
                    override fun onResponse(
                        call: Call<ResetPassResponse>,
                        response: Response<ResetPassResponse>
                    ) {
                        if (response.body()!!.status==1){
                            progressDialog!!.dismiss()
                            myToast(requireActivity(), response.body()!!.message)
                            binding.llChangePassword.visibility = View.GONE
                            binding.edtNewPassword.text.clear()
                            binding.edtConfirmPassword.text.clear()
                        }
                    }

                    override fun onFailure(call: Call<ResetPassResponse>, t: Throwable) {
                        progressDialog!!.dismiss()
                        myToast(requireActivity(),"Something went wrong")

                    }

                })


            }


        }
    }
    override fun onStart() {
        super.onStart()
        if (isOnline(requireContext())){
            //  myToast(requireActivity(), "Connected")
        }else{
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