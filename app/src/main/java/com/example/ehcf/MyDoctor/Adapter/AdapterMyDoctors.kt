package com.example.ehcf.MyDoctor.Adapter

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
import com.example.ehcf.MyDoctor.Model.ResultMyDoctor
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterMyDoctors(
    val context: Context,
    private val list: ArrayList<ResultMyDoctor>,
    val videoCall: VideoCall
) :
    RecyclerView.Adapter<AdapterMyDoctors.MyViewHolder>() {
    lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_all_doctor, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.id.text= "${position+1}"
        // holder.id.text= list.result[position].id
        sessionManager = SessionManager(context)
        try {
            if (list[position].profile_image.isNotEmpty()) {
                Picasso.get()
                    .load("${sessionManager.imageurl}${list[position].profile_image}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(holder.imgProfile);

                //  Picasso.get().load("https://ehcf.in/uploads/${list.result[position].profile_image}").into(holder.imgProfile)
            }

            holder.hospitalName.text = list[position].doctor_name
            holder.exprince.text = list[position].experience
            // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)
            holder.tvAdressMyDoctor.text =
                list[position].address + " " + list[position].city + " " + list[position].state

            holder.btnBookApp.setOnClickListener {
                val intent = Intent(context as Activity, DoctorProfile::class.java)
                    .putExtra("doctorId", list[position].id)
                context.startActivity(intent)
            }
            holder.btnJoin.setOnClickListener {
                videoCall.videoCall(list[position].id + "EHCFHIS" + sessionManager.id)
            }


            when (list[position].specialist) {
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
            }

            // Glide.with(hol der.image).load(list[position].url).into(holder.image)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)

        //  val id: TextView = itemView.findViewById(R.id.tvIdAllDoctor)
        val exprince: TextView = itemView.findViewById(R.id.tvexprinceAllDoctorN)
        val specialities: TextView = itemView.findViewById(R.id.tvSpecialitiesAllDoctor1)
        val tvAdressMyDoctor: TextView = itemView.findViewById(R.id.tvAdressMyDoctor)
        val btnBookApp: Button = itemView.findViewById(R.id.btnBookAppAllDoctor)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        val btnJoin: Button = itemView.findViewById(R.id.btnJoin)


    }

    interface VideoCall {
        fun videoCall(id: String)
    }
}