package com.example.ehcf.Fragment.adapter

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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.Appointments
import com.example.ehcf.Appointments.UpComing.activity.AppointmentDetails
import com.example.ehcf.Appointments.UpComing.activity.UpComingFragment
import com.example.ehcf.Appointments.UpComing.model.ModelAppointments
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingNew
import com.example.ehcf.Helper.changeDateFormatNew
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterAppointmentsHome(val context: Context, private val list: ModelUpComingNew,) :
    RecyclerView.Adapter<AdapterAppointmentsHome.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming_home, parent, false)
        )
    }
    var currentDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.appointmentDate.text = list.result[position].date
        holder.doctorName.text = list.result[position].doctor_name.toString()
        holder.startTime.text = convertTo12Hour(list.result[position].start_time)
        holder.endTime.text = convertTo12Hour(list.result[position].end_time)
        holder.tvStatus.text = list.result[position].status_name
        holder.consultationType.text = list.result[position].consultation_type

        when (list.result[position].slug) {
            "waiting_for_accept" -> {
            }
//                "waiting_for_accept" -> {
//                holder.btnCheck.visibility = View.GONE
//                holder.btnJoinMeeting.visibility = View.GONE
//
//            }


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
        }
        Log.e("currentDate", currentDate)
        Log.e("startTime", list.result[position].date+" "+list.result[position].time)
        if (list.result[position].date+" "+list.result[position].start_time<=currentDate && list.result[position].slug=="accepted" &&
             list.result[position].consultation_type=="1")
        {

        }
        if (list.result[position].date+" "+list.result[position].start_time<=currentDate && list.result[position].slug=="accepted"
            && list.result[position].consultation_type=="2")
        {

        }
        if (list.result[position].date+" "+list.result[position].start_time<=currentDate && list.result[position].slug=="accepted"
            && list.result[position].consultation_type=="3")
        {
        }
//        holder.bookingId.text = list.result.upcoming[position].id.toString()
//        holder.title.text = list.result[position].title.toString()
//        holder.status.text = list.result[position].status_name.toString()
     //   Picasso.get().load(list.result.upcoming[position].profile_image).into(holder.profile)


//        holder.btnJoinMeeting.setOnClickListener {
//            showPopUp.videoCall(list.result[position].time)
//
//        }
        holder.btnView.setOnClickListener {
            val intent = Intent(context as Activity, AppointmentDetails::class.java)
                .putExtra("bookingId",list.result[position].id.toString())
            context.startActivity(intent)
        }

        holder.cardAppointmentHome.setOnClickListener {
            val intent = Intent(context as Activity, Appointments::class.java)
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
          val endTime: TextView = itemView.findViewById(R.id.tvEndTime)
          val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
          val consultationType: TextView = itemView.findViewById(R.id.tvConsultationType)
          val profile: ImageView = itemView.findViewById(R.id.imgProfile)
        val btnView: Button = itemView.findViewById(R.id.btnViewUpcomingHome)
        val cardAppointmentHome: CardView = itemView.findViewById(R.id.cardAppointmentHome)

        //  val btnOkDialog: Button = itemView.findViewById(R.id.btnOkDialog)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)


    }
    interface ShowPopUp{
      //  fun showPopup()
        fun popupRemainingTime(startTime: String)

        fun videoCall(startTime: String, id: String)
      //  fun dismissPopup()

    }
}