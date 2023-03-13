package com.example.ehcf.Appointments.Consulted.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.R


class AdapterConsulted(val context: Context, private val list: ModelAppointmentBySlag) :
    RecyclerView.Adapter<AdapterConsulted.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_completed, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"


        holder.appointmentDate.text = list.result[position].date
        holder.doctorName.text = list.result[position].doctor_name.toString()
        holder.startTime.text = list.result[position].time
        holder.status.text = list.result[position].status_for_customer
        holder.amount.text = list.result[position].total

//        holder.btnCheck.setOnClickListener {
//            showPopUp.showPopup()
//
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
          val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDateCom)
          val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNameCom)
          val startTime: TextView = itemView.findViewById(R.id.tvStartTimeCom)
          val status: TextView = itemView.findViewById(R.id.tvStatusCom)
          val amount: TextView = itemView.findViewById(R.id.tvTotalAmountCom)
         // val endTime: TextView = itemView.findViewById(R.id.tvEndTime)
          val profile: ImageView = itemView.findViewById(R.id.imgProfileCom)



    }
}