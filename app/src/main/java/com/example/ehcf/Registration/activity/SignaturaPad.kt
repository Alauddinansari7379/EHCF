package com.example.ehcf.Registration.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ehcf.Fragment.test.UploadRequestBody
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.PrivacyPolicies
import com.example.ehcf.Registration.ModelResponse.RegistationResponse
import com.example.ehcf.databinding.ActivitySignaturaPadBinding
import com.example.ehcf.login.activity.SignIn
import com.example.ehcf.retrofit.ApiClient
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.*

class SignaturaPad : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: ActivitySignaturaPadBinding
    private var selectedImageUri: Uri? = null
    private var photo: File? = null
    val context = this@SignaturaPad
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignaturaPadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //set permission
        verifyStoragePermissions(this);

        binding.btnRegister.isEnabled = false

        Log.e("Registration.coustmerNameCom ", Registration.coustmerNameCom)
        Log.e("Registration.phoneNumberCom ", Registration.phoneNumberCom)
        Log.e("Registration.phoneNumberWithCodeCom ", Registration.phoneNumberWithCodeCom)
        Log.e("Registration.emailCom ", Registration.emailCom)
        Log.e("Registration.passwordCom ", Registration.passwordCom)
        Log.e("Registration.bloodGroupCom ", Registration.bloodGroupCom)
        Log.e("Registration.genderValueCom ", Registration.genderValueCom)
        Log.e("Registration.fcmTokenCom ", Registration.fcmTokenCom)
        Log.e("Registration.dateOfBirth ", Registration.dateOfBirth)



        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        //draw signature
        binding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Toast.makeText(this@MainActivity, "Mulai menulis...", Toast.LENGTH_SHORT).show()
            }

            override fun onSigned() {
                binding.btnRegister.isEnabled = true
                binding.btnClear.isEnabled = true
            }

            override fun onClear() {
                binding.btnClear.isEnabled = false
                binding.btnRegister.isEnabled = false
            }
        })

        //clear signature
        binding.btnClear.setOnClickListener {
            binding.signaturePad.clear()
        }

        binding.tvPrivacyTerms.setOnClickListener {
            startActivity(Intent(this@SignaturaPad, PrivacyPolicies::class.java))
        }

        //save signature
        binding.btnRegister.setOnClickListener {
            if (!binding.checkBox1.isChecked) {
                myToast(this@SignaturaPad, "Please Accept Term & Condition")
            } else if (!binding.checkBox2.isChecked) {
                myToast(this@SignaturaPad, "Please Accept Term & Condition")

            } else {
                val signatureBitmap = binding.signaturePad.signatureBitmap
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    apiCallRegister()
                } else {
                    apiCallRegister()
                }

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@SignaturaPad, "Permission required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getAlbumStorageDir(albumName: String?): File {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            albumName
        )
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    //Image Signature
    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    private fun addJpgSignatureToGallery(signature: Bitmap): Boolean {
        var result = false
        try {
            photo = File(
                getAlbumStorageDir("SignaturePad"), String.format(
                    "Signature_%d.jpg",
                    System.currentTimeMillis()
                )
            )
            saveBitmapToJPG(signature, photo)
            scanMediaFile(photo!!)
            //selectedImageUri=photo
            Log.e("photo", photo.toString())
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        this@SignaturaPad.sendBroadcast(mediaScanIntent)
    }

    //SVG Signature
    private fun addSvgSignatureToGallery(signatureSvg: String?): Boolean {
        var result = false
        try {
            val svgFile = File(
                getAlbumStorageDir("SignaturePad"), String.format(
                    "Signature_%d.svg",
                    System.currentTimeMillis()
                )
            )
            val stream: OutputStream = FileOutputStream(svgFile)
            val writer = OutputStreamWriter(stream)
            writer.write(signatureSvg)
            writer.close()
            stream.flush()
            stream.close()
            scanMediaFile(svgFile)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun subscribed() {
        Firebase.messaging.subscribeToTopic("Patient")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(ContentValues.TAG, msg)
            }
    }

    private fun apiCallRegister() {
        if (photo == null) {
            myToast(this@SignaturaPad, "Select Report First")
            // binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        AppProgressBar.showLoaderDialog(context)
        val body = UploadRequestBody(photo!!, "image", this)
        ApiClient.apiService.register(
            Registration.coustmerNameCom,
            Registration.phoneNumberCom,
            Registration.phoneNumberWithCodeCom,
            Registration.emailCom,
            Registration.passwordCom,
            Registration.bloodGroupCom,
            Registration.genderValueCom.toInt(),
            Registration.fcmTokenCom,
            MultipartBody.Part.createFormData("signature", photo!!.name, body),
            "json".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            Registration.dateOfBirth,

            )
            .enqueue(object : Callback<RegistationResponse> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<RegistationResponse>, response: Response<RegistationResponse>
                ) {
                    AppProgressBar.hideLoaderDialog()
                    Log.e("Ala", "${response.body()!!.result}")
                    Log.e("Ala", response.body()!!.message)
                    Log.e("Ala", "${response.body()!!.status}")

                    if (response.body()!!.status == 1) {
                        myToast(this@SignaturaPad, response.body()!!.message)
                        subscribed()
                        AppProgressBar.hideLoaderDialog()

                        val intent = Intent(applicationContext, SignIn::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)

                    } else {
                        myToast(this@SignaturaPad, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<RegistationResponse>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallRegister()
                    } else {
                        myToast(this@SignaturaPad, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })
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


    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        fun verifyStoragePermissions(activity: SignaturaPad?) {
            val permission = ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {
    }

}
