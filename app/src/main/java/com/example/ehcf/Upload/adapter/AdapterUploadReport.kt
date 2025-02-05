package com.example.ehcf.Upload.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.R
import com.example.ehcf.Upload.activity.ReportList
import com.example.ehcf.Upload.model.ResultX
import com.example.ehcf.report.activity.ViewReport


class AdapterUploadReport(private val list: ArrayList<ResultX>, val context: Context, private val deleteReport: ReportList) :
    RecyclerView.Adapter<AdapterUploadReport.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_view_all_report, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            holder.tvReportName.text = list[position].title
            holder.tvReportDate.text = list[position].date

            if (list[position].member_name != null) {
                holder.tvReportMemberName.text = list[position].member_name
            } else {
                holder.tvReportMemberName.text = list[position].customer_name

            }

            holder.btnViewReport.setOnClickListener {
                if (list[position].title=="AyuSynk Report"){
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("${list[position].ayusynk_report}"))
                    context.startActivity(browserIntent)
                }else {
                    val intent = Intent(context as Activity, ViewReport::class.java)
                        .putExtra("report", list[position].report)
                    context.startActivity(intent)
               }
            }

            holder.imgDelete.setOnClickListener {
                deleteReport.deleteReport(list[position].patient_reports_id.toString())

            }

            // Glide.with(hol der.image).load(list[position].url).into(holder.image)


        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val btnViewReport: TextView = itemView.findViewById(R.id.btnViewReportViewAll)
            val tvReportMemberName: TextView = itemView.findViewById(R.id.tvReportMemberName)
            val tvReportName: TextView = itemView.findViewById(R.id.tvReportName)
            val tvReportDate: TextView = itemView.findViewById(R.id.tvReportDate)
            val cardView: CardView = itemView.findViewById(R.id.cardViewViewRe)
            val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)

    }
    interface DeleteReport{
        fun deleteReport(id:String)


    }
}