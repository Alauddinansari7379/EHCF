package com.example.ehcf.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.Prescription.model.ModelPreList
import com.example.ehcf.Prescription.model.My_model
import com.example.ehcf.Prescription.model.ResultPrePending
import com.example.ehcf.R
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterPrescriptionPending(
    val context: Context, private val list: ArrayList<ResultPrePending>
) :
    RecyclerView.Adapter<AdapterPrescriptionPending.MyViewHolder>() {
lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_pending_prescription, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
sessionManager= SessionManager(context)
        try {
            if (list[position].profile_image!!.isNotEmpty()){
                Picasso.get().load("${sessionManager.imageurl}${list[position].profile_image}").placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.imgProfile);
            }
            holder.bookingDate.text = list[position].date
            holder.specialitiesName.text = list[position].category_name.toString()
            holder.doctorName.text = list[position].doctor_name.toString()

            if (list[position].member_name != null) {
                holder.tvPatientNamePending.text = list[position].member_name

            } else {
                holder.tvPatientNamePending.text = list[position].customer_name
            }
            if (list[position].start_time.toString() != null) {
                holder.startTime.text = convertTo12Hour(list[position].start_time.toString())
                holder.endTime.text = convertTo12Hour(list[position].end_time.toString())
            }


//        holder.bookingDate.text = list.result[0].get(position).date
//         holder.specialitiesName.text = list.result[0].get(position).categoryName
//        holder.doctorName.text = list.result[position].get(0).doctorName
//        holder.startTime.text = list.result[position].get(position).startTime
//        holder.endTime.text = list.result[position].get(position).endTime
            // holder.doctorName.text = list.result[position]..toString()

//        when (list.result[position].specialist) {
//            "1" -> {
//                holder.specialitiesName.text = "PSYCHOLOGIST"
//            }
//            "2" -> {
//                holder.specialitiesName.text = "SEXOLOGIST"
//            }
//            "3" -> {
//                holder.specialitiesName.text = "DERMATOLOGIST"
//            }
//            "4" -> {
//                holder.specialitiesName.text = "GYNEACOLOGIST"
//            }
//            "5" -> {
//                holder.specialitiesName.text = "GENERAL PHYSICIAN"
//            }
//            "6" -> {
//                holder.specialitiesName.text = "ANESTHESIA"
//            }
//            "7" -> {
//                holder.specialitiesName.text = "GASTROENTEROLOGIST"
//            }
//            "8" -> {
//                holder.specialitiesName.text = "CARDIOLOGIST"
//            }
//            "9" -> {
//                holder.specialitiesName.text = "DENTIST"
//            }
//            "10" -> {
//                holder.specialitiesName.text = "DIABETOLOGIST"
//            }
//            "11" -> {
//                holder.specialitiesName.text = "ENT SPECIALIST"
//            }
//            "12" -> {
//                holder.specialitiesName.text = "GENERAL SURGEON"
//            }
//            "13" -> {
//                holder.specialitiesName.text = "IVF (TEST TUBE BABY)"
//            }
//            "14" -> {
//                holder.specialitiesName.text = "NEPHROLOGIST"
//            }
//            "15" -> {
//                holder.specialitiesName.text = "OPTHALMOLOGIST (EYE SPECIALIST)"
//            }
//            "16" -> {
//                holder.specialitiesName.text = "ORTHOPEDICS"
//            }
//            "17" -> {
//                holder.specialitiesName.text = "PAEDIATRICIAN"
//            }
//            "18" -> {
//                holder.specialitiesName.text = "PHYSIOTHERAPY"
//            }
//            "19" -> {
//                holder.specialitiesName.text = "UROLOGIST"
//            }
//        }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookingDate: TextView = itemView.findViewById(R.id.tvBookingDatePPending)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
        val endTime: TextView = itemView.findViewById(R.id.tvEndTimePending)
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNamePending)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        val specialitiesName: TextView = itemView.findViewById(R.id.tvSpecialitiesNamePreList)
        val tvPatientNamePending: TextView = itemView.findViewById(R.id.tvPatientNamePending)

        //  val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescriptionPPending)
        val cardView: CardView = itemView.findViewById(R.id.cardViewPre)


    }

}