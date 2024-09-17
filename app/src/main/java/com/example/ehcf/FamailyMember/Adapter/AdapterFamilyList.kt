package com.example.ehcf.FamailyMember.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.Consulted.activity.ConsultedFragment
import com.example.ehcf.Appointments.UpComing.activity.AppointmentDetails
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
 import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.FamailyMember.activity.AddNewFamily
import com.example.ehcf.R
import com.example.ehcf.Upload.activity.FamilyMemberHistory
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class AdapterFamilyList(
    val context: Context,
    private val list: ModelFamilyList,
    val showPopUp: EditFamilyMember
) :
    RecyclerView.Adapter<AdapterFamilyList.MyViewHolder>() {
lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_family_member, parent, false)
        )
    }

    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager= SessionManager(context)
        if (list.result[position].profile_picture!=null) {
            Picasso.get()
                .load("${sessionManager.imageurl}${list.result[position].profile_picture}")
                .placeholder(R.drawable.profile).error(R.drawable.profile)
                .into(holder.imgProfileFamily);
        }

        holder.firstName.text = list.result[position].member_name
         holder.dOB.text = list.result[position].dob
        holder.emailFM.text = list.result[position].description

        when (list.result[position].gender) {
            "1" -> {
                holder.gender.text = "Male"

            }
            "2" -> {
                holder.gender.text = "Female"

            }
            else -> {
                holder.gender.text = "Other"

            }
        }

        when (list.result[position].relation) {
            "1" -> {
                holder.relationFM.text = "Father"

            }
            "2" -> {
                holder.relationFM.text = "Father in law"

            }
            "3" -> {
                holder.relationFM.text = "Mother"

            }
            "4" -> {
                holder.relationFM.text = "Mother in law"

            }
            "5" -> {
                holder.relationFM.text = "Brother"

            }
            "6" -> {
                holder.relationFM.text = "Brother in law"

            }
            "7" -> {
                holder.relationFM.text = "Sister"

            }
            "8" -> {
                holder.relationFM.text = "Sister in law"

            }
            "9" -> {
                holder.relationFM.text = "Son"

            }
            "10" -> {
                holder.relationFM.text = "Son in law"

            }
            "11" -> {
                holder.relationFM.text = "Daughter"

            }
            "12" -> {
                holder.relationFM.text = "Daughter in law"

            }
            "13" -> {
                holder.relationFM.text = "Wife"

            }
            "14" -> {
                holder.relationFM.text = "Grand Father"

            }
            "15" -> {
                holder.relationFM.text = "Grand Mother"

            }
            else -> {
                holder.relationFM.text = "Other"

            }
        }
        holder.imgEditFM.setOnClickListener {
            val intent = Intent(context as Activity, AddNewFamily::class.java)
                .putExtra("Edit","1")
                .putExtra("id",list.result[position].id)
                .putExtra("first_name",list.result[position].member_name)
                .putExtra("last_name",list.result[position].last_name)
                .putExtra("dob",list.result[position].dob)
                .putExtra("gender",list.result[position].gender)
                .putExtra("relation",holder.relationFM.text.toString())
                .putExtra("email",list.result[position].description)
            context.startActivity(intent)
        }

        holder.btnAppointmentF.setOnClickListener {
            familyMemberList="1"
            ConsultedFragment.adapter="2"
            AdapterFamilyListView.memberID=list.result[position].id
            val intent = Intent(context as Activity, FamilyMemberHistory::class.java)
                 .putExtra("id",list.result[position].id)
            context.startActivity(intent)
        }

        holder.btnReportF.setOnClickListener {
            familyMemberList="2"
            AdapterFamilyListView.memberID=list.result[position].id
            val intent = Intent(context as Activity, FamilyMemberHistory::class.java)
                 .putExtra("id",list.result[position].id)
            context.startActivity(intent)
        }

        holder.imgDeleteFM.setOnClickListener {
            showPopUp.delete(list.result[position].id)

        }



    }
companion object{
    var familyMemberList=""
}

    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.tvFirstNameFM)
         val dOB: TextView = itemView.findViewById(R.id.tvDOBFM)
        val gender: TextView = itemView.findViewById(R.id.tvGenderFM)
        val relationFM: TextView = itemView.findViewById(R.id.tvRelationFM)
        val emailFM: TextView = itemView.findViewById(R.id.tvEmailFM)
        val btnReportF: TextView = itemView.findViewById(R.id.btnReportF)
        val imgEditFM: ImageView = itemView.findViewById(R.id.imgEditFM)
        val btnAppointmentF: Button = itemView.findViewById(R.id.btnAppointmentF)
        val imgProfileFamily: ImageView = itemView.findViewById(R.id.imgProfileFamily)
        val imgDeleteFM: ImageView = itemView.findViewById(R.id.imgDeleteFM)


    }

    interface EditFamilyMember {
        fun delete(startTime: String)
         //  fun dismissPopup()

    }
}