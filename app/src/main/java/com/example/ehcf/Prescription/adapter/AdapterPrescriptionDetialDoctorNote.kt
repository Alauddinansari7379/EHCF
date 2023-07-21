package com.example.ehcf.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Prescription.model.ModelPreDetJava
import com.example.ehcf.Prescription.model.ModelPreDetial
import com.example.ehcf.R
import com.example.ehcf.sharedpreferences.SessionManager


class AdapterPrescriptionDetialDoctorNote(val context: Context, private val list: ModelPreDetJava
) :
    RecyclerView.Adapter<AdapterPrescriptionDetialDoctorNote.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_prescription_detial_note, parent, false)
        )

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)
        holder.publicNote.text = list.doctorNotes[position].doctorNotes

    }

    override fun getItemCount(): Int {
        return list.doctorNotes.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val publicNote: TextView = itemView.findViewById(R.id.tvPublicNote)





    }

}