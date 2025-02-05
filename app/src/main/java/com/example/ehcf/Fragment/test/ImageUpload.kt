package com.example.ehcf.Fragment.test

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.databinding.ActivityImageUploadBinding
import com.example.ehcf.report.adapter.AdapterAppReport
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


class ImageUpload : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding:ActivityImageUploadBinding
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener {
            opeinImageChooser()
        }

        binding.buttonUpload.setOnClickListener {
            uploadImage1()
        }

    }
    fun opeinImageChooser() {

//        ImagePicker.with(this)
//            .crop()	    			//Crop image(Optional), Check Customization for more option
//            .compress(1024)			//Final image size will be less than 1 MB(Optional)
//            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//            .start()
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)

        }
    }

    fun uploadImage(id: String) {
        uploadImage1()

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }
    fun selectImage() {
        opeinImageChooser()

    }
    fun uploadImage1() {

        if (selectedImageUri == null) {
//            binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            selectedImageUri!!, "r", null
        ) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

        val file = File(cacheDir,contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

      //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file,"image",this)

//        ApiClient.apiService.uploadImage("22",MultipartBody.Part.createFormData("image", file.name,body),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"json")).enqueue(object : Callback<UploadResponse>{
//            override fun onResponse(
//                call: Call<UploadResponse>,
//                response: Response<UploadResponse>
//            ) {
//                response.body()?.let {
//                    myToast(this@ImageUpload,response.message())
//                   binding.layoutRoot.snackbar(it.message)
////                    binding.progressBar.progress = 100
//                }
//            }
//
//            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
////                binding.layoutRoot.snackbar(t.message!!)
////                binding.progressBar.progress = 0
//            }
//
//        })


    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }



    fun upload(id: String) {

            val parcelFileDescriptor = contentResolver.openFileDescriptor(
                selectedImageUri!!, "r", null
            ) ?: return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

            val file = File(cacheDir,contentResolver.getFileName(selectedImageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

           // binding.progressBar.progress = 0
            val body = UploadRequestBody(file,"image",this)
//
//            ApiClient.apiService.uploadImage("22",MultipartBody.Part.createFormData("image", file.name, body),
//                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"json"))
//                .enqueue(object : Callback<UploadResponse>{
//                override fun onResponse(
//                    call: Call<UploadResponse>,
//                    response: Response<UploadResponse>
//                ) {
//                    response.body()?.let {
////                        binding.layoutRoot.snackbar(it.message)
////                        binding.progressBar.progress = 100
//                    }
//                }
//
//                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
////                    binding.layoutRoot.snackbar(t.message!!)
////                    binding.progressBar.progress = 0
//                }
//
//            })


        }

}

private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
    var name = ""
    val returnCursor = this.query(selectedImageUri,null,null,null,null)
    if (returnCursor!=null){
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



