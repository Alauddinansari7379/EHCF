package com.example.ehcf.Prescription.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.report.activity.ReportMain
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.Prescription.model.ResultPrescribed
import com.example.ehcf.R
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterPrescribed(
    val context: Context, private val list: ArrayList<ResultPrescribed>
) :
    RecyclerView.Adapter<AdapterPrescribed.MyViewHolder>() {
lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_prescribed, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            sessionManager= SessionManager(context)
            // holder.SrNo.text= "${position+1}"
            var custmorName = ""
            if (list[position].start_time != null) {
                holder.startTime.text = convertTo12Hour(list[position].start_time.toString())
                holder.endTime.text = convertTo12Hour(list[position].end_time.toString())
            }
            if (list[position].profile_image!!.isNotEmpty()){
                Picasso.get().load("${sessionManager.imageurl}${list[position].profile_image}").placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile_image);
             }
            if (list[position].member_name != null) {
                custmorName = list[position].member_name
                holder.tvPatientNamePrescribed.text = list[position].member_name

            } else {
                custmorName = list[position].customer_name
                holder.tvPatientNamePrescribed.text = list[position].customer_name
            }
            holder.bookingDate.text = list[position].date
            holder.specialitiesNamePrescribed.text = list[position].category_name
            holder.doctorName.text = list[position].doctor_name
            //  holder.doctorName.text = list.result[position].n.toString()

            holder.btnAddReport.setOnClickListener {
                val intent = Intent(context as Activity, ReportMain::class.java)
                    .putExtra("id", list[position].id.toString())
                context.startActivity(intent)

            }

            holder.btnViewPrescription.setOnClickListener {
                val intent = Intent(context as Activity, PrescriptionDetails::class.java)
                    .putExtra("id", list[position].id)
                    .putExtra("doctorName", list[position].doctor_name)
                    .putExtra("customer_name", custmorName)
                    .putExtra("member_id", list[position].member_id)
                    .putExtra("member_name", list[position].member_name)
                    .putExtra("date", list[position].date)
                context.startActivity(intent)

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookingDate: TextView = itemView.findViewById(R.id.tvBookingDatePrescribed)
        val specialitiesNamePrescribed: TextView =
            itemView.findViewById(R.id.tvSpecialitiesNamePrescribed)
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNamePrescribed)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePrescribed)
        val endTime: TextView = itemView.findViewById(R.id.tvEndTimePrescribed)
        val profile_image: ImageView = itemView.findViewById(R.id.imgProfile)
        val btnAddReport: Button = itemView.findViewById(R.id.btnAddReportPrescribed)
        val tvPatientNamePrescribed: TextView = itemView.findViewById(R.id.tvPatientNamePrescribed)
        val btnViewPrescription: Button = itemView.findViewById(R.id.btnViewPrescriptionPrescribed)

        //  val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescriptionPPending)
        val cardView: CardView = itemView.findViewById(R.id.cardViewPre)


    }

}