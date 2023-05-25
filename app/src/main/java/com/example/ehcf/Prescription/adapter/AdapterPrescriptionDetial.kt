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
        holder.doctorName.text = list.result[position].doctor_name
        holder.address.text = list.result[position].address
        holder.registration.text = list.result[position].registration
        holder.note.text = list.result[position].doctor_notes
        holder.coustmorName.text = sessionManager.customerName.toString()
        holder.date.text = list.result[position].date
        holder.subjectiveInformation.text = list.result[position].subjective_information
        holder.objectiveInformation.text = list.result[position].objective_information
        holder.assessment.text = list.result[position].assessment
        holder.plan.text = list.result[position].plan
        holder.tvRegistrationPreDetial.text = list.result[position].registration

       // holder.endTime.text = list.result[position].end_time

        when(list.result[position].gender){
            "1"->{
                holder.gender.text="Male"
            }
            "2"->{
                holder.gender.text="Female"
            }
            else->{
                holder.gender.text="Other"
            }
        }

    }

    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNamePreDetial)
        val address: TextView = itemView.findViewById(R.id.tvAddressPreDetial)
        val registration: TextView = itemView.findViewById(R.id.tvRegistrationPreDetial)
        val gender: TextView = itemView.findViewById(R.id.tvGenderPreDetial)
        val note: TextView = itemView.findViewById(R.id.doctor_notes)
        val subjectiveInformation: TextView = itemView.findViewById(R.id.subjectiveInformation)
        val objectiveInformation: TextView = itemView.findViewById(R.id.objectiveInformation)
        val assessment: TextView = itemView.findViewById(R.id.assessment)
        val plan: TextView = itemView.findViewById(R.id.plan)
        val tvRegistrationPreDetial: TextView = itemView.findViewById(R.id.tvRegistrationPreDetial)
        val coustmorName: TextView = itemView.findViewById(R.id.tvCoustmorNamePreDet)
        val date: TextView = itemView.findViewById(R.id.tvDatePreDetialPreDetial)
//        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
//        val endTime: TextView = itemView.findViewById(R.id.tvEndTimePending)



    }

}