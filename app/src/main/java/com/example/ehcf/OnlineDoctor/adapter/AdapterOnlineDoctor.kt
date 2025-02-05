package com.example.ehcf.OnlineDoctor.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.CreateSlot.activity.MyAvailableSlot
import com.example.ehcf.OnlineDoctor.model.ModelOnlineDoctor
import com.example.ehcf.PaymentMode
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile


class AdapterOnlineDoctor(val context: Context, private val list: ModelOnlineDoctor) :
    RecyclerView.Adapter<AdapterOnlineDoctor.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_online_doctor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.id.text= "${position+1}"
        holder.doctorName.text= list.result[position].doctor_name
        holder.specialities.text = list.result[position].specialist
        holder.experience.text = list.result[position].experience
        holder.qualification.text = list.result[position].qualification
       // holder.language.text = list.result[position].toString()
      //  holder.symptomName.text = list.result[position].providing_services.toString()
        holder.consaltaionFee.text = list.result[position].pricing
        holder.addressOnline.text = list.result[position].clinic_address
       //  Picasso.get().load(list.result[position].profile_image).into(holder.imgProfile)
//
//        holder.btnViewProfileOnline.setOnClickListener {
//          //  val intent = Intent(context as Activity, PaymentMode::class.java)
//            val intent = Intent(context as Activity, DoctorProfile::class.java)
//                .putExtra("doctorId",list.result[position].id.toString())
//            context.startActivity(intent)
//        }

        holder.btnTodayAvalible.setOnClickListener {
          //  val intent = Intent(context as Activity, PaymentMode::class.java)
            val intent = Intent(context as Activity, MyAvailableSlot::class.java)
                .putExtra("doctorId",list.result[position].id.toString())
                .putExtra("Online","1")
            context.startActivity(intent)
        }

        when(list.result[position].gender){
            "1"->{
                holder.genderOnline.text="Male"
            }
            "2"-> {
                holder.genderOnline.text="FeMale"
            }
            else->{
                holder.genderOnline.text="Other"

            }

        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvNameOnline)
        val experience: TextView = itemView.findViewById(R.id.tvExprinceOnline)
        val specialities: TextView = itemView.findViewById(R.id.tvSpecialitiesOnline)
        val qualification: TextView = itemView.findViewById(R.id.tvQualificationOnline)
        val consaltaionFee: TextView = itemView.findViewById(R.id.tvFeeOnline)
         val addressOnline: TextView = itemView.findViewById(R.id.tvAddressOnline)
        val genderOnline: TextView = itemView.findViewById(R.id.tvGenderOnline)
       //  val symptomName: TextView = itemView.findViewById(R.id.tvSymptomNameOnline)
      //  val language: TextView = itemView.findViewById(R.id.tvLanguageOnline)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfileOnline)
        val btnViewProfileOnline: Button = itemView.findViewById(R.id.btnViewProfileOnline)
        val btnTodayAvalible: Button = itemView.findViewById(R.id.btnTodayAvalible)


    }
}