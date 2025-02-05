package com.example.ehcf.RatingAndReviews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.Specialities.model.ModelCommentList


class AdapterCommentList
    (
    val context: Context,
    private val list: ModelCommentList,
    private val consaltaton: DoctorProfile
) :
    RecyclerView.Adapter<AdapterCommentList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_comment_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

             holder.comment.text= list.result[position].comments
              holder.tvOverAllRatingComment.text= list.result[position].rating.toString()
              holder.custmoerName.text= list.result[position].customer_name.toString()
            holder.ratingBarComment.rating= list.result[position].rating.toFloat()



        }



    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val comment: TextView = itemView.findViewById(R.id.tvComment)
        val tvOverAllRatingComment: TextView = itemView.findViewById(R.id.tvOverAllRatingComment)
        val custmoerName: TextView = itemView.findViewById(R.id.tvCustmoerName)
        val ratingBarComment: RatingBar = itemView.findViewById(R.id.ratingBarComment)



    }
}