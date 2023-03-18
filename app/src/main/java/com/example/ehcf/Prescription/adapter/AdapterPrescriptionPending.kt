package com.example.ehcf.Prescription.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Prescription.model.ModelPrePending
import com.example.ehcf.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterPrescriptionPending(
    val context: Context, private val list: ModelPrePending
) :
    RecyclerView.Adapter<AdapterPrescriptionPending.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_pending_prescription, parent, false)
        )
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        holder.startTime.text = list.result[position].time
        holder.endTime.text = list.result[position].time
        holder.bookingDate.text = list.result[position].date
      //  holder.doctorName.text = list.result[position].n.toString()

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val bookingDate: TextView = itemView.findViewById(R.id.tvBookingDatePPending)
          val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
          val endTime: TextView = itemView.findViewById(R.id.tvEndTimePending)
          val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNamePending)
        //  val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescriptionPPending)
          val cardView: CardView = itemView.findViewById(R.id.cardViewPre)


    }

}