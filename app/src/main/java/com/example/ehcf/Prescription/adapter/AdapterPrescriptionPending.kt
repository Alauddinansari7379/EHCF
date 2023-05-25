package com.example.ehcf.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.Prescription.model.ModelPreList
import com.example.ehcf.Prescription.model.My_model
import com.example.ehcf.R


class AdapterPrescriptionPending(val context: Context, private val list: ModelPreList
) :
    RecyclerView.Adapter<AdapterPrescriptionPending.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_pending_prescription, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"


        holder.bookingDate.text = list.result[position].date
        holder.specialitiesName.text = list.result[position].category_name.toString()
        holder.doctorName.text = list.result[position].doctor_name.toString()
        holder.startTime.text = convertTo12Hour(list.result[position].start_time.toString())
        holder.endTime.text = convertTo12Hour(list.result[position].end_time.toString())

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


    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookingDate: TextView = itemView.findViewById(R.id.tvBookingDatePPending)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
        val endTime: TextView = itemView.findViewById(R.id.tvEndTimePending)
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNamePending)
        val specialitiesName: TextView = itemView.findViewById(R.id.tvSpecialitiesNamePreList)

        //  val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescriptionPPending)
        val cardView: CardView = itemView.findViewById(R.id.cardViewPre)


    }

}