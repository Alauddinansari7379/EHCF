package com.example.ehcf.Specialities.adapter

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
import com.example.ehcf.OnlineDoctor.model.ModelOnlineDoctor
import com.example.ehcf.PaymentMode
import com.example.ehcf.R


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
        holder.fee.text = list.result[position].consultation_fee.toString()
      //  Picasso.get().load(list.result[position].profile_image).into(holder.imgProfile)
//
        holder.btnConsultNow.setOnClickListener {
            val intent = Intent(context as Activity, PaymentMode::class.java)
                .putExtra("doctorId",list.result[position].id.toString())
            context.startActivity(intent)
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
      //  val symptomName: TextView = itemView.findViewById(R.id.tvSymptomNameOnline)
      //  val language: TextView = itemView.findViewById(R.id.tvLanguageOnline)
        val fee: TextView = itemView.findViewById(R.id.tvFeeOnline)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfileOnline)
        val btnConsultNow: Button = itemView.findViewById(R.id.btnConsultNowOnline)


    }
}