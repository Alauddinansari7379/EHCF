package com.example.ehcf.Pharmacy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Pharmacy.activity.ProductDetail
import com.example.ehcf.Pharmacy.activity.ProductListing
import com.example.ehcf.Pharmacy.model.Result
import com.example.ehcf.R
import com.example.ehcf.databinding.SingleRowListingBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso

class AdapterListing(
    val context: Context,
    var list: ArrayList<Result>,
    val addToCart: AddToCart
) : RecyclerView.Adapter<AdapterListing.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowListingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleRowListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                binding.tvName.text = product_name
                binding.description.text = description
                binding.price.text = "₹ $price"
                binding.marketPrice.paintFlags = binding.marketPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.marketPrice.text = "₹ $marked_price"



                if (list[position].image != null) {
                    Picasso.get().load(sessionManager.imageurl + list[position].image)
                        .placeholder(R.drawable.placeholder_n)
                        .error(R.drawable.error_placeholder)
                        .into(binding.image)
                }
                binding.cardMedicine.setOnClickListener {
                    val intent = Intent(context as Activity, ProductDetail::class.java)
                        .putExtra("id", id.toString())
                        .putExtra("product_number", product_number.toString())
                        .putExtra("sub_category_id", sub_category_id.toString())
                    context.startActivity(intent)

                }
//                holder.itemView.setOnClickListener {
//                    val intent = Intent(context as Activity, ProductDetail::class.java)
//                        .putExtra("vendor_id", vendor_id.toString())
//                        .putExtra("sub_category_id", sub_category_id.toString())
//                    context.startActivity(intent)
//
//                }
                binding.tvAddCart.setOnClickListener {
                    addToCart.addToCart(product_number.toString())
                 }
            }



        }

    }
    interface AddToCart{
        fun addToCart(id:String)

    }



}
