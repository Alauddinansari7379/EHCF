package com.example.ehcf.report.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.report.activity.AddReport
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.R
import com.example.ehcf.report.model.ModelGetTest
import java.text.SimpleDateFormat
import java.util.*


class AdapterAppReport(
    val context: Context,
    private val list: ModelGetTest,
    val uploadAdd: AddReport
) :
    RecyclerView.Adapter<AdapterAppReport.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_add_report, parent, false)
        )
    }

    var currentDate: String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        try {

            holder.testName.text = list.result[position].test_name
            holder.testInstraction.text = list.result[position].instructions
//        holder.doctorName.text = list.result[position].doctor_name.toString()
//        holder.startTime.text = list.result[position].time
//        holder.tvStatus.text = list.result[position].status_for_customer
//        holder.consultationType.text = list.result[position].consultation_type


            if (list.result[position].test_report != null) {
                holder.reportStatus.text = "Uploaded"
                holder.reportStatus.setTextColor(Color.parseColor("#FF1B5E20"))
            } else {
                holder.reportStatus.text = "Pending"
                holder.reportStatus.setTextColor(Color.parseColor("#FFB71C1C"))
            }

//
//        if ( holder.btnUpload.text =="Disabled"){
//            holder.btnUpload.isEnabled = false
//        }

//        when(list.result[position].is_test){
//            "0"->{
//                holder.cardView.visibility=View.GONE
//            }
//        }

//        holder.bookingId.text = list.result.upcoming[position].id.toString()
//        holder.title.text = list.result[position].title.toString()
//        holder.status.text = list.result[position].status_name.toString()
            //   Picasso.get().load(list.result.upcoming[position].profile_image).into(holder.profile)
//
//        holder.btnCheck.setOnClickListener {
//            showPopUp.showPopup()
//
//        }
            holder.layoutPDF.setOnClickListener {
                uploadAdd.selectPDF()
//            holder.btnUpload.isEnabled = true
//            holder.btnUpload.text = "Upload"

            }
            holder.layoutGallery.setOnClickListener {
                uploadAdd.selectImage()
//            holder.btnUpload.isEnabled = true
//            holder.btnUpload.text = "Upload"

            }

            holder.layoutCamera.setOnClickListener {
                uploadAdd.camera()
//            holder.btnUpload.isEnabled = true
//            holder.btnUpload.text = "Upload"
            }

            holder.btnUpload.setOnClickListener {
                uploadAdd.upload(list.result[position].id!!)

            }

//        holder.btnJoinMeeting.setOnClickListener {
//            showPopUp.videoCall(list.result[position].time)
//
//        }
//        holder.btnUpload.setOnClickListener {
//            val intent = Intent(context as Activity, AppointmentDetails::class.java)
//                .putExtra("bookingId",list.result[position].id.toString())
//            context.startActivity(intent)
//        }
//        holder.btnOkDialog.setOnClickListener {
//            showPopUp.dismissPopup()
//
//        }

            // Glide.with(hol der.image).load(list[position].url).into(holder.image)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testName: TextView = itemView.findViewById(R.id.tvTestNameAddRe)
        val testInstraction: TextView = itemView.findViewById(R.id.tvTestInstraction)
        val reportStatus: TextView = itemView.findViewById(R.id.reportStatus)
        val layoutGallery: LinearLayout = itemView.findViewById(R.id.layoutGallery)
        val layoutPDF: LinearLayout = itemView.findViewById(R.id.layoutPDF)
        val layoutCamera: LinearLayout = itemView.findViewById(R.id.layoutCamera)
        val btnUpload: TextView = itemView.findViewById(R.id.btnUploadRe)
        val cardView: CardView = itemView.findViewById(R.id.cardAddReport)


    }

    interface UploadImage {
        fun selectImage()
        fun selectPDF()
        fun camera()
        fun upload(id: String)
        //  fun dismissPopup()

    }
}