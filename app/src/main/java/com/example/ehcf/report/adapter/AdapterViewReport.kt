package com.example.ehcf.report.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.R
import com.example.ehcf.report.activity.ViewReport
import com.example.ehcf.report.model.ModelGetTest
import java.text.SimpleDateFormat
import java.util.*


class AdapterViewReport(
    val context: Context,
    private val list: ModelGetTest,
) :
    RecyclerView.Adapter<AdapterViewReport.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_view_report, parent, false)
        )
    }

    var currentDate: String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.tvTestViewDesc.text = list.result[position].instructions
        holder.testName.text = list.result[position].test_name


//        holder.doctorName.text = list.result[position].doctor_name.toString()
//        holder.startTime.text = list.result[position].time
//        holder.tvStatus.text = list.result[position].status_for_customer
//        holder.consultationType.text = list.result[position].consultation_type

//        if (list.result[position].is_test=="0") {
//                holder.cardView.visibility = View.GONE
//
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
//        holder.selectImage.setOnClickListener {
//         //   uploadAdd.selectImage()
//            holder.btnUpload.isEnabled = true
//            holder.btnUpload.text = "Upload"
//
//        }

        if (list.result[position].test_report==null){
            holder.btnViewReport.visibility=View.GONE
            holder.tvReportNotUploaded.visibility=View.VISIBLE
        }
        holder.btnViewReport.setOnClickListener {
            val intent = Intent(context as Activity, ViewReport::class.java)
                .putExtra("report",list.result[position].test_report.toString())
            context.startActivity(intent)
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

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnViewReport: TextView = itemView.findViewById(R.id.btnViewReportView)
        val testName: TextView = itemView.findViewById(R.id.TextName)
        val tvTestViewDesc: TextView = itemView.findViewById(R.id.tvTestViewDesc)
        val tvReportNotUploaded: TextView = itemView.findViewById(R.id.tvReportNotUploaded)
        val cardView: CardView = itemView.findViewById(R.id.cardViewViewRe)


    }

    interface UploadImage {
        fun selectImage()
        fun upload(id: String)
        //  fun dismissPopup()

    }
}