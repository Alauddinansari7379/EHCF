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
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterAllDoctor(val context: Context, private val list: ModelAllDoctorNew) :
    RecyclerView.Adapter<AdapterAllDoctor.MyViewHolder>() {
        lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_all_doctor_home, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.id.text= "${position+1}"
        sessionManager= SessionManager(context)
        try {
            if (list.result.doctor_list[position].profile_image!=null){
                Picasso.get().load("${sessionManager.imageurl}${list.result.doctor_list[position].profile_image}").placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile_image);
            }
            holder.hospitalName.text = list.result.doctor_list[position].hospital_name
            //   holder.specialities.text = list.result.doctor_list[position].specialist
            holder.tvAddressAllDoctor.text = list.result.doctor_list[position].address
            holder.exeprince.text = list.result.doctor_list[position].experience

            // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)

            holder.btnBookApp.setOnClickListener {
                dashboard="1"
                val intent = Intent(context as Activity, DoctorProfile::class.java)
                    .putExtra("doctorId", list.result.doctor_list[position].admin_user_id)
                context.startActivity(intent)
            }


            when (list.result.doctor_list[position].specialist) {
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
                else -> {
                    holder.specialities.text = "Other"

                }
            }


            // Glide.with(hol der.image).load(list[position].url).into(holder.image)

        }catch (e:Exception){

            e.printStackTrace()
        }

    }
    companion object{
        var dashboard=""
    }


    override fun getItemCount(): Int {
        return list.result.doctor_list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val profile_image: ImageView = itemView.findViewById(R.id.imgProfile)
        val specialities: TextView = itemView.findViewById(R.id.tvSpecialitiesAllDoctor1)
        val tvAddressAllDoctor: TextView = itemView.findViewById(R.id.tvAddressAllDoctor)
        val exeprince: TextView = itemView.findViewById(R.id.tvexprinceAllDoctorN)
        val btnBookApp: Button = itemView.findViewById(R.id.btnBookAppAllDoctor)


    }
}