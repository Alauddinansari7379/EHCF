package com.example.ehcf.Upload.adapter

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
import com.example.ehcf.Appointments.Consulted.activity.ConsultedFragment
import com.example.ehcf.Appointments.UpComing.activity.AppointmentDetails
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Appointments.UpComing.model.ResultXXX
import com.example.ehcf.FamailyMember.Model.ModelFamily
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.Prescription.PrescriptionDetails
import com.example.ehcf.R
import com.example.ehcf.RatingAndReviews.Rating
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterFamilyMember(val context: Context, private val list: ModelFamily) :
    RecyclerView.Adapter<AdapterFamilyMember.MyViewHolder>() {
   // private val filteredData = ArrayList(list)
        lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_completed, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
sessionManager= SessionManager(context)
        try {
            holder.appointmentDate.text = list.result[position].date
            holder.doctorName.text = list.result[position].doctor_name.toString()


            if(ConsultedFragment.adapter=="2"){
                if (list.result[position].profile_picture!=null){
                    Picasso.get().load("${sessionManager.imageurl}${list.result[position].profile_picture}").placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile);

                }
            }
            if(ConsultedFragment.adapter=="1"){
                            if (list.result[position].profile_image!=null){
                Picasso.get().load("${sessionManager.imageurl}${list.result[position].profile_image}").placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile);

            }
            }




            if (list.result[position].member_name != null) {
                holder.patientNameCom.text = list.result[position].member_name

            } else {
                holder.patientNameCom.text = list.result[position].customer_name
            }

            if (list.result[position].start_time != null) {
                holder.startTime.text = convertTo12Hour(list.result[position].start_time)
                holder.endTime.text = convertTo12Hour(list.result[position].end_time)
            }
            holder.status.text = list.result[position].status_name
            holder.amount.text = list.result[position].total

//            if (list.result[position].status_name == "Priscribed") {
//                holder.btnViewPrescription.visibility = View.VISIBLE
//            }
            holder.btnViewPrescription.setOnClickListener {
                val intent = Intent(context as Activity, PrescriptionDetails::class.java)
                    .putExtra("id", list.result[position].prescriptionid)
                    .putExtra("doctorName", list.result[position].doctor_name)
                    .putExtra("customer_name", list.result[position].customer_name)
                    .putExtra("member_id", list.result[position].member_id)
                    .putExtra("date", list.result[position].date)
                context.startActivity(intent)
            }

            if (list.result[position].rating == "0") {
                holder.btnSubmitReview.visibility = View.VISIBLE
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
            holder.btnSubmitReview.setOnClickListener {
                val intent = Intent(context as Activity, Rating::class.java)
                    .putExtra("meetingId", list.result[position].id.toString())
                context.startActivity(intent)
            }
//        holder.btnOkDialog.setOnClickListener {
//            showPopUp.dismissPopup()
//
//        }

            // Glide.with(hol der.image).load(list[position].url).into(holder.image)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
//    fun filter(query: String) {
//        filteredData.clear()
//        if (query.isEmpty()) {
//            filteredData.addAll(list)
//        } else {
//            val filteredList = list.filter { it.contains(query, ignoreCase = true) }
//            filteredData.addAll(filteredList)
//        }
//        notifyDataSetChanged()
//    }


        override fun getItemCount(): Int {
            return list.result.size

        }

        open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDateCom)
            val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNameCom)
            val startTime: TextView = itemView.findViewById(R.id.tvStartTimeCom)
            val endTime: TextView = itemView.findViewById(R.id.tvEndTimeCom)
            val status: TextView = itemView.findViewById(R.id.tvStatusCom)
            val consultationType: TextView = itemView.findViewById(R.id.tvConsultationTypeCan)
            val amount: TextView = itemView.findViewById(R.id.tvTotalAmountCom)
            val patientNameCom: TextView = itemView.findViewById(R.id.tvPatientNameCom)
            val btnSubmitReview: Button = itemView.findViewById(R.id.btnSubmitReview)
            val btnViewPrescription: Button = itemView.findViewById(R.id.btnViewPrescription)

            // val endTime: TextView = itemView.findViewById(R.id.tvEndTime)
            val profile: ImageView = itemView.findViewById(R.id.imgProfileCom)


        }
    }