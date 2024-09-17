package com.example.ehcf.Specialities.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.OnlineDoctor.activity.OnlineDoctor
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.Specialities.activity.FilteredDoctor
import com.example.ehcf.Specialities.model.ModelSplic
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso


class AdapterSpecialities(val context: Context, private val list: ModelSplic,private val consaltaton: DoctorProfile) :
    RecyclerView.Adapter<AdapterSpecialities.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_specialities, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)

        holder.name.text = list.result[position].category_name.toString()
        Picasso.get().load("${sessionManager.imageurl}"+list.result[position].category_image).into(holder.image)


        holder.cardView.setOnClickListener {
            sessionManager.bookingType
            Log.e("BookingType", "${sessionManager.bookingType}")

            when(sessionManager.bookingType){
               "1" ->{
                  // val intent = Intent(context as Activity, OnlineDoctor::class.java)
                   val intent = Intent(context as Activity, FilteredDoctor::class.java)
                        .putExtra("specialitiesID",list.result[position].id)
                     context.startActivity(intent)
               }
                "2" ->{
                   val intent = Intent(context as Activity, FilteredDoctor::class.java)
                        .putExtra("specialitiesID",list.result[position].id)
                     context.startActivity(intent)
               }
                "3" -> {
                    val intent = Intent(context as Activity, FilteredDoctor::class.java)
                        .putExtra("specialitiesID", list.result[position].id)
                    context.startActivity(intent)
                }
//                    else->{
//                        consaltaton.consaltationType(list.result[position].id.toString())
//
//
//
//                    }
            }

        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvNameSpeci)
        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
        val cardView: CardView = itemView.findViewById(R.id.cardView)


    }
    interface ConsaltaionTypeInterFace{
        fun consaltationType(toString: String)
    }
}