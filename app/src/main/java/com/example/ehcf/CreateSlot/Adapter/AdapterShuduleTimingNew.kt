package com.example.ehcf.CreateSlot.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.CreateSlot.model.ModelSlotResNew
import com.example.ehcf.R


class AdapterShuduleTimingNew(val context: Context, private val list: ModelSlotResNew, val showPopUp:dilog) :
    RecyclerView.Adapter<AdapterShuduleTimingNew.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_slot_timing, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"



        //holder.date.text= list.result[position].date
        holder.startTime.text= list.result[position].start_time
        holder.endTime.text= list.result[position].end_time
        holder.slotId.text= list.result[position].id.toString()

        holder.cardView.setOnClickListener {
            showPopUp.showPopup(list.result?.get(position)?.start_time.toString(),list.result?.get(position)?.end_time.toString(),list.result[position].id.toString())
            // holder.textname.setBackgroundColor(Color.parseColor("#451DE4"))

        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       // var date:TextView  =itemView.findViewById(R.id.tvtDateSTime);
        var startTime:TextView  =itemView.findViewById(R.id.tvStartTimeStime);
        var endTime:TextView  =itemView.findViewById(R.id.tvEndTimeStime);
        var slotId:TextView  =itemView.findViewById(R.id.tvSlotIdSTime);
        var cardView:CardView  =itemView.findViewById(R.id.cardViewSTime);

    }
    interface dilog{
        fun showPopup(slotTime: String, slotTimeValue: String, slotId: String)
        //  fun dismissPopup()

    }
}