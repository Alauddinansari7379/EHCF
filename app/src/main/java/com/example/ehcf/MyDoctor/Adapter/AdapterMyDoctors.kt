package com.example.ehcf.MyDoctor.Adapter

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
import com.example.ehcf.MyDoctor.Model.ModelMyDoctor
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile


class AdapterMyDoctors(val context: Context, private val list: ModelMyDoctor) :
    RecyclerView.Adapter<AdapterMyDoctors.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_all_doctor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.id.text= "${position+1}"
       // holder.id.text= list.result[position].id
        holder.hospitalName.text = list.result[position].doctor_name
        holder.exprince.text = list.result[position].experience
        holder.specialities.text = list.result[position].specialist
       // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)

//        holder.btnBookApp.setOnClickListener {
//            val intent = Intent(context as Activity, DoctorProfile::class.java)
//                .putExtra("doctorId",list.result.doctor_list[position].admin_user_id)
//            context.startActivity(intent)
//        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val id: TextView = itemView.findViewById(R.id.tvIdAllDoctor)
        val exprince: TextView = itemView.findViewById(R.id.exprinceMyDoctor)
        val specialities: TextView = itemView.findViewById(R.id.tvSpecialitiesAllDoctor)
        val btnBookApp: Button = itemView.findViewById(R.id.btnBookAppAllDoctor)


    }
}