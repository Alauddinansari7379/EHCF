package com.example.ehcf.Pharmacy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Pharmacy.activity.OrderDetail
import com.example.ehcf.Pharmacy.model.ResultXXX
import com.example.ehcf.databinding.SingleRowOrderListBinding
import com.example.ehcf.sharedpreferences.SessionManager

class AdapterOrderList(
    val context: Context,
    var list: List<ResultXXX>,
) : RecyclerView.Adapter<AdapterOrderList.ViewHolder>() {
    lateinit var sessionManager: SessionManager
    var listValue = ""


    inner class ViewHolder(val binding: SingleRowOrderListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        try {
            with(holder) {
                with(list[position]) {
                    binding.orderNo.text = "EHCFOrder$order_id"
                    binding.statues.text = status
                    binding.cost.text = totalCoast.toString()
                    binding.payment.text = payment_name


                    val pro = ArrayList<String>()
                    product_names.forEach {
                        pro.add(it)

                    }
                    listValue = pro.toString()
                    Log.d("listOrder", listValue)
                    binding.product.text = listValue.replace("[", "").replace("]", "")


                    binding.btnView.setOnClickListener {
                        val intent = Intent(context as Activity, OrderDetail::class.java)
                            .putExtra("order_id", order_id.toString())
                        context.startActivity(intent)
                    }


                    // binding.count.text= "${position+1}. "


                    // binding.marketPrice.paintFlags = binding.marketPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


//                if (image != null) {
//                    Picasso.get().load(sessionManager.imageurl + image)
//                        .placeholder(R.drawable.placeholder_n)
//                        .error(R.drawable.error_placeholder)
//                        .into(binding.image)
//                }


                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    interface Cart {
        fun addToCart(id: String)
        fun removeToCart(id: String)
    }

}
