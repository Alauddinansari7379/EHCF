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
import com.example.ehcf.Appointments.Appointments
import com.example.ehcf.Appointments.UpComing.activity.AppointmentDetails
import com.example.ehcf.Appointments.UpComing.activity.UpComingFragment
import com.example.ehcf.Appointments.UpComing.model.ModelAppointments
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingNew
import com.example.ehcf.Appointments.UpComing.model.ResultXXX
import com.example.ehcf.Appointments.UpComing.model.ResultXXXX
import com.example.ehcf.Fragment.HomeFragment
import com.example.ehcf.Helper.changeDateFormatNew
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.R
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class AdapterAppointments(
    val context: Context,
    private val list: ArrayList<ResultXXXX>,
    val showPopUp: ShowPopUp
) :
    RecyclerView.Adapter<AdapterAppointments.MyViewHolder>() {
    lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming, parent, false)
        )
    }

    var currentDate: String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)
        holder.appointmentDate.text = list[position].date
        holder.doctorName.text = list[position].doctor_name.toString()

        if (list[position].profile_image!!.isNotEmpty()) {
            Picasso.get().load("${sessionManager.imageurl}${list[position].profile_image}")
                .placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile);
        }

        if (list[position].member_name != null) {
            holder.tvPatientName.text = list[position].member_name

        } else {
            holder.tvPatientName.text = list[position].customer_name
        }
        //  holder.startTime.text = list.result[position].start_time
        if (list[position].start_time != null) {
            holder.startTime.text = convertTo12Hour(list[position].start_time.toString())
            holder.endTime.text = convertTo12Hour(list[position].end_time.toString())
        }
        holder.tvStatus.text = list[position].status_name
        holder.consultationType.text = list[position].consultation_type

        when (list[position].slug) {
            "waiting_for_accept" -> {
                holder.btnCheck.visibility = View.GONE
                holder.btnJoinMeeting.visibility = View.GONE
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
        when (list[position].consultation_type) {
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
        Log.e("startTime", list[position].date + " " + list[position].time)

        if (list[position].date + " " + list[position].start_time <= currentDate && list[position].slug == "accepted" &&
            list[position].consultation_type == "1"
        ) {
            holder.btnJoinMeeting.visibility = View.VISIBLE
            holder.btnCheck.visibility = View.GONE

        }
        if (list[position].date + " " + list[position].start_time <= currentDate && list[position].slug == "accepted"
            && list[position].consultation_type == "2"
        ) {
            holder.btnJoinMeeting.visibility = View.GONE
            holder.btnCheck.visibility = View.GONE

        }
        if (list[position].date + " " + list[position].start_time <= currentDate && list[position].slug == "accepted"
            && list[position].consultation_type == "3"
        ) {
            holder.btnJoinMeeting.visibility = View.GONE
            holder.btnCheck.visibility = View.GONE
        }
//        holder.bookingId.text = list.result.upcoming[position].id.toString()
//        holder.title.text = list.result[position].title.toString()
//        holder.status.text = list.result[position].status_name.toString()
        //   Picasso.get().load(list.result.upcoming[position].profile_image).into(holder.profile)

        holder.btnCheck.setOnClickListener {

            if (list[position].start_time != null) {
                showPopUp.popupRemainingTime(list[position].date?.let { it1 ->
                    changeDateFormatNew(it1)
                } + " " + list[position].start_time)

            }


            // showPopUp.showPopup()

        }
        holder.btnJoinMeeting.setOnClickListener {
            showPopUp.videoCall(
                list[position].date + "EHCF" + list[position].start_time,
                list[position].id
            )

        }

//        holder.btnJoinMeeting.setOnClickListener {
//            showPopUp.videoCall(list.result[position].time)
//
//        }
        holder.btnView.setOnClickListener {
            val intent = Intent(context as Activity, AppointmentDetails::class.java)
                .putExtra("bookingId", list[position].id.toString())
            context.startActivity(intent)
        }
//        holder.btnOkDialog.setOnClickListener {
//            showPopUp.dismissPopup()
//
//        }

        if (HomeFragment.homeCall == "1") {
            holder.itemView.setOnClickListener {
                val intent = Intent(context as Activity, Appointments::class.java)
                    .putExtra("bookingId", list[position].id.toString())
                context.startActivity(intent)
            }
        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDate)
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val tvPatientName: TextView = itemView.findViewById(R.id.tvPatientName)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
        val endTime: TextView = itemView.findViewById(R.id.tvEndTime)
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

    interface ShowPopUp {
        //  fun showPopup()
        fun popupRemainingTime(startTime: String)

        fun videoCall(startTime: String, id: String)
        //  fun dismissPopup()

    }
}