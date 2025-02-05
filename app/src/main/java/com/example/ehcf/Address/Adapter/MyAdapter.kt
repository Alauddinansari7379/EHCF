//package com.example.ehcf.Address.Adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.ehcf.R
//
//
//
//public   class MyAdapter(var arraylist: LanguageResponse, var context: Context): RecyclerView.Adapter<MyAdapter.Myholder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.Myholder {
//
//        return Myholder(LayoutInflater.from(context).inflate(R.layout.single_row_address_list,parent,false))
//
//    }
//
//    override fun onBindViewHolder(holder: MyAdapter.Myholder, position: Int) {
//
////
////
////        Glide.with(context).load(arraylist.get(position).languageImg.toString()).into(holder.data)
//
//
//        holder.textname.text=arraylist.result.get(position).id
//
//
//
//
//    }
//
//
//    class Myholder(itemview: View) :RecyclerView.ViewHolder(itemview){
////
//        var textname:TextView  =itemview.findViewById(R.id.tvAddress);
//
//
//
//
//    }
//
//
//    override fun getItemCount(): Int {
//
//        return arraylist.result.size;
//
//    }
//
//
//
//
//}