package com.example.ehcf.Specialities.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.DateForConsultaion.DateForConsultation
import com.example.ehcf.Fragment.MainActivity
import com.example.ehcf.R
import com.example.ehcf.Specialities.model.ModelSplic
import com.squareup.picasso.Picasso


class AdapterSpecialities(val context: Context, private val list: ModelSplic) :
    RecyclerView.Adapter<AdapterSpecialities.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_specialities, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.name.text = list.result[position].category_name.toString()
        Picasso.get().load(list.result[position].category_image).into(holder.image)

        holder.cardView.setOnClickListener {
            val intent = Intent(context as Activity, DateForConsultation::class.java)
            context.startActivity(intent)
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
}