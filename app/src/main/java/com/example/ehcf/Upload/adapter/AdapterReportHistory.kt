package com.example.ehcf.Upload.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.R
import com.example.ehcf.Upload.activity.FamilyMemberHistory
import com.example.ehcf.Upload.activity.ReportList
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.report.activity.ViewReport


class AdapterReportHistory(private val list: ModelGetAllReport, val context: Context) :
    RecyclerView.Adapter<AdapterReportHistory.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_view_all_report_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            holder.tvReportName.text = list.result[position].title
            holder.tvReportDate.text = list.result[position].date

            if (list.result[position].member_name != null) {
                holder.tvReportMemberName.text = list.result[position].member_name
            } else {
                holder.tvReportMemberName.text = list.result[position].customer_name

            }

            holder.btnViewReport.setOnClickListener {
                val intent = Intent(context as Activity, ViewReport::class.java)
                    .putExtra("report", list.result[position].report)
                context.startActivity(intent)
            }



            // Glide.with(hol der.image).load(list[position].url).into(holder.image)


        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val btnViewReport: TextView = itemView.findViewById(R.id.btnViewReportViewAll)
            val tvReportMemberName: TextView = itemView.findViewById(R.id.tvReportMemberName)
            val tvReportName: TextView = itemView.findViewById(R.id.tvReportName)
            val tvReportDate: TextView = itemView.findViewById(R.id.tvReportDate)
            val cardView: CardView = itemView.findViewById(R.id.cardViewViewRe)

    }
    interface DeleteReport{
        fun deleteReport(id:String)


    }
}