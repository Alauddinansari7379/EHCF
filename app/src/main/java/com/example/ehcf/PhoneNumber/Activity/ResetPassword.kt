package com.example.ehcf.PhoneNumber.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Profile.modelResponse.ResetPassResponse
import com.example.ehcf.databinding.ActivityResetPasswordBinding
import com.example.ehcf.login.activity.SignIn
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassword : AppCompatActivity() {
    private lateinit var binding:ActivityResetPasswordBinding
    private lateinit var sessionManager: SessionManager
     var id = ""
     var count = 0
    val context=this@ResetPassword
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        Log.e("iddd",id)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnChangePass.setOnClickListener {
            if (binding.edtNewPass.text!!.isEmpty()) {
                binding.edtNewPass.error = "Enter New Password"
                binding.edtNewPass.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtConfirmPassword.text!!.isEmpty()) {
                binding.edtConfirmPassword.error = "Enter Confirm Password"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            val password = binding.edtNewPass.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

            if (password != confirmPassword) {
                binding.edtNewPass.error = "Password Miss Match"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            } else {
                apiCallChangePass(confirmPassword)
            }
        }

    }

    private fun apiCallChangePass(confirmPassword: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.resetPassword(id, confirmPassword)
            .enqueue(object :
                Callback<ResetPassResponse> {
                override fun onResponse(
                    call: Call<ResetPassResponse>,
                    response: Response<ResetPassResponse>
                ) {
                    if (response.body()!!.status == 1) {
                       AppProgressBar.hideLoaderDialog()
                        alretDilogChanged()
                        //myToast(requireActivity(), response.body()!!.message)
                    } else {
                        myToast(this@ResetPassword, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ResetPassResponse>, t: Throwable) {
                    if (count <= 3) {
                        apiCallChangePass(confirmPassword)
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })


    }

    private fun alretDilogChanged() {
        SweetAlertDialog(this@ResetPassword, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your Password has been changed!")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                val intent = Intent(applicationContext, SignIn::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                finish()
                startActivity(intent)

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }


}