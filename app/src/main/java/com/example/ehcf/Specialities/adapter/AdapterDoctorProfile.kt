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
import com.example.ehcf.CreateSlot.activity.MySlot
import com.example.ehcf.PaymentMode
import com.example.ehcf.R
import com.example.ehcf.Specialities.model.ModelDoctorProfile
import com.example.ehcf.sharedpreferences.SessionManager


class AdapterDoctorProfile(val context: Context, private val list: ModelDoctorProfile) :
    RecyclerView.Adapter<AdapterDoctorProfile.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_doctor_profile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.id.text= "${position+1}"
        holder.doctorName.text= list.result[position].doctor_name
        holder.exeprince.text= list.result[position].experience
        holder.qualification.text = list.result[position].qualification
        holder.additionalQualification.text = list.result[position].additional_qualification
        holder.phoneNumber.text = list.result[position].phone_number
        sessionManager = SessionManager(context)

        when(list.result[position].gender){
            "1"->{
                list.result[position].gender="Male"
            }
            "2"->{
                list.result[position].gender="Female"
            }
            else->{
                list.result[position].gender="Other"

            }
        }
        holder.gender.text = list.result[position].gender
        holder.email.text = list.result[position].email
        holder.description.text = list.result[position].description
//       // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)
//
        holder.btnBookApp.setOnClickListener {
            when(sessionManager.bookingType){
                "1"->{
                    val intent = Intent(context as Activity, PaymentMode::class.java)
                        .putExtra("doctorId",list.result[position].id.toString())
                    context.startActivity(intent)
                }
                "2"->{
                    val intent = Intent(context as Activity, MySlot::class.java)
                        .putExtra("doctorId",list.result[position].id.toString())
                    context.startActivity(intent)
                }
                else->{
                    val intent = Intent(context as Activity, MySlot::class.java)
                        .putExtra("doctorId",list.result[position].id.toString())
                    context.startActivity(intent)

                }
            }

        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorDProfile)
        val exeprince: TextView = itemView.findViewById(R.id.tvExprinceDProfile)
        val qualification: TextView = itemView.findViewById(R.id.tvAdditionalQualificationDProfile)
        val additionalQualification: TextView = itemView.findViewById(R.id.tvAdditionalQualificationDProfile)
        val phoneNumber: TextView = itemView.findViewById(R.id.tvPhoneNumberDProfile)
        val gender: TextView = itemView.findViewById(R.id.tvGenderDProfile)
        val email: TextView = itemView.findViewById(R.id.tvEmailDProfile)
        val description: TextView = itemView.findViewById(R.id.tvDescriptionDProfile)
        val btnBookApp: Button = itemView.findViewById(R.id.btnBookAppDProfile)
        val cardView: CardView = itemView.findViewById(R.id.cardDoctorProfile)


    }
}