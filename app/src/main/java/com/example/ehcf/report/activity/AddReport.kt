package com.example.ehcf.report.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Fragment.test.*
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentViewReportBinding
import com.example.ehcf.report.adapter.AdapterAppReport
import com.example.ehcf.report.model.ModelGetTest
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class AddReport : Fragment(), UploadRequestBody.UploadCallback, AdapterAppReport.UploadImage {
    private lateinit var binding: FragmentViewReportBinding
    private var selectedImageUri: Uri? = null
    private lateinit var sessionManager: SessionManager
    var image_viewAddRe: ImageView? = null
    var progressDialog: ProgressDialog? = null
    var imageView: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewReportBinding.bind(view)

        sessionManager = SessionManager(requireContext())
//        binding.btnSelectImage.setOnClickListener {
//            startActivity(Intent(requireContext(), ImageUpload::class.java))
//        }

         imageView= view.findViewById<ImageView>(R.id.imageViewNew)
        apiCallGetTest()
      //  apiCallGetPrePending()

//        binding.cardSubmit.setOnClickListener {
//            opeinImageChooser()
//        }


    }

//    private fun apiCallGetPrePending() {
//        progressDialog = ProgressDialog(requireContext())
//        progressDialog!!.setMessage("Loading..")
//        progressDialog!!.setTitle("Please Wait")
//        progressDialog!!.isIndeterminate = false
//        progressDialog!!.setCancelable(true)
//        progressDialog!!.show()
//
//        ApiClient.apiService.prescribedList(sessionManager.id.toString())
//            .enqueue(object : Callback<ModelPrescribed> {
//                @SuppressLint("LogNotTimber")
//                override fun onResponse(
//                    call: Call<ModelPrescribed>, response: Response<ModelPrescribed>
//                ) {
//                    if (response.body()!!.result.isEmpty()) {
//                        binding.tvNoDataFound.visibility = View.VISIBLE
//                        // myToast(requireActivity(),"No Data Found")
//                        progressDialog!!.dismiss()
//
//                    } else {
//                        binding.recyclerView.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            adapter = activity?.let {
//                                AdapterAppReport(
//                                    it, response.body()!!, this@AddReport
//                                )
//                            }
//                            progressDialog!!.dismiss()
//
//                        }
//                    }
//
//                }
//
//                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
//                    myToast(requireActivity(), "Something went wrong")
//                    progressDialog!!.dismiss()
//
//                }
//
//            })
//    }
    private fun apiCallGetTest() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getTest(ReportMain.prescriptionId)
            .enqueue(object : Callback<ModelGetTest> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetTest>, response: Response<ModelGetTest>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.recyclerView.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = activity?.let {
                                AdapterAppReport(
                                    it, response.body()!!, this@AddReport
                                )
                            }
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetTest>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }


    private fun uploadImage(id: String) {
        if (selectedImageUri == null) {
            binding.layoutRoot.snackbar("Select an Image First")
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

        progressDialog = ProgressDialog(activity)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        // binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)


        ApiClient.apiService.UploadPatientTestReport(id, MultipartBody.Part.createFormData("image", file.name, body), "json".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        ).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>, response: Response<UploadResponse>
            ) {
                response.body()?.let {
                 //   binding.layoutRoot.snackbar("Successfully Uploaded")
                    progressDialog!!.dismiss()
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Successfully Uploaded")
                        .setConfirmText("Ok")
                        .showCancelButton(true)
                        .setConfirmClickListener { sDialog ->
                            sDialog.cancel()
                            (activity as ReportMain).refresh()

                        }
                        .setCancelClickListener { sDialog ->
                            sDialog.cancel()
                        }
                        .show()
                   // apiCallGetPrePending1()

                    //  binding.progressBar.progress = 100
                    progressDialog!!.dismiss()

                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                binding.layoutRoot.snackbar(t.message!!)
                // binding.progressBar.progress = 0
                progressDialog!!.dismiss()

            }

        })
    }

    private fun opeinImagePDF() {
//        Intent(Intent.ACTION_PICK).also {
//            it.type = "image/*"
//            (MediaStore.ACTION_IMAGE_CAPTURE)
//            val mimeTypes = arrayOf("image/jpeg", "image/png")
//            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//            startActivityForResult(it, REQUEST_CODE_IMAGE)

            val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
            pdfIntent.type = "application/pdf"
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

     //   }
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
                    binding.imageViewNew.visibility = View.VISIBLE
                 //   imageView?.setImageURI(selectedImageUri)
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

    override fun selectPDF() {
        opeinImagePDF()
    }
    override fun selectImage() {
        opeinImageChooser()
    }

    override fun upload(id: String) {
        uploadImage(id)
    }
}

