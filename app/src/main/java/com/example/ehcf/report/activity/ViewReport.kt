package com.example.ehcf.report.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityViewReportBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.rajat.pdfviewer.PdfViewerActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class ViewReport : AppCompatActivity() {
    private lateinit var binding: ActivityViewReportBinding
    private val context: Context = this@ViewReport
    var setView = 1
    var imageView: ImageView? = null
    var left: ImageView? = null
    var etURL: EditText? = null
    var clearbtn: Button? = null
    var fetchbtn: Button? = null
    var mainHandler = Handler()
    var progressDialog: ProgressDialog? = null
    var reportData = ""
    private val PERMISSION_REQUEST_CODE = 1
lateinit var sessionManager: SessionManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
sessionManager= SessionManager(context)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnDashboard.setOnClickListener {
            onBackPressed()
        }

        binding.imgDow.setOnClickListener {
            if(reportData.contains("png") || reportData.contains("jpeg") || reportData.contains("jpg")){
                captureCardViewAsImage(binding.imageView2)
            }else{
                myToast(this@ViewReport,"Report not found")
            }
        }

        imageView = findViewById<View>(R.id.imageView2) as ImageView
//        etURL = findViewById<View>(R.id.etURL) as EditText

//        String url = etURL.getText().toString();

        reportData = intent.getStringExtra("report").toString()
        Log.e("reportData", reportData)

        if (reportData.contains("png") || reportData.contains("jpeg") || reportData.contains("jpg")) {
            val url="${sessionManager.imageurl}$reportData"
             FetchImage(url).start()
//            SweetAlertDialog(this@ViewReport, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("No Report Found")
//                .setConfirmText("Ok")
//                //.setCancelText("Ok")
//                .showCancelButton(true)
//                .setConfirmClickListener { sDialog ->
//                    sDialog.cancel()
//                    onBackPressed()
//                }
//                .setCancelClickListener { sDialog ->
//                    sDialog.cancel()
//                }
//                .show()
            // myToast(this,"No Report Found")
        } else {
            startActivity(
                // Use 'launchPdfFromPath' if you want to use assets file (enable "fromAssets" flag) / internal directory
                PdfViewerActivity.launchPdfFromUrl(           //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
                    context,
                    "${sessionManager.imageurl}$reportData",                                // PDF URL in String format
                    "Report",                        // PDF Name/Title in String format
                    "pdf directory to save",                  // If nothing specific, Put "" it will save to Downloads
                    enableDownload = true                    // This param is true by defualt.
                )
            )
//            val url= baseUrl+reportData
//            FetchImage(url).start()

        }


     }

    internal inner class FetchImage(var URL: String) : Thread() {
        private var bitmap: Bitmap? = null
        override fun run() {
            mainHandler.post {
                progressDialog = ProgressDialog(this@ViewReport)
                progressDialog!!.setMessage("Getting Report Image....")
                progressDialog!!.setCancelable(true)
                progressDialog!!.show()
            }
            var inputStream: InputStream? = null
            try {
                inputStream = URL(URL).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mainHandler.post {
                if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                imageView!!.setImageBitmap(bitmap)
            }
        }
    }

    private fun captureCardViewAsImage(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        val currentTimestamp = System.currentTimeMillis()


        val filename = "Report$currentTimestamp.png"
        val contentResolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Report")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val imageUri = contentResolver.insert(collection, contentValues)

        imageUri?.let {
            try {
                val outputStream: OutputStream? = contentResolver.openOutputStream(imageUri)
                outputStream?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    it.flush()
                    it.close()
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    contentResolver.update(imageUri, contentValues, null, null)
                    Toast.makeText(context, "Report Downloaded Successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//Dowanlod imge from URL
    @OptIn(DelicateCoroutinesApi::class)
    private fun downloadImage(context: Context, imageUrl: String, imageName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                val bitmap: Bitmap = BitmapFactory.decodeStream(input)

                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "YourDirectory"
                )

                if (!directory.exists()) {
                    directory.mkdirs()
                }

                val file = File(directory, imageName)

                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Image downloaded successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }

}