package com.example.ehcf.Prescription.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.Prescription.ReportMain
import com.example.ehcf.Prescription.model.ModelPrescribed
import com.example.ehcf.R


class AdapterPrescribed(
    val context: Context, private val list: ModelPrescribed
) :
    RecyclerView.Adapter<AdapterPrescribed.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_prescribed, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        holder.startTime.text = list.result[position].start_time
        holder.endTime.text = list.result[position].end_time
        holder.bookingDate.text = list.result[position].date
        holder.specialitiesNamePrescribed.text = list.result[position].category_name
        holder.doctorName.text = list.result[position].doctor_name
      //  holder.doctorName.text = list.result[position].n.toString()

        holder.btnAddReport.setOnClickListener {
            val intent = Intent(context as Activity, ReportMain::class.java)
                .putExtra("id",list.result[position].id.toString())
            context.startActivity(intent)

        }

        holder.btnViewPrescription.setOnClickListener {
            val intent = Intent(context as Activity, PrescriptionDetails::class.java)
                .putExtra("id",list.result[position].id.toString())
            context.startActivity(intent)

        }

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val bookingDate: TextView = itemView.findViewById(R.id.tvBookingDatePrescribed)
          val specialitiesNamePrescribed: TextView = itemView.findViewById(R.id.tvSpecialitiesNamePrescribed)
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNamePrescribed)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePrescribed)
          val endTime: TextView = itemView.findViewById(R.id.tvEndTimePrescribed)
          val btnAddReport: Button = itemView.findViewById(R.id.btnAddReportPrescribed)
          val btnViewPrescription: Button = itemView.findViewById(R.id.btnViewPrescriptionPrescribed)
        //  val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescriptionPPending)
          val cardView: CardView = itemView.findViewById(R.id.cardViewPre)


    }

}