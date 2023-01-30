package com.example.ehcf.DateForConsultaion.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Appointments.UpComing.adapter.AdapterUpComing
import com.example.ehcf.DateForConsultaion.activity.ShuduleTiming
import com.example.ehcf.DateForConsultaion.model.ModelSlotRes
import com.example.ehcf.R

class AdapterShuduleTiming(
    var arraylist: ModelSlotRes, var context: Context,
    val showBookPopUp: ShuduleTiming
): RecyclerView.Adapter<AdapterShuduleTiming.Myholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterShuduleTiming.Myholder {

        return Myholder(LayoutInflater.from(context).inflate(R.layout.single_row_slot_timing,parent,false))

    }

    override fun onBindViewHolder(holder: AdapterShuduleTiming.Myholder, position: Int) {

//        Glide.with(context).load(arraylist.get(position).languageImg.toString()).into(holder.data)


        holder.textname.text= arraylist.result?.get(position)?.key

        holder.textname.setOnClickListener {
            showBookPopUp.showPopup(arraylist.result?.get(position)?.key.toString())
           // holder.textname.setBackgroundColor(Color.parseColor("#451DE4"))

        }

    }


    class Myholder(itemview: View) :RecyclerView.ViewHolder(itemview){
//
        //var data:ImageView  =itemview.findViewById(R.id.shayari_rl);

        var textname:TextView  =itemview.findViewById(R.id.tvSlotTime);

    }


    override fun getItemCount(): Int {

        return arraylist.result!!.size;

    }
    interface BookPopUp{
        fun showPopup(slotTime:String)
    }
}