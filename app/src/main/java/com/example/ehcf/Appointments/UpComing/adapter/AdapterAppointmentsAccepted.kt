package com.example.ehcf.Appointments.UpComing.adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.UpComing.activity.AppointmentDetails
import com.example.ehcf.Appointments.UpComing.activity.UpComingFragment
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Appointments.UpComing.model.ModelAppointments
import com.example.ehcf.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterAppointmentsAccepted(val context: Context, private val list: ModelAppointmentBySlag, val showPopUp: UpComingFragment) :
    RecyclerView.Adapter<AdapterAppointmentsAccepted.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming, parent, false)
        )
    }
    var currentDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.appointmentDate.text = list.result[position].date
        holder.doctorName.text = list.result[position].doctor_name.toString()
        holder.startTime.text = list.result[position].time
        holder.tvStatus.text = list.result[position].status_for_customer

        when (list.result[position].slug) {
            "waiting_for_accept" -> {
                holder.btnCheck.visibility = View.GONE
                holder.btnJoinMeeting.visibility = View.GONE


            }


//            1 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#FF0000"))
//                holder.btnStart.text = "Stop"
//            }
//            2 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#119241"))
//                holder.btnStart.text = "Done"
//            }
        }
        when (list.result[position].consultation_type) {
            "1" -> {
                holder.consultationType.text = "Tele-Consultation"


            }
            "2" -> {
                holder.consultationType.text = "Clinic-Visit"


            }
            "3" -> {
                holder.consultationType.text = "Home-Visit"


            }


//            1 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#FF0000"))
//                holder.btnStart.text = "Stop"
//            }
//            2 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#119241"))
//                holder.btnStart.text = "Done"
//            }
        }

        Log.e("currentDate", currentDate)
        Log.e("startTime", list.result[position].date+" "+list.result[position].time)
        if (list.result[position].date+" "+list.result[position].time<=currentDate && list.result[position].slug=="accepted" &&
             list.result[position].consultation_type=="1")
        {
            holder.btnJoinMeeting.visibility = View.VISIBLE
            holder.btnCheck.visibility = View.VISIBLE

        }
        if (list.result[position].date+" "+list.result[position].time<=currentDate && list.result[position].slug=="accepted"
            && list.result[position].consultation_type=="2")
        {
            holder.btnJoinMeeting.visibility = View.GONE
            holder.btnCheck.visibility = View.VISIBLE

        }
        if (list.result[position].date+" "+list.result[position].time<=currentDate && list.result[position].slug=="accepted"
            && list.result[position].consultation_type=="3")
        {
            holder.btnJoinMeeting.visibility = View.GONE
            holder.btnCheck.visibility = View.VISIBLE
        }
//        holder.bookingId.text = list.result.upcoming[position].id.toString()
//        holder.title.text = list.result[position].title.toString()
//        holder.status.text = list.result[position].status_name.toString()
     //   Picasso.get().load(list.result.upcoming[position].profile_image).into(holder.profile)

        holder.btnCheck.setOnClickListener {
           // showPopUp.showPopup()

        }
        holder.btnJoinMeeting.setOnClickListener {
            showPopUp.videoCall(list.result[position].date+" "+list.result[position].time,list.result[position].id)

        }

//        holder.btnJoinMeeting.setOnClickListener {
//            showPopUp.videoCall(list.result[position].time)
//
//        }
        holder.btnView.setOnClickListener {
            val intent = Intent(context as Activity, AppointmentDetails::class.java)
                .putExtra("bookingId",list.result[position].id.toString())
            context.startActivity(intent)
        }
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
          val consultationType: TextView = itemView.findViewById(R.id.tvConsultationType)
          val profile: ImageView = itemView.findViewById(R.id.imgProfile)
          val btnCheck: Button = itemView.findViewById(R.id.btnCheck)
          val btnJoinMeeting: Button = itemView.findViewById(R.id.btnJoinMeeting)
        val btnView: Button = itemView.findViewById(R.id.btnViewUpcoming)

        //  val btnOkDialog: Button = itemView.findViewById(R.id.btnOkDialog)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)


    }
    interface ShowPopUp{
        fun showPopup()
        fun videoCall(startTime: String, id: String)
      //  fun dismissPopup()

    }
}