package com.example.ehcf.Appointments.UpComing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.UpComing.activity.UpComingFragment
import com.example.ehcf.Appointments.UpComing.model.ModelAppointments
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentsDetails
import com.example.ehcf.R
import java.text.SimpleDateFormat
import java.util.*


//class AdapterAppointmentsDetails(val context: Context, private val list: ModelAppointmentsDetails) :
//    RecyclerView.Adapter<AdapterAppointmentsDetails.MyViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        return MyViewHolder(
//            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming, parent, false)
//        )
//    }
//    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        // holder.SrNo.text= "${position+1}"
//
////        holder.appointmentDate.text = list.result[position].date
////        holder.doctorName.text = list.result[position].doctor_name.toString()
////        holder.startTime.text = list.result[position].time
////        holder.tvStatus.text = list.result[position].status_for_customer
//
//
//        // Glide.with(hol der.image).load(list[position].url).into(holder.image)
//
//    }
//
//
////    override fun getItemCount(): Int {
////      //  return list.result.size
////
////    }
//
//    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//          val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDate)
//          val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
//          val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
//          val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
//          val profile: ImageView = itemView.findViewById(R.id.imgProfile)
//          val btnCheck: Button = itemView.findViewById(R.id.btnCheck)
//          val btnJoinMeeting: Button = itemView.findViewById(R.id.btnJoinMeeting)
//        //  val btnOkDialog: Button = itemView.findViewById(R.id.btnOkDialog)
////        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
////        val cardView: CardView = itemView.findViewById(R.id.cardView)
//
//
//    }
//}