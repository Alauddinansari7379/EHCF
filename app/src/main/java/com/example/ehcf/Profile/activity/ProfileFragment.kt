package com.example.ehcf.Profile.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Profile.modelResponse.ModelUpdateNameEmail
import com.example.ehcf.Profile.modelResponse.ResetPassResponse
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentProfile2Binding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class ProfileFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentProfile2Binding
    var progressDialog: ProgressDialog? =null
    var customerName =""
    var dialog: Dialog? = null
    private var fullName = ""
    private var emailUpdate = ""

    var confirmPassword =""
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



        customerName = sessionManager.customerName.toString()
        email = sessionManager.email.toString()

        binding.tvFirstName.text = customerName
        binding.tvLastName.text = customerName
        binding.tvEmail.text = email


        binding.btnUpdateName.setOnClickListener {
            updateNameEmailDialog()
        }
        binding.btnChangePassword.setOnClickListener {
            alretDilogChangePass()

        }

    }

    private fun apiCallChangePass(confirmPassword: String) {

        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
        ApiClient.apiService.resetPassword(sessionManager.id.toString(), confirmPassword).enqueue(object :Callback<ResetPassResponse>
        {
            override fun onResponse(
                call: Call<ResetPassResponse>,
                response: Response<ResetPassResponse>
            ) {
                if (response.body()!!.status == 1) {
                    progressDialog!!.dismiss()
                    alretDilogChanged()
                    //myToast(requireActivity(), response.body()!!.message)
                } else {
                    myToast(requireActivity(), response.body()!!.message)
                    progressDialog!!.dismiss()

                }
            }

            override fun onFailure(call: Call<ResetPassResponse>, t: Throwable) {
                progressDialog!!.dismiss()
                myToast(requireActivity(),"Something went wrong")

            }

        })


    }


    private fun alretDilogChanged() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your Password has been changed!")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }


    private fun alretDilogChangePass() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Change Password?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                // completeSlot(bookingId)
                changePassDialog()
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }
    private fun changePassDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_password_change, null)
        dialog = Dialog(requireContext())
        val btnChange = view!!.findViewById<Button>(R.id.btnChangeDialogPass)
        val newPass = view!!.findViewById<EditText>(R.id.edtNewPasswordDialogPass)
        val confirmPass = view!!.findViewById<EditText>(R.id.edtConfirmPasswordDialogPass)
        dialog = Dialog(requireContext())


        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)


        dialog?.show()
        btnChange.setOnClickListener {
            if (newPass.text!!.isEmpty()) {
                newPass.error = "Enter New Password"
                newPass.requestFocus()
                return@setOnClickListener
            }
            if (confirmPass.text!!.isEmpty()) {
                confirmPass.error = "Enter Confirm Password"
                confirmPass.requestFocus()
                return@setOnClickListener
            }
            val password = newPass.text.toString().trim()
            val confirmPassword = confirmPass.text.toString().trim()

            if (password != confirmPassword) {
                newPass.error = "Password Miss Match"
                confirmPass.requestFocus()
                return@setOnClickListener
            } else {
                apiCallChangePass(confirmPassword)
                dialog?.dismiss()
            }
        }
    }
    private fun apiCallUpdateNameEmail(name: String, email: String) {

        Log.e("NAme", name)
        Log.e("email", email)

        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
        ApiClient.apiService.updateNameEmail(sessionManager.id.toString(), name, email)
            .enqueue(object :
                Callback<ModelUpdateNameEmail> {
                override fun onResponse(
                    call: Call<ModelUpdateNameEmail>,
                    response: Response<ModelUpdateNameEmail>
                ) {
                    if (response.code() == 500) {
                        myToast(requireActivity(), "Server Error")
                        progressDialog!!.dismiss()

                    } else if (response.code() == 200) {
                        progressDialog!!.dismiss()
                        alertDialogUpdated()
                        sessionManager.customerName = response.body()!!.result.customer_name
                        sessionManager.email = response.body()!!.result.email

                        customerName = sessionManager.customerName.toString()
                        this@ProfileFragment.email = sessionManager.email.toString()

                        binding.tvFirstName.text = customerName
                        binding.tvLastName.text = customerName
                        binding.tvEmail.text = this@ProfileFragment.email

                    } else {
                        myToast(requireActivity(), "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelUpdateNameEmail>, t: Throwable) {
                    progressDialog!!.dismiss()
                    myToast(requireActivity(), "Something went wrong")

                }

            })


    }
    private fun updateNameEmailDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_update_name_and_email, null)
        dialog = Dialog(requireContext())
        val btnUpdate = view!!.findViewById<Button>(R.id.btnUpdateDialogUpdate)
        val edtFullName = view!!.findViewById<EditText>(R.id.edtFullNameDialogUpdate)
        val edtEmail = view!!.findViewById<EditText>(R.id.edtEmailDialogUpdate)
        dialog = Dialog(requireContext())


        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)


        dialog?.show()
        btnUpdate.setOnClickListener {


            fullName = edtFullName.text.toString()
            emailUpdate = edtEmail.text.toString()

            if (edtFullName.text.toString().isEmpty()) {
                fullName = sessionManager.customerName.toString()
            }
            if (edtEmail.text.toString().isEmpty()) {
                emailUpdate = sessionManager.email.toString()
            }
            apiCallUpdateNameEmail(fullName, emailUpdate)
            dialog?.dismiss()
        }
    }

    private fun alertDialogUpdated() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Successfully Updated!")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
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