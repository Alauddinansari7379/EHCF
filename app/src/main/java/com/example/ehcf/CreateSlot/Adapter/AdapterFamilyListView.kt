package com.example.ehcf.CreateSlot.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterFamilyListView(
    val context: Context,
    private val list: ModelFamilyList,
    val checkBox: CheckBox,
) :
    RecyclerView.Adapter<AdapterFamilyListView.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_family_member_new, parent, false))
    }

    var currentDate: String? = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    private var lastCheckedPosition = -1
    private var checkeditem = 0


    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        // holder.SrNo.text= "${position+1}"
        holder.familyMember.text = list.result[position].member_name

        if (position == lastCheckedPosition) {
            holder.cardViewViewMember.setCardBackgroundColor(Color.parseColor("#FF4CAF50"));
        } else {
            holder.cardViewViewMember.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }


        holder.itemView.setOnClickListener {
            Log.e("lastCheckedPosition", lastCheckedPosition.toString())
            Log.e("position", position.toString())
            if (lastCheckedPosition==position){
                holder.cardViewViewMember.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                lastCheckedPosition=100
                memberID=""
                memberName=""
                checkBox.checkBox(1)
            }
           else{
                memberID = list.result[position].id
                memberName = list.result[position].member_name
                lastCheckedPosition = position
                notifyItemRangeChanged(0, list.result.size)
                checkBox.checkBox(0)
            }

        }


    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //  val firstName: TextView = itemView.findViewById(R.id.tvFirstNameFM)
        val familyMember: TextView = itemView.findViewById(R.id.familyMember)
        val cardViewViewMember: CardView = itemView.findViewById(R.id.cardViewViewMember)
        //val btnClerFamily: Button = itemView.findViewById(R.id.btnClerFamily)
    }

    companion object {
        var memberID = ""
        var memberName = ""
    }

    interface CheckBox {
        fun checkBox(id:Int)
    }

}