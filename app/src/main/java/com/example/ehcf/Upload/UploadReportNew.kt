package com.example.ehcf.Upload

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import com.example.ehcf.Fragment.AddReport
import com.example.ehcf.Fragment.test.UploadRequestBody
import com.example.ehcf.Fragment.test.UploadResponse
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.Upload.adapter.AdapterUploadReport
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.Upload.model.ModelUploadReport
import com.example.ehcf.databinding.ActivityUploadReportNewBinding
import com.example.ehcf.report.adapter.AdapterAppReport
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UploadReportNew : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: ActivityUploadReportNewBinding
    private val context: Context = this@UploadReportNew
    private var selectedImageUri: Uri? = null
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadReportNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@UploadReportNew)

        apiCallGetAllReport()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btbAddMoreReport.setOnClickListener {
            opeinImageChooser()
        }
        sessionManager = SessionManager(this@UploadReportNew)

        binding.buttonUpload.setOnClickListener {

            uploadImage()


        }
    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun opeinImageChooser() {

//        Intent(Intent.ACTION_PICK).also {
//            it.type = "image/*"
//            (MediaStore.ACTION_IMAGE_CAPTURE)
//            val mimeTypes = arrayOf("image/jpeg", "image/png")
//            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//            startActivityForResult(it, REQUEST_CODE_IMAGE)
            val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
            pdfIntent.type = "application/pdf"
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(pdfIntent, AddReport.REQUEST_CODE_IMAGE)


   //     }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.imageView.visibility = View.VISIBLE
                    binding.buttonUpload.visibility = View.VISIBLE
                    // binding.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun apiCallGetAllReport() {
        progressDialog = ProgressDialog(this@UploadReportNew)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getReport(sessionManager.id.toString())
            .enqueue(object : Callback<ModelGetAllReport> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetAllReport>, response: Response<ModelGetAllReport>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.btbAddMoreReport.text="Add Report"
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.recyclerView.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterUploadReport(response.body()!!, this@UploadReportNew)
                        }
                        progressDialog!!.dismiss()

                    }


                }

                override fun onFailure(call: Call<ModelGetAllReport>, t: Throwable) {
                    myToast(this@UploadReportNew, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }


    private fun uploadImage() {

        if (selectedImageUri == null) {
            // binding.layoutRoot.snackbar("Select an Image First")
            return
        }


        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return


        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        progressDialog = ProgressDialog(this@UploadReportNew)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")

        progressDialog!!.isIndeterminate = false

        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)

        ApiClient.apiService.uploadReport(
            sessionManager.id
                .toString(),
            MultipartBody.Part.createFormData("report", file.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
        ).enqueue(object :
            Callback<ModelUploadReport> {
            override fun onResponse(
                call: Call<ModelUploadReport>,
                response: Response<ModelUploadReport>
            ) {
                response.body()?.let {
                    myToast(this@UploadReportNew, response.body()!!.message)
                    refresh()
//                    binding.layoutRoot.snackbar(it.message)
//                    binding.progressBar.progress = 100
                    progressDialog!!.dismiss()

                }
            }


            override fun onFailure(call: Call<ModelUploadReport>, t: Throwable) {
//                binding.layoutRoot.snackbar(t.message!!)
//                binding.progressBar.progress = 0
                progressDialog!!.dismiss()

            }


        })


    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    override fun onProgressUpdate(percentage: Int) {
        //  binding.progressBar.progress = percentage
    }


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