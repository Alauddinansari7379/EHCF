package com.example.ehcf.Pharmacy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Pharmacy.activity.BrowseMedicine
import com.example.ehcf.Pharmacy.model.ResultXXXXXX
import com.example.ehcf.R
import com.example.ehcf.databinding.SingleRowOrderDetailBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso

class AdapterOrderDetail(
    val context: Context,
    var list: List<ResultXXXXXX>,
) : RecyclerView.Adapter<AdapterOrderDetail.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowOrderDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleRowOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        with(holder) {
            with(list[position]) {
                if (BrowseMedicine.slug == "test") {
                    binding.tvName.text = test_name
                } else {
                    binding.tvName.text = product_name
                }
                binding.description.text = description
                binding.price.text = totalCoast.toString()
                binding.qty.text = "Qty $qty"
                if (image != null) {
                    Picasso.get().load(sessionManager.imageurl + image)
                        .placeholder(R.drawable.placeholder_n)
                        .error(R.drawable.error_placeholder)
                        .into(binding.image)
                }


                // binding.count.text= "${position+1}. "


                // binding.marketPrice.paintFlags = binding.marketPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


            }
        }
    }

    interface Cart {
        fun addToCart(id: String)
        fun removeToCart(id: String)
    }

}
