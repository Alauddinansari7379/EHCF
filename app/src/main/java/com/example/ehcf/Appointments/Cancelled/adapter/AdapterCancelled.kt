package com.example.ehcf.Appointments.Cancelled.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.Cancelled.model.ModelCancelled
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Appointments.UpComing.model.ModelAppointments
import com.example.ehcf.Appointments.UpComing.model.ResultXXX
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.OnlineDoctor.model.ModelCreateConsultation
import com.example.ehcf.R
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterCancelled(val context: Context, private val list: ArrayList<ResultXXX>) :
    RecyclerView.Adapter<AdapterCancelled.MyViewHolder>() {
    lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_cancelled, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        try {
            sessionManager=SessionManager(context)

//        holder.appointmentDate.text = list.result.cancelled[position].start_time.substring(0,10)
//        holder.doctorName.text = list.result.cancelled[position].doctor_name
//        holder.startTime.text = list.result.cancelled[position].start_time.substring(10)
//        holder.tvStatus.text = list.result.cancelled[position].status_for_customer
//        holder.description.text = list.result.cancelled[position].description
//        holder.totalAmount.text = list.result.cancelled[position].total_amount
            if (list[position].profile_image!!.isNotEmpty()) {
                Picasso.get().load("${sessionManager.imageurl}${list[position].profile_image}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile);


            }
            holder.appointmentDate.text = list[position].date
            holder.doctorName.text = list[position].doctor_name
            if (list[position].member_name != null) {
                holder.tvPatientNameRej.text = list[position].member_name

            } else {
                holder.tvPatientNameRej.text = list[position].customer_name
            }
            // holder.doctorName.text = list.result[position].doctor_name.toString()
            if (list[position].start_time != null) {
                holder.startTime.text = convertTo12Hour(list[position].start_time)
                holder.endTime.text = convertTo12Hour(list[position].end_time)
            }

            holder.tvStatus.text = list[position].status_for_customer
            holder.totalAmount.text = list[position].total


//        when (list.result[position].slug) {
//            "rejected" -> {
//                // holder.visibility(View.INVISIBLE);
//
//                holder.cardView.visibility = View.VISIBLE
//            }else->{
//
//            holder.cardView.visibility = View.GONE
//
//        }
            when (list[position].consultation_type) {
                "1" -> {
                    holder.tvConsultationTypeCan.text = "Tele-Consultation"
                }

                "2" -> {
                    holder.tvConsultationTypeCan.text = "Clinic-Visit"
                }

                "3" -> {
                    holder.tvConsultationTypeCan.text = "Home-Visit"
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
//            1 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#FF0000"))
//                holder.btnStart.text = "Stop"
//            }
//            2 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#119241"))
//                holder.btnStart.text = "Done"
//            }

//        holder.bookingId.text = list.result.upcoming[position].id.toString()
//        holder.title.text = list.result[position].title.toString()
//        holder.status.text = list.result[position].status_name.toString()
        //   Picasso.get().load(list.result.upcoming[position].profile_image).into(holder.profile)


//        holder.btnOkDialog.setOnClickListener {
//            showPopUp.dismissPopup()
//
//        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }

    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDateCan)
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNameCan)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimeCan)
        val endTime: TextView = itemView.findViewById(R.id.tvEndTimeCan)
        val totalAmount: TextView = itemView.findViewById(R.id.tvTotalAmountCan)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusCan)
        val tvPatientNameRej: TextView = itemView.findViewById(R.id.tvPatientNameRej)
        val tvConsultationTypeCan: TextView = itemView.findViewById(R.id.tvConsultationTypeCan)
        val profile: ImageView = itemView.findViewById(R.id.imgProfile)

        //  val btnOkDialog: Button = itemView.findViewById(R.id.btnOkDialog)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
        val cardView: CardView = itemView.findViewById(R.id.cardViewCan)


    }

}