package com.example.ehcf.Dashboard.adapter

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
import com.example.ehcf.Dashboard.modelResponse.ModelScarchByLocationAndSpc
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterFilteredDoctor(val context: Context, private val list: ModelScarchByLocationAndSpc) :
    RecyclerView.Adapter<AdapterFilteredDoctor.MyViewHolder>() {
        lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_search_doctor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.id.text= "${position+1}"
        sessionManager= SessionManager(context)
        if (list.result[position].profile_image!!.isNotEmpty()){
            Picasso.get().load("${sessionManager.imageurl}${list.result[position].profile_image}").placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.imgProfile);
        }
        holder.doctorName.text = list.result[position].doctor_name
        holder.address.text = list.result[position].address
        holder.exeprince.text = list.result[position].experience
        // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)

        holder.btnBookApp.setOnClickListener {
            val intent = Intent(context as Activity, DoctorProfile::class.java)
                .putExtra("doctorId", list.result[position].id)
            context.startActivity(intent)
        }

        when (list.result[position].specialist) {
            "1" -> {
                holder.specialities.text = "PSYCHOLOGIST"
            }
            "2" -> {
                holder.specialities.text = "SEXOLOGIST"
            }
            "3" -> {
                holder.specialities.text = "DERMATOLOGIST"
            }
            "4" -> {
                holder.specialities.text = "GYNEACOLOGIST"
            }
            "5" -> {
                holder.specialities.text = "GENERAL PHYSICIAN"
            }
            "6" -> {
                holder.specialities.text = "ANESTHESIA"
            }
            "7" -> {
                holder.specialities.text = "GASTROENTEROLOGIST"
            }
            "8" -> {
                holder.specialities.text = "CARDIOLOGIST"
            }
            "9" -> {
                holder.specialities.text = "DENTIST"
            }
            "10" -> {
                holder.specialities.text = "DIABETOLOGIST"
            }
            "11" -> {
                holder.specialities.text = "ENT SPECIALIST"
            }
            "12" -> {
                holder.specialities.text = "GENERAL SURGEON"
            }
            "13" -> {
                holder.specialities.text = "IVF (TEST TUBE BABY)"
            }
            "14" -> {
                holder.specialities.text = "NEPHROLOGIST"
            }
            "15" -> {
                holder.specialities.text = "OPTHALMOLOGIST (EYE SPECIALIST)"
            }
            "16" -> {
                holder.specialities.text = "ORTHOPEDICS"
            }
            "17" -> {
                holder.specialities.text = "PAEDIATRICIAN"
            }
            "18" -> {
                holder.specialities.text = "PHYSIOTHERAPY"
            }
            "19" -> {
                holder.specialities.text = "UROLOGIST"
            }


            // Glide.with(hol der.image).load(list[position].url).into(holder.image)
        }
    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val exeprince: TextView = itemView.findViewById(R.id.tvexprinceAllDoctor)
        val address: TextView = itemView.findViewById(R.id.tvAddressAllDoctor)
        val specialities: TextView = itemView.findViewById(R.id.tvSpecialitiesAllDoctor)
        val btnBookApp: Button = itemView.findViewById(R.id.btnBookAppAllDoctor)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)


    }
}