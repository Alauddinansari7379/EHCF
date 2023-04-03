package com.example.ehcf.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Prescription.model.ModelPrescriptionDetial
import com.example.ehcf.Prescription.model.My_model
import com.example.ehcf.R
import com.example.ehcf.sharedpreferences.SessionManager


class AdapterPrescriptionDetial(val context: Context, private val list: ModelPrescriptionDetial
) :
    RecyclerView.Adapter<AdapterPrescriptionDetial.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_prescription_detial, parent, false)
        )

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)
        holder.note.text = list.result[position].doctor_notes
        holder.coustmorName.text = sessionManager.customerName.toString()
        holder.date.text = list.result[position].date

       // holder.endTime.text = list.result[position].end_time

    }

    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val note: TextView = itemView.findViewById(R.id.tvNotePreDetial)
        val coustmorName: TextView = itemView.findViewById(R.id.tvCoustmorNamePreDet)
        val date: TextView = itemView.findViewById(R.id.tvDatePreDetial)
//        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
//        val endTime: TextView = itemView.findViewById(R.id.tvEndTimePending)



    }

}