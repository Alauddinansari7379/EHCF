package com.example.ehcf.Notification.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.R
import com.example.ehcf.Specialities.model.ModelSplic
import com.squareup.picasso.Picasso


class Adapter(val context: Context, private val list: ModelSplic) :
    RecyclerView.Adapter<Adapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.id.text = list.result[position].id.toString()
        holder.name.text = list.result[position].category_name.toString()
        holder.userName.text = list.result[position].status
       // holder.email.text = list[position].thumbnailUrl
        Picasso.get().load(list.result[position].category_image).into(holder.image)

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)


    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.tvId)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val email: TextView = itemView.findViewById(R.id.tvEmail)
        val image: ImageView = itemView.findViewById(R.id.image)


    }
}