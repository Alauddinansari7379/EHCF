package com.example.ehcf.Appointments.Consulted.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.Consulted.model.ModelConsultedResponse
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingResponse
import com.example.ehcf.R
import com.squareup.picasso.Picasso


class AdapterConsulted(val context: Context, private val list: ModelConsultedResponse) :
    RecyclerView.Adapter<AdapterConsulted.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.appointmentDate.text = list.result.completed[position].start_time.substring(0,10)
        holder.doctorName.text = list.result.completed[position].doctor_name
        holder.startTime.text = list.result.completed[position].start_time.substring(10)
//        holder.bookingId.text = list.result.upcoming[position].id.toString()
//        holder.title.text = list.result[position].title.toString()
//        holder.status.text = list.result[position].status_name.toString()
     //   Picasso.get().load(list.result.upcoming[position].profile_image).into(holder.profile)

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
        return list.result.completed.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDate)
          val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
          val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
         // val endTime: TextView = itemView.findViewById(R.id.tvEndTime)
          val profile: ImageView = itemView.findViewById(R.id.imgProfile)
          val btnCheck: Button = itemView.findViewById(R.id.btnCheck)
        //  val btnOkDialog: Button = itemView.findViewById(R.id.btnOkDialog)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)


    }
    interface ShowPopUp{
        fun showPopup()
        fun dismissPopup()

    }
}