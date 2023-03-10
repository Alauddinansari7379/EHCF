package com.example.ehcf.Specialities.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.Specialities.model.ModelFilteredDoctor


class AdapterFilteredDoctor(val context: Context, private val list: ModelFilteredDoctor) :
    RecyclerView.Adapter<AdapterFilteredDoctor.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_filtered_doctor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.id.text= "${position+1}"
        holder.doctorName.text= list.result[position].doctor_name
        holder.experience.text = list.result[position].experience
        holder.qualification.text = list.result[position].qualification
//       // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)
//
        holder.viewProfile.setOnClickListener {
            val intent = Intent(context as Activity, DoctorProfile::class.java)
                .putExtra("doctorId",list.result[position].id.toString())
            context.startActivity(intent)
        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNameFilter)
        val experience: TextView = itemView.findViewById(R.id.tvExprinceFilter)
        val qualification: TextView = itemView.findViewById(R.id.tvQualification)
        val viewProfile: Button = itemView.findViewById(R.id.btnViewProfileFilter)
        val cardView: CardView = itemView.findViewById(R.id.cardFilter)


    }
}