package com.example.ehcf.Dashboard.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.Fragment.Model.ModelNearByDoctor
import com.example.ehcf.Fragment.Model.ResultX
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterNearByDoctor(val context: Context, private val list: ArrayList<ResultX>) :
    RecyclerView.Adapter<AdapterNearByDoctor.MyViewHolder>() {
lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_all_doctor_home, parent, false)
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.id.text= "${position+1}"
        try {
            sessionManager= SessionManager(context)
            if (list[position].profile_image!=null){
                Picasso.get().load("${sessionManager.imageurl}${list[position].profile_image}").placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile_image);
            }
            holder.hospitalName.text = list[position].doctor_name
            //   holder.specialities.text = list.result.doctor_list[position].specialist

           // holder.tvAddressAllDoctor.text = list[position].address
            holder.exeprince.text = list[position].experience
            if (list[position].clinic_address!=null){
                holder.tvClinicAddress.text = list[position].clinic_address
            }

            if (list[position].online_status==0){
                holder.imgOnlineRed.visibility=View.VISIBLE
            }else{
                holder.imgOnline.visibility=View.VISIBLE

            }


            // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)

            holder.btnBookApp.setOnClickListener {
                AdapterAllDoctor.dashboard="1"
                val intent = Intent(context as Activity, DoctorProfile::class.java)
                    .putExtra("doctorId", list[position].id.toString())
                 context.startActivity(intent)
            }


            when (list[position].specialist.toString()) {
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

    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val profile_image: ImageView = itemView.findViewById(R.id.imgProfile)
        val specialities: TextView = itemView.findViewById(R.id.tvSpecialitiesAllDoctor1)
        val imgOnline: ImageFilterButton = itemView.findViewById(R.id.imgOnline)
        val imgOnlineRed: ImageFilterButton = itemView.findViewById(R.id.imgOnlineRed)
         val tvClinicAddress: TextView = itemView.findViewById(R.id.tvClinicAddress)
        val exeprince: TextView = itemView.findViewById(R.id.tvexprinceAllDoctorN)
        val btnBookApp: Button = itemView.findViewById(R.id.btnBookAppAllDoctor)


    }
}