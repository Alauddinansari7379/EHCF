package com.example.ehcf.Upload.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.report.activity.AddReport
import com.example.ehcf.Fragment.test.UploadRequestBody
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Upload.model.ModelUploadReport
import com.example.ehcf.databinding.ActivityUploadReportNewBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UploadReportNew : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: ActivityUploadReportNewBinding
    private val context: Context = this@UploadReportNew
    private var selectedImageUri: Uri? = null
    var progressDialog: ProgressDialog? = null
    var mydilaog: Dialog? = null
    var fileChosser = ""
    var date = ""

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadReportNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@UploadReportNew)
        AdapterFamilyListView.memberID=""
        // apiCallGetAllReport()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallFamilyListNew()

//        binding.layoutCamera.setOnClickListener {
//          //  openCameraChooser()
//            ImagePicker.with(this)
//                .cameraOnly()	//User can only capture image using Camera
//                .start()
//        }
        binding.layoutGallery.setOnClickListener {
            openImageChooser()
        }
        binding.layoutPDF.setOnClickListener {
            openPDFChooser()
        }

        binding.buttonUpload.setOnClickListener {
            if (binding.edtTitle.text.isEmpty()) {
                binding.edtTitle.error = "Enter Title"
                binding.edtTitle.requestFocus()
                return@setOnClickListener
            }
            uploadImage()


        }


        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar1 = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                DateFormat.getDateInstance().format(newDate.time)
                // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                 date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text = date
                Log.e(ContentValues.TAG, "onCreate: >>>>>>>>>>>>>>>>>>>>>>$date")
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )
        // Productiondate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        binding.tvDate.setOnClickListener {
            datePicker.show()

        }
    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun openPDFChooser() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, AddReport.REQUEST_CODE_IMAGE)


        //     }
    }

    private fun openImageChooser() {
        fileChosser = "1"
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)

        }
    }

    private fun openCameraChooser() {
        Intent(Intent.ACTION_PICK).also {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //  it.type = Intent(MediaStore.ACTION_IMAGE_CAPTURE).toString()
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            cameraIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            // startActivityForResult(it, REQUEST_CODE_IMAGE)
            Camera = 1
            startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    if (fileChosser == "1") {
                        binding.layoutGalleryGreen.visibility=View.VISIBLE
                        binding.layoutGalleryNew.visibility=View.GONE

                    } else {
                        binding.layoutPDFGreen.visibility=View.VISIBLE
                        binding.layoutPDFNew.visibility=View.GONE

                    }
                    // binding.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun getImageUri(inContext: Context?, inImage: Bitmap) {

        val tempFile = File.createTempFile("temprentpk", ".png")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        selectedImageUri = Uri.fromFile(tempFile)
        binding.imageView.visibility = View.VISIBLE
        binding.imageView.setImageURI(selectedImageUri)
        val newPath = selectedImageUri.toString().replace("temprentpk", "")
        Log.e("Image", selectedImageUri.toString())
        // selectedImageUri=te
    }


    private fun apiCallFamilyListNew() {

        progressDialog = ProgressDialog(this@UploadReportNew)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelFamilyList>,
                    response: Response<ModelFamilyList>
                ) {
                    // binding.rvSlotTiming.invalidate();
                    if (response.body()!!.status == 0) {
                        myToast(this@UploadReportNew, "${response.body()!!.message}")
                        progressDialog!!.dismiss()
                    } else if (response.code() == 500) {
                        myToast(this@UploadReportNew, "Server Error")
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvSlotTimingFamily.apply {
                            adapter =
                                AdapterFamilyListView(this@UploadReportNew, response.body()!!)
                            progressDialog!!.dismiss()
                        }
                    } else {
                        binding.rvSlotTimingFamily.apply {
                            //   adapter!!.notifyDataSetChanged();
                            //myToast(this@ShuduleTiming, response.body()!!.message)
                            adapter = AdapterFamilyListView(this@UploadReportNew, response.body()!!)
                            binding.rvSlotTimingFamily.layoutManager = GridLayoutManager(context, 3)
                            //    binding.layoutFamilyMemeber.visibility=View.VISIBLE

                            progressDialog!!.dismiss()
                        }

                    }
                }


                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    progressDialog!!.dismiss()
                    myToast(this@UploadReportNew, "Something went wrong")
                }


            })
    }


    private fun uploadImage() {

        if (selectedImageUri == null) {
            myToast(this@UploadReportNew, "Select Report First")
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

        ApiClient.apiService.uploadReport(sessionManager.id.toString(),
            binding.edtTitle.text.toString(),
            MultipartBody.Part.createFormData("report", file.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json"),
            AdapterFamilyListView.memberID,date
        ).enqueue(object :
            Callback<ModelUploadReport> {
            override fun onResponse(
                call: Call<ModelUploadReport>,
                response: Response<ModelUploadReport>
            ) {
                response.body()?.let {
                    if (response.code() == 500) {
                        myToast(this@UploadReportNew, "Server Error")
                        progressDialog!!.dismiss()

                    } else {
                        myToast(this@UploadReportNew, response.body()!!.message)
                        refresh()
//                    binding.layoutRoot.snackbar(it.message)
//                    binding.progressBar.progress = 100
                        progressDialog!!.dismiss()
                    }

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
        var Camera = 0
    }

    override fun onProgressUpdate(percentage: Int) {
        //  binding.progressBar.progress = percentage
    }

//    override fun deleteReport(id: String) {
//        SweetAlertDialog(this@UploadReportNew, SweetAlertDialog.WARNING_TYPE)
//            .setTitleText("Are you sure want to Delete Report?")
//            .setCancelText("No")
//            .setConfirmText("Yes")
//            .showCancelButton(true)
//            .setConfirmClickListener { sDialog ->
//                sDialog.cancel()
//                deleteReportNew(id)
//            }
//            .setCancelClickListener { sDialog ->
//                sDialog.cancel()
//            }
//            .show()
//
//    }

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
}