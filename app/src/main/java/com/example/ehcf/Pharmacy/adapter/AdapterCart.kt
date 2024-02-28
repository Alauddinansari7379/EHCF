package com.example.ehcf.Pharmacy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Pharmacy.model.ModelMedicine
import com.example.ehcf.R
import com.example.ehcf.databinding.SingleRowCartListBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso

class AdapterCart(
    val context: Context,
    var list: ModelMedicine,
    val cart: Cart
) : RecyclerView.Adapter<AdapterCart.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowCartListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleRowCartListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.result.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        with(holder) {
            with(list.result[position]) {
               // binding.count.text= "${position+1}. "
                 binding.name.text = product_name
                binding.price.text = "₹ $price"
                binding.description.text = description
                binding.qty.text = quantity
                binding.totalPrice.text = "₹ $total_price"
                binding.marketPrice.text = "₹ $marked_price".toString()
                binding.marketPrice.paintFlags = binding.marketPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


                binding.layoutPlush.setOnClickListener {
                    cart.addToCart(cart_id.toString())
                }

                binding.layoutDelete.setOnClickListener {
                    cart.removeToCart(cart_id.toString())
                }

                binding.layoutMinus.setOnClickListener {
                    cart.decreaseToCart(cart_id.toString())
                }
                if (image != null) {
                    Picasso.get().load(sessionManager.imageurl + image)
                        .placeholder(R.drawable.placeholder_n)
                        .error(R.drawable.error_placeholder)
                        .into(binding.image)
                }
            }

        }
    }
    interface Cart{
        fun addToCart(id:String)
        fun removeToCart(id:String)
        fun decreaseToCart(id:String)
    }

}
