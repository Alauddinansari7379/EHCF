package com.example.ehcf.Diagnostic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Diagnostic.model.Result
import com.example.ehcf.databinding.SingleRowTestListBinding
import com.example.ehcf.sharedpreferences.SessionManager

class AdapterTestList(
    val context: Context,
    var list: ArrayList<Result>,
    private val addToCart: AddToCart
) : RecyclerView.Adapter<AdapterTestList.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowTestListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleRowTestListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                binding.name.text = Test_Name
                binding.description.text = Test_Code
                binding.price.text = "₹ $dos"

//                holder.itemView.setOnClickListener {
//                    val intent = Intent(context as Activity, ProductDetail::class.java)
//                        .putExtra("vendor_id", vendor_id.toString())
//                        .putExtra("sub_category_id", sub_category_id.toString())
//                    context.startActivity(intent)
//
//                }
                binding.btnAdd.setOnClickListener {
                    addToCart.addToCart(product_number.toString())
                }
            }


        }

    }

    interface AddToCart {
        fun addToCart(id: String)

    }


}
