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
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.Fragment.test.UploadRequestBody
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Upload.model.ModelUploadReport
import com.example.ehcf.databinding.ActivityUploadReportNewBinding
import com.example.ehcf.report.activity.AddReport
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.myrecyview.apiclient.ApiClient
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
import kotlin.collections.ArrayList

class UploadReportNew : AppCompatActivity(), UploadRequestBody.UploadCallback,
    AdapterFamilyListView.CheckBox {
    private lateinit var binding: ActivityUploadReportNewBinding
    private val context = this@UploadReportNew
    private var selectedImageUri: Uri? = null
    var mydilaog: Dialog? = null
    private var fileChosser = ""
    var date = ""
    var fis1: FileInputStream? = null
    var len1 = 0
    var countR4 = 0
    var countR5 = 0
    var countR6 = 0
    var pImgFile1: File? = null
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadReportNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@UploadReportNew)
        AdapterFamilyListView.memberID = ""
        // apiCallGetAllReport()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallFamilyListNew()



        binding.layoutCamera.setOnClickListener {
            //  myToast(this, "Work on Progress")
            fileChosser = "1"
            ImagePicker.with(this).cameraOnly()
//                                            .createIntent { intent ->
//                                startForProfileImageResult.launch(intent)
//                            }
                .start(REQUEST_CODE_IMAGE)
        }

        binding.layoutGallery.setOnClickListener {
            fileChosser = "2"
            openImageChooser()
        }

        binding.layoutPDF.setOnClickListener {
            fileChosser = "3"
            openPDFChooser()
        }

        binding.buttonUpload.setOnClickListener {
            if (binding.edtTitle.text.isEmpty()) {
                binding.edtTitle.error = "Enter Title"
                binding.edtTitle.requestFocus()
                return@setOnClickListener
            }
            if (fileChosser == "1") {
                uploadImageCamera()
            } else {
                uploadImage()

            }
            //  uploadImage()


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
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data!!
            //  binding.imageViewNew.setImageURI(fileUri)
            selectedImageUri = data?.data



            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    Log.e("filecgose", fileChosser)
                    when (fileChosser) {
                        "1" -> {
                            binding.layoutCameraGreen.visibility = View.VISIBLE
                            binding.layoutCameraNew.visibility = View.GONE
                        }

                        "2" -> {
                            binding.layoutGalleryGreen.visibility = View.VISIBLE
                            binding.layoutGalleryNew.visibility = View.GONE
                        }

                        "3" -> {
                            binding.layoutPDFGreen.visibility = View.VISIBLE
                            binding.layoutPDFNew.visibility = View.GONE

                        }
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
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelFamilyList>,
                    response: Response<ModelFamilyList>
                ) {

                    try {
                        // binding.rvSlotTiming.invalidate();
                        if (response.body()!!.status == 0) {
                            myToast(this@UploadReportNew, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 500) {
                            myToast(this@UploadReportNew, "Server Error")
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.rvSlotTimingFamily.apply {
                                adapter =
                                    AdapterFamilyListView(
                                        this@UploadReportNew,
                                        response.body()!!,
                                        this@UploadReportNew
                                    )
                                AppProgressBar.hideLoaderDialog()
                            }
                        } else {
                            binding.rvSlotTimingFamily.apply {
                                //   adapter!!.notifyDataSetChanged();
                                //myToast(this@ShuduleTiming, response.body()!!.message)

                                adapter = AdapterFamilyListView(
                                    this@UploadReportNew,
                                    response.body()!!,
                                    this@UploadReportNew
                                )
                                binding.rvSlotTimingFamily.layoutManager =
                                    GridLayoutManager(context, 3)
                                //    binding.layoutFamilyMemeber.visibility=View.VISIBLE

                                AppProgressBar.hideLoaderDialog()
                            }

                        }
                    } catch (e: Exception) {
                        AppProgressBar.hideLoaderDialog()
                        e.printStackTrace()
                        myToast(this@UploadReportNew, "Something went wrong")
                    }
                }


                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    countR4++
                    if (countR4 <= 3) {
                        apiCallFamilyListNew()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    // circleImageView.setImageURI(fileUri)
                    pImgFile1 = File(selectedImageUri!!.path)

                    runBlocking {
                        val pImg = async {
                            Compressor.compress(context, File(selectedImageUri!!.path))
                        }
                        pImgFile1 = pImg.await()

//
//                 //   val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return@runBlocking
//                    val inputStream = FileInputStream(pImgFile1)
//
//                    val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
//                    val outputStream = FileOutputStream(file)
//                    inputStream.copyTo(outputStream)

                        if (pImgFile1 != null) {
                            fis1 = FileInputStream(pImgFile1)
                            len1 = pImgFile1!!.length().toInt()
                        }
                        GlobalScope.launch {
                            // insertData()
                        }
                    }

                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        }


    private fun uploadImage() {

        if (selectedImageUri == null) {
            myToast(this@UploadReportNew, "Select Report First")
            // binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return



        if (fileChosser == "1") {
            val inputStream = FileInputStream(pImgFile1)
            val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            //   val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return@runBlocking
        }

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        AppProgressBar.showLoaderDialog(context)

        //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)

        ApiClient.apiService.uploadReport(
            sessionManager.id.toString(),
            binding.edtTitle.text.toString(),
            MultipartBody.Part.createFormData("report", file.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json"),
            AdapterFamilyListView.memberID, date
        ).enqueue(object :
            Callback<ModelUploadReport> {
            override fun onResponse(
                call: Call<ModelUploadReport>,
                response: Response<ModelUploadReport>
            ) {
                try {
                    response.body()?.let {
                        if (response.code() == 500) {
                            myToast(this@UploadReportNew, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@UploadReportNew, response.body()!!.message)
                            refresh()

                            AppProgressBar.hideLoaderDialog()
                        }

                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    AppProgressBar.hideLoaderDialog()
                    myToast(this@UploadReportNew, "Something Went Wrong")


                }
            }


            override fun onFailure(call: Call<ModelUploadReport>, t: Throwable) {
                countR5++
                if (countR5 <= 3) {
                    uploadImage()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
            }


        })


    }

    private fun uploadImageCamera() {

        if (selectedImageUri == null) {
            myToast(this@UploadReportNew, "Select Report First")
            // binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        val file: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .absolutePath + "/myAppImages/"
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        val file1: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/myAppImages/" + selectedImageUri!!.lastPathSegment
        )

        // val fos = FileOutputStream(file1)

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        // val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file1)
        inputStream.copyTo(outputStream)

        AppProgressBar.showLoaderDialog(context)

        //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file1, "image", this)

        ApiClient.apiService.uploadReport(
            sessionManager.id.toString(),
            binding.edtTitle.text.toString(),
            MultipartBody.Part.createFormData("report", file1.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json"),
            AdapterFamilyListView.memberID, date
        ).enqueue(object :
            Callback<ModelUploadReport> {
            override fun onResponse(
                call: Call<ModelUploadReport>,
                response: Response<ModelUploadReport>
            ) {
                try {
                    response.body()?.let {
                        if (response.code() == 500) {
                            myToast(this@UploadReportNew, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@UploadReportNew, response.body()!!.message)
                            refresh()

                            AppProgressBar.hideLoaderDialog()
                        }

                    }
                } catch (e: java.lang.Exception) {
                    myToast(this@UploadReportNew, "Something went wrong")
                    e.printStackTrace()
                    AppProgressBar.hideLoaderDialog()

                }
            }


            override fun onFailure(call: Call<ModelUploadReport>, t: Throwable) {
                countR6++
                if (countR6 <= 3) {
                    uploadImageCamera()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

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

    override fun checkBox(id: Int) {
        if (id == 1) {
            binding.checkSelf.isChecked = true
            //Do Whatever you want in isChecked
        } else {
            binding.checkSelf.isChecked = false
        }
    }
}