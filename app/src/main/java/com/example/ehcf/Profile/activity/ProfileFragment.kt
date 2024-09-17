package com.example.ehcf.Profile.activity

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Fragment.test.UploadRequestBody
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Profile.modelResponse.ModelProfilePic
import com.example.ehcf.Profile.modelResponse.ModelUpdateNameEmail
import com.example.ehcf.Profile.modelResponse.ResetPassResponse
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentProfile2Binding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ProfileFragment : Fragment(), UploadRequestBody.UploadCallback {
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentProfile2Binding
    var customerName = ""
    private var selectedImageUri: Uri? = null

    var dialog: Dialog? = null
    private var fullName = ""
    private var emailUpdate = ""

    var email = ""
    var count = 0
    var count1 = 0
    var count2 = 0
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

        if (sessionManager.profilePic!!.isNotEmpty()) {

            Picasso.get().load("${sessionManager.imageurl}${sessionManager.profilePic}")
                .placeholder(R.drawable.profile).error(R.drawable.profile).into(binding.userProfile)
            Log.e("pofile", "${sessionManager.imageurl}${sessionManager.profilePic}")
        }
        binding.btnUpdateName.setOnClickListener {
            updateNameEmailDialog()
        }
        binding.btnChangePassword.setOnClickListener {
            alretDilogChangePass()

        }

        binding.cameraBtn.setOnClickListener {
            opeinImageChooser()
        }
        binding.btnUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (selectedImageUri == null) {
            activity?.let { myToast(it, "Select an Image First") }
            // binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(
            selectedImageUri!!, "r", null

        ) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        var file = File(
            requireActivity().cacheDir, activity?.contentResolver!!.getFileName(selectedImageUri!!)
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        AppProgressBar.showLoaderDialog(requireContext())

        // binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)


        ApiClient.apiService.profilePicture(
            sessionManager.id.toString(),
            MultipartBody.Part.createFormData("image", file.name, body),
            "json".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        ).enqueue(object : Callback<ModelProfilePic> {
            override fun onResponse(
                call: Call<ModelProfilePic>, response: Response<ModelProfilePic>
            ) {
                response.body()?.let {
                    try {
                        if (response.code() == 500) {
                            myToast(activity!!, "Server error")

                        } else if (response.code() == 200) {
                            count = 0
                            //   binding.layoutRoot.snackbar("Successfully Uploaded")
                            AppProgressBar.hideLoaderDialog()
                            SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Successfully Uploaded")
                                .setConfirmText("Ok")
                                .showCancelButton(true)
                                .setConfirmClickListener { sDialog ->
                                    sDialog.cancel()
                                    // (activity as ReportMain).refresh()
                                    binding.layoutBtnUpload.visibility = View.GONE
                                    sessionManager.profilePic = response.body()!!.result
                                    Picasso.get()
                                        .load("${sessionManager.imageurl}${sessionManager.profilePic}")
                                        .into(binding.userProfile)

                                }
                                .setCancelClickListener { sDialog ->
                                    sDialog.cancel()
                                }
                                .show()

                            AppProgressBar.hideLoaderDialog()
                        } else {
                            myToast(activity!!, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()
                        }

                    } catch (e: Exception) {
                        myToast(activity!!, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }
            }

            override fun onFailure(call: Call<ModelProfilePic>, t: Throwable) {
                count++
                if (count <= 3) {
                    uploadImage()
                } else {
                    myToast(requireActivity(), t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            }

        })
    }

    private fun opeinImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)

//            val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
//            pdfIntent.type = "application/pdf"
//            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
//            startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    Log.e("data?.data", data?.data.toString())
                    binding.userProfile.setImageURI(selectedImageUri)
                    binding.layoutBtnUpload.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    override fun onProgressUpdate(percentage: Int) {
        //   binding.progressBar.progress = percentage
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()

        }

        return name
    }

    private fun View.snackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("OK") {
                snackbar.dismiss()
            }
        }.show()

    }

    private fun apiCallChangePass(confirmPassword: String) {

        AppProgressBar.showLoaderDialog(requireContext())
        ApiClient.apiService.resetPassword(sessionManager.id.toString(), confirmPassword)
            .enqueue(object : Callback<ResetPassResponse> {
                override fun onResponse(
                    call: Call<ResetPassResponse>,
                    response: Response<ResetPassResponse>
                ) {
                    try {
                        if (response.body()!!.status == 1) {
                            AppProgressBar.hideLoaderDialog()
                            count1 = 0
                            alretDilogChanged()
                            //myToast(requireActivity(), response.body()!!.message)
                        } else {
                            myToast(requireActivity(), response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ResetPassResponse>, t: Throwable) {
                    count1++
                    if (count1 <= 3) {
                        apiCallChangePass(confirmPassword)
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
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

        AppProgressBar.showLoaderDialog(requireContext())
        ApiClient.apiService.updateNameEmail(sessionManager.id.toString(), name, email)
            .enqueue(object :
                Callback<ModelUpdateNameEmail> {
                override fun onResponse(
                    call: Call<ModelUpdateNameEmail>,
                    response: Response<ModelUpdateNameEmail>
                ) {
                    if (response.code() == 500) {
                        myToast(requireActivity(), "Server Error")
                        AppProgressBar.hideLoaderDialog()
                    } else if (response.code() == 200) {
                        AppProgressBar.hideLoaderDialog()
                        alertDialogUpdated()
                        sessionManager.customerName = response.body()!!.result.customer_name
                        sessionManager.email = response.body()!!.result.email
                        count2 = 0
                        customerName = sessionManager.customerName.toString()
                        this@ProfileFragment.email = sessionManager.email.toString()

                        binding.tvFirstName.text = customerName
                        binding.tvLastName.text = customerName
                        binding.tvEmail.text = this@ProfileFragment.email

                    } else {
                        myToast(requireActivity(), "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelUpdateNameEmail>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        apiCallUpdateNameEmail(name, email)
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
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
        if (isOnline(requireContext())) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }

    }

}