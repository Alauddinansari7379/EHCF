package com.example.ehcf.Address.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Address.ModelResponse.AddressListResponse
import com.example.ehcf.R

class AdapterAddressList(val context: Context, private val list: AddressListResponse) :
    RecyclerView.Adapter<AdapterAddressList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_address_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       //  holder.id.text= "${position+1}"
        holder.address.text= list.result[position].address

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)
    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val address: TextView = itemView.findViewById(R.id.tvAddress)
//        val name: TextView = itemView.findViewById(R.id.tvName)



    }
}