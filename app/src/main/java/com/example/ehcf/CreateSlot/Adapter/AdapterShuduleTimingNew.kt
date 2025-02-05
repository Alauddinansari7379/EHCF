package com.example.ehcf.CreateSlot.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.CreateSlot.model.ModelSlotResNew
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.R
import com.example.ehcf.sharedpreferences.SessionManager
import java.text.SimpleDateFormat
import java.util.*


class AdapterShuduleTimingNew(
    val context: Context,
    private val list: ModelSlotResNew,
    val showPopUp: dilog
) :
    RecyclerView.Adapter<AdapterShuduleTimingNew.MyViewHolder>() {

    private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_slot_timing, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)

        var time: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        var currentDate: String = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        //  val currentTime =time+sessionManager.selectedDate

        Log.e("CTime", time)
        Log.e("currentDate", currentDate)
        Log.e("end_time", list.result[position].end_time)
        Log.e("selectedDate", sessionManager.selectedDate.toString())

        //holder.date.text= list.result[position].date
        holder.startTime.text = convertTo12Hour(list.result[position].start_time)
        holder.endTime.text = convertTo12Hour(list.result[position].end_time)
        holder.slotId.text = list.result[position].id.toString()
        if (list.result[position].address!=null){
            holder.tvSlotAddress.text = list.result[position].address.toString()
        }
        when (sessionManager.bookingType) {
            "1" -> {
                 holder.layoutAddress.visibility=View.GONE
            }
            "2" -> {
                 holder.layoutAddress.visibility=View.VISIBLE
            }
            "3" -> {
                 holder.layoutAddress.visibility=View.VISIBLE
            }
        }

        if (list.result[position].end_time < time && currentDate == sessionManager.selectedDate.toString()) {
            holder.cardView.isEnabled = false
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFDDDBDB"))
        }
        else {
            holder.cardView.isEnabled = true
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        }


//        when (list.result[position].end_time <time && currentDate!=sessionManager.selectedDate.toString()) {
//            true -> {
//                holder.cardView.isEnabled = false
//                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFDDDBDB"))
//            }
//            else -> {
//                holder.cardView.isEnabled = true
//                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
//            }
//        }
        holder.cardView.setOnClickListener {
            showPopUp.showPopup(
                list.result?.get(position)?.start_time.toString(),
                list.result?.get(position)?.end_time.toString(),
                list.result[position].id.toString()
            )
            // holder.textname.setBackgroundColor(Color.parseColor("#451DE4"))

        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size
    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // var date:TextView  =itemView.findViewById(R.id.tvtDateSTime);
        var startTime: TextView = itemView.findViewById(R.id.tvStartTimeStime)
        var endTime: TextView = itemView.findViewById(R.id.tvEndTimeStime)
        var slotId: TextView = itemView.findViewById(R.id.tvSlotIdSTime)
        var tvSlotAddress: TextView = itemView.findViewById(R.id.tvSlotAddress)
        var cardView: CardView = itemView.findViewById(R.id.cardViewSTime)
        var layoutAddress: LinearLayout = itemView.findViewById(R.id.layoutAddress)

    }

    interface dilog {
        fun showPopup(slotTime: String, slotTimeValue: String, slotId: String)
        //  fun dismissPopup()

    }
}