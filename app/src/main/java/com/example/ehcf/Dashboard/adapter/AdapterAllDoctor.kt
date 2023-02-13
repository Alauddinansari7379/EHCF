package com.example.ehcf.Dashboard.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile


class AdapterAllDoctor(val context: Context, private val list: ModelAllDoctorNew) :
    RecyclerView.Adapter<AdapterAllDoctor.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_all_doctor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.id.text= "${position+1}"
        holder.id.text= list.result.doctor_list[position].id
        holder.hospitalName.text = list.result.doctor_list[position].hospital_name.toString()
       // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)

        holder.btnBookApp.setOnClickListener {
            val intent = Intent(context as Activity, DoctorProfile::class.java)
                .putExtra("doctorId",list.result.doctor_list[position].admin_user_id)
            context.startActivity(intent)
        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.doctor_list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val id: TextView = itemView.findViewById(R.id.tvIdAllDoctor)
        val specialities: TextView = itemView.findViewById(R.id.tvSpecialitiesAllDoctor)
        val btnBookApp: Button = itemView.findViewById(R.id.btnBookAppAllDoctor)


    }
}