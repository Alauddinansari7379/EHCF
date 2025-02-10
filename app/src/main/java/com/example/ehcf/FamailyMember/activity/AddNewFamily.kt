package com.example.ehcf.FamailyMember.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.ehcf.FamailyMember.Model.ModelFamilyMember
import com.example.ehcf.FamailyMember.Model.ModelFamilyNew
import com.example.ehcf.FamailyMember.activity.AddNewFamily.Data.Companion.refreshValue
import com.example.ehcf.Fragment.test.UploadRequestBody
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityAddNewFamilyBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddNewFamily : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private val context = this@AddNewFamily
    var mydilaog: Dialog? = null
    private var relationListNew = ModelFamilyNew()
    var firstName = ""
    private lateinit var sessionManager: SessionManager
    var lastName = ""
    private var selectedImageUri: Uri? = null
    var dob = ""
    var gender = ""
    var relationId = ""
    var email = ""
    var description = ""
    var id = ""
    var edit = ""
    var fileChosser = ""
    var countR3 = 0
    var countR4 = 0
    var countR5 = 0
    var countR6 = 0
    var dialog: Dialog? = null
    private lateinit var binding: ActivityAddNewFamilyBinding

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        id = intent.getStringExtra("id").toString()
        gender = intent.getStringExtra("gender").toString()
        edit = intent.getStringExtra("Edit").toString()
        if (edit == "1") {
            binding.importent.visibility = View.GONE
            binding.appCompatTextView2.text = "Edit Family Member"
            binding.btnSave.text = "Confirm"
            //val membername=intent.getStringExtra("first_name").toString()
            // val lasName= membername.split(" ")
            val name = intent.getStringExtra("first_name").toString()
            val firstName = name.substringBeforeLast(" ")
            val lastName = name.substringAfterLast(" ")
            binding.edtFirstName.setText(firstName)
            binding.edtLastName.setText(lastName)
            binding.edtDescription.setText(intent.getStringExtra("email").toString())
            binding.tvDate.text = intent.getStringExtra("dob").toString()
            relationId = intent.getStringExtra("relation").toString()
            Log.e("gender", gender)
        }

//        binding.tvMale.setTextColor(Color.parseColor("#A19398"))
//        binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
//        binding.tvOther.setTextColor(Color.parseColor("#9F367A"))


        when (gender) {
            "1" -> {
                binding.tvMale.setTextColor(Color.parseColor("#A19398"))
                binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
                binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
            }

            "2" -> {
                binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
                binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
                binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
            }

            "3" -> {
                binding.tvOther.setTextColor(Color.parseColor("#A19398"))
                binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
                binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
            }
        }



        sessionManager = SessionManager(this@AddNewFamily)
        apiCallGetRelation()

        binding.btnCancle.setOnClickListener {
            onBackPressed()
        }

        binding.userProfile.setOnClickListener {
            //  openImageChooser()
            popupUpload()
        }

        binding.btnSave.setOnClickListener {
            if (binding.edtFirstName.text.isEmpty()) {
                binding.edtFirstName.error = "Enter First Name"
                binding.edtFirstName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtLastName.text.isEmpty()) {
                binding.edtLastName.error = "Enter Last Name"
                binding.edtLastName.requestFocus()
                return@setOnClickListener

            }
            if (binding.tvDate.text.isEmpty()) {
                binding.tvDate.error = "Select Date"
                binding.tvDate.requestFocus()
                return@setOnClickListener
            }
            if (gender == "null") {
                myToast(this, "Select Gender")
                return@setOnClickListener
            }
//            if (binding.edtDescription.text.isEmpty()) {
//                binding.edtDescription.error = "Enter Description"
//                binding.edtDescription.requestFocus()
//                return@setOnClickListener
//            }

            firstName = binding.edtFirstName.text.toString().trim()
            lastName = binding.edtLastName.text.toString().trim()
            dob = binding.tvDate.text.toString().trim()
            description = binding.edtDescription.text.toString().trim()

            if (edit == "1") {
                apiCallEditFamily()
            } else {
                if (selectedImageUri == null) {
                    apiCallAddMemberWithOutImg()
                } else {
                    if (fileChosser == "1") {
                        apiCallAddMemberCamera()
                    } else {
                        apiCallAddMember()
                    }
                }

            }


        }


        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar1 = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                DateFormat.getDateInstance().format(newDate.time)
                // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
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

        binding.tvMale.setOnClickListener {
            gender = "1"
            binding.tvMale.setTextColor(Color.parseColor("#A19398"))
            binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
        }
        binding.tvFemale.setOnClickListener {
            gender = "2"
            binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
            binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
            binding.tvMale.setTextColor(Color.parseColor("#9F367A"))

        }
        binding.tvOther.setOnClickListener {
            gender = "3"
            binding.tvOther.setTextColor(Color.parseColor("#A19398"))
            binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }


    private fun popupUpload() {
        val view = layoutInflater.inflate(R.layout.dialog_upload_report, null)
        dialog = Dialog(this@AddNewFamily)
        val layoutSnapshot = view!!.findViewById<LinearLayout>(R.id.layoutSnapshot)
        val layoutGallery = view!!.findViewById<LinearLayout>(R.id.layoutGallery)
        val layoutPDF = view!!.findViewById<LinearLayout>(R.id.layoutPDF)
        val imgClose = view!!.findViewById<ImageView>(R.id.imgClose)

        //      val minute = view!!.findViewById<TextView>(R.id.imgClose)
//        val second = view!!.findViewById<TextView>(R.id.tvSecondTime)
        dialog = Dialog(this@AddNewFamily)

        val currentTime = SimpleDateFormat("yy/MM/dd HH:m:ss", Locale.getDefault()).format(Date())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)
        // val d1 = format.parse("2023/03/29 11:04:00")
//        Log.e("currentDate", currentTime)
//        Log.e("EndTime", startTime)

        dialog?.show()

        imgClose.setOnClickListener {
            dialog?.dismiss()
        }

        layoutGallery.setOnClickListener {
            dialog?.dismiss()
            fileChosser = "2"
            openImageChooser()


        }

        layoutSnapshot.setOnClickListener {
            fileChosser = "1"
            dialog?.dismiss()
            ImagePicker.with(this).cameraOnly()
//                                            .createIntent { intent ->
//                                startForProfileImageResult.launch(intent)
//                            }
                .start(REQUEST_CODE_IMAGE)
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    dialog?.dismiss()
                    selectedImageUri = data?.data
                    Log.e("data?.data", data?.data.toString())
                    binding.userProfile.setImageURI(selectedImageUri)
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

    private fun apiCallGetRelation() {


        ApiClient.apiService.getRelation()
            .enqueue(object : Callback<ModelFamilyNew> {
                @SuppressLint("LogNotTimber", "SuspiciousIndentation")
                override fun onResponse(
                    call: Call<ModelFamilyNew>, response: Response<ModelFamilyNew>
                ) {
                    try {
                        relationListNew = response.body()!!;
                        if (relationListNew != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(relationListNew.result!!.size)

                            for (i in relationListNew.result!!.indices) {
                                items[i] = relationListNew.result!![i].name
                            }
                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(this@AddNewFamily, R.layout.simple_list_item_1, items)
                            binding.spinnerFamily.adapter = adapter

                            binding.spinnerFamily.setSelection(items.indexOf(relationId));
                            Log.e("relaytion", relationId)




                            binding.spinnerFamily.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        val id = relationListNew.result!![i].stateId
                                        relationId = id.toString()
                                        //   Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }

                        }
                    } catch (e: Exception) {
                        myToast(this@AddNewFamily, "Something went wrong")
                    }

                }

                override fun onFailure(call: Call<ModelFamilyNew>, t: Throwable) {
                    countR3++
                    if (countR3 <= 3) {
                        apiCallGetRelation()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    private fun apiCallAddMember() {

        if (selectedImageUri == null) {
            myToast(this@AddNewFamily, "Select Profile Picture")
            // binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        AppProgressBar.showLoaderDialog(context)

        //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "profile_picture", this)

        ApiClient.apiService.addFamily(
            sessionManager.id.toString(), firstName, lastName,
            dob, gender, relationId, email, description,
            MultipartBody.Part.createFormData("profile_picture", file.name, body)
        )
            .enqueue(object : Callback<ModelFamilyMember> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamilyMember>, response: Response<ModelFamilyMember>
                ) {
                    try {

                        if (response.body()!!.status == 1) {
                            myToast(this@AddNewFamily, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            refresh()

                        } else {
                            myToast(this@AddNewFamily, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: java.lang.Exception) {
                        myToast(this@AddNewFamily, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelFamilyMember>, t: Throwable) {
                    countR5++
                    if (countR5 <= 3) {
                        apiCallAddMember()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    private fun apiCallAddMemberWithOutImg() {

        AppProgressBar.showLoaderDialog(context)

        //  binding.progressBar.progress = 0

        ApiClient.apiService.addFamilyWithOutImg(
            sessionManager.id.toString(), firstName, lastName,
            dob, gender, relationId, email, description,
        )
            .enqueue(object : Callback<ModelFamilyMember> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamilyMember>, response: Response<ModelFamilyMember>
                ) {
                    try {

                        if (response.body()!!.status == 1) {
                            myToast(this@AddNewFamily, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            refresh()

                        } else {
                            myToast(this@AddNewFamily, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: java.lang.Exception) {
                        myToast(this@AddNewFamily, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelFamilyMember>, t: Throwable) {
                    countR3++
                    if (countR3 <= 3) {
                        apiCallGetRelation()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    private fun apiCallAddMemberCamera() {

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

        AppProgressBar.showLoaderDialog(this@AddNewFamily)

        //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file1, "image", this)

        ApiClient.apiService.addFamily(
            sessionManager.id.toString(), firstName, lastName,
            dob, gender, relationId, email, description,
            MultipartBody.Part.createFormData("profile_picture", file.name, body)
        ).enqueue(object :
            Callback<ModelFamilyMember> {
            override fun onResponse(
                call: Call<ModelFamilyMember>,
                response: Response<ModelFamilyMember>
            ) {
                try {
                    response.body()?.let {
                        if (response.code() == 500) {
                            myToast(this@AddNewFamily, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@AddNewFamily, response.body()!!.message)
                            onBackPressed()
//                    binding.layoutRoot.snackbar(it.message)
//                    binding.progressBar.progress = 100
                            AppProgressBar.hideLoaderDialog()
                        }

                    }
                } catch (e: java.lang.Exception) {
                    myToast(this@AddNewFamily, "Something went wrong")
                    e.printStackTrace()
                    AppProgressBar.hideLoaderDialog()

                }
            }


            override fun onFailure(call: Call<ModelFamilyMember>, t: Throwable) {
                countR4++
                if (countR4 <= 3) {
                    apiCallAddMemberCamera()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
            }

        })

    }


    private fun apiCallEditFamily() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.editFamily(
            id,
            firstName,
            lastName,
            dob,
            gender,
            relationId,
            email,
            description
        )
            .enqueue(object : Callback<ModelFamilyMember> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFamilyMember>, response: Response<ModelFamilyMember>
                ) {
                    try {

                        if (response.body()!!.status == 1) {
                            myToast(this@AddNewFamily, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            refreshValue = "1"
                            onBackPressed()
                            //refresh()

                        } else {
                            myToast(this@AddNewFamily, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        myToast(this@AddNewFamily, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelFamilyMember>, t: Throwable) {
                    countR6++
                    if (countR6 <= 3) {
                        apiCallEditFamily()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })
    }

    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    class Data() {

        // fields which are common across class objects
        companion object {
            var refreshValue = ""
        }
    }

    override fun onStart() {
        super.onStart()
        if (isOnline(this)) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
    }
}
