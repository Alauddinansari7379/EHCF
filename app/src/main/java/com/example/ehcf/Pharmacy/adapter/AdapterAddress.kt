package com.example.ehcf.Pharmacy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Pharmacy.model.ModelMedicine
import com.example.ehcf.databinding.SingleRowAddressListBinding
import com.example.ehcf.sharedpreferences.SessionManager

class AdapterAddress(
    val context: Context,
    var list: ModelMedicine,
    var addressID: AddressID
  ) : RecyclerView.Adapter<AdapterAddress.ViewHolder>() {
    lateinit var sessionManager: SessionManager
    private var lastCheckedRB: RadioButton? = null

    inner class ViewHolder(val binding: SingleRowAddressListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleRowAddressListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                with(binding) {
                    tvAddress.text = "$address $landmark $city $pincode"

//                    if (position==0){
//                         radio.isChecked=true
//                    }

//                    tvAddress.setOnClickListener {
//                        val intent = Intent(context as Activity, ProductDetail::class.java)
//                            .putExtra("id", id.toString())
//                            .putExtra("sub_category_id", sub_category_id.toString())
//                        context.startActivity(intent)
//
//                    }

                    radio.setOnClickListener {
                        if (lastCheckedRB != null) {
                            lastCheckedRB?.isChecked = false
                        }
                        addressID.addressId(id.toString())

                        lastCheckedRB = radio
                    }

//                holder.itemView.setOnClickListener {
//                    val intent = Intent(context as Activity, ProductDetail::class.java)
//                        .putExtra("vendor_id", vendor_id.toString())
//                        .putExtra("sub_category_id", sub_category_id.toString())
//                    context.startActivity(intent)
//
//                }
                }

            }


        }

    }



    interface AddressID {
        fun addressId(id: String)

    }


}
