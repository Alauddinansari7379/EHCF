package com.example.ehcf.Appointments.UpComing.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
 import com.example.ehcf.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class AdapterUpComing(val context: Context, private val list: ModelAppointmentBySlag, val showPopUp:ShowPopUp) :
    RecyclerView.Adapter<AdapterUpComing.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming, parent, false)
        )
    }
    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        // holder.SrNo.text= "${position+1}"
//
//        holder.appointmentDate.text = list.result[position].start_time.substring(0,10)
//        holder.doctorName.text = list.result.upcoming[position].doctor_name
//        holder.startTime.text = list.result.upcoming[position].start_time.substring(10)
//        holder.tvStatus.text = list.result.upcoming[position].status_for_customer
//
//        when (list.result.upcoming[position].slug) {
//            "waiting_for_confirmation" -> {
//                holder.btnCheck.visibility = View.GONE
//
//            }
//
//
////            1 -> {
////                holder.btnStart.setBackgroundColor(Color.parseColor("#FF0000"))
////                holder.btnStart.text = "Stop"
////
    //}
//            2 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#119241"))
//                holder.btnStart.text = "Done"
//            }
     //   }
//
//        if (list.result.upcoming[position].start_time <=currentDate )
//        {
//            holder.btnJoinMeeting.visibility = View.VISIBLE
//            holder.btnCheck.visibility = View.GONE
//
//        }
////        holder.bookingId.text = list.result.upcoming[position].id.toString()
////        holder.title.text = list.result[position].title.toString()
////        holder.status.text = list.result[position].status_name.toString()
//     //   Picasso.get().load(list.result.upcoming[position].profile_image).into(holder.profile)
//
//        holder.btnCheck.setOnClickListener {
//            showPopUp.showPopup()
//
//        }
//        holder.btnJoinMeeting.setOnClickListener {
//            showPopUp.videoCall(list.result.upcoming[position].start_time)
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
          val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDate)
          val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
          val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
          val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
          val profile: ImageView = itemView.findViewById(R.id.imgProfile)
          val btnCheck: Button = itemView.findViewById(R.id.btnCheck)
          val btnJoinMeeting: Button = itemView.findViewById(R.id.btnJoinMeeting)
        //  val btnOkDialog: Button = itemView.findViewById(R.id.btnOkDialog)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)


    }
    interface ShowPopUp{
        fun showPopup()
        fun videoCall(startTime:String)
      //  fun dismissPopup()

    }
}