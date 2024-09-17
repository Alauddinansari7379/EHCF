package com.example.ehcf.Specialities.adapter

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.CreateSlot.activity.MyAvailableSlot
import com.example.ehcf.Dashboard.adapter.AdapterAllDoctor
import com.example.ehcf.R
import com.example.ehcf.Specialities.activity.DoctorProfile
import com.example.ehcf.Specialities.activity.DoctorProfile.Companion.consaltationList
import com.example.ehcf.Specialities.activity.DoctorProfile.Companion.consultationTypeId
import com.example.ehcf.Specialities.model.ModelConsaltation
import com.example.ehcf.Specialities.model.ModelDoctorProfile
import com.example.ehcf.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso
import java.util.ArrayList


class AdapterDoctorProfile
    (
    val context: Context,
    private val list: ModelDoctorProfile,
    private val consaltaton: DoctorProfile,
    val comment: CommentList
) :
    RecyclerView.Adapter<AdapterDoctorProfile.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_doctor_profile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {

            sessionManager = SessionManager(context)
            if (list.result[position].profile_image!!.isNotEmpty()) {
                Picasso.get()
                    .load("${sessionManager.imageurl}${list.result[position].profile_image}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(holder.imgProfile);
            }
            // holder.id.text= "${position+1}"
            holder.doctorName.text = list.result[position].doctor_name
            holder.exeprince.text = list.result[position].experience
            holder.qualification.text = list.result[position].qualification
            holder.location.text =
                list.result[position].address + " " + list.result[position].city + " " + list.result[position].state
            holder.description.text = list.result[position].description
            holder.price.text = list.result[position].pricing
            if (list.result[position].overall_ratings != null) {
                holder.ratingBar.rating = list.result[position].overall_ratings.toFloat()
                holder.overAllRating.text = list.result[position].overall_ratings.toString()
                holder.tvNoOfrating.text = list.result[position].no_of_ratings
            }

            sessionManager = SessionManager(context)

            when (list.result[position].gender) {
                "1" -> {
                    list.result[position].gender = "Male"
                }

                "2" -> {
                    list.result[position].gender = "Female"
                }

                else -> {
                    list.result[position].gender = "Other"

                }
            }
            when (list.result[position].specialist) {
                "1" -> {
                    holder.specialistDProfile.text = "PSYCHOLOGIST"
                }

                "2" -> {
                    holder.specialistDProfile.text = "SEXOLOGIST"
                }

                "3" -> {
                    holder.specialistDProfile.text = "DERMATOLOGIST"
                }

                "4" -> {
                    holder.specialistDProfile.text = "GYNEACOLOGIST"
                }

                "5" -> {
                    holder.specialistDProfile.text = "GENERAL PHYSICIAN"
                }

                "6" -> {
                    holder.specialistDProfile.text = "ANESTHESIA"
                }

                "7" -> {
                    holder.specialistDProfile.text = "GASTROENTEROLOGIST"
                }

                "8" -> {
                    holder.specialistDProfile.text = "CARDIOLOGIST"
                }

                "9" -> {
                    holder.specialistDProfile.text = "DENTIST"
                }

                "10" -> {
                    holder.specialistDProfile.text = "DIABETOLOGIST"
                }

                "11" -> {
                    holder.specialistDProfile.text = "ENT SPECIALIST"
                }

                "12" -> {
                    holder.specialistDProfile.text = "GENERAL SURGEON"
                }

                "13" -> {
                    holder.specialistDProfile.text = "IVF (TEST TUBE BABY)"
                }

                "14" -> {
                    holder.specialistDProfile.text = "NEPHROLOGIST"
                }

                "15" -> {
                    holder.specialistDProfile.text = "OPTHALMOLOGIST (EYE SPECIALIST)"
                }

                "16" -> {
                    holder.specialistDProfile.text = "ORTHOPEDICS"
                }

                "17" -> {
                    holder.specialistDProfile.text = "PAEDIATRICIAN"
                }

                "18" -> {
                    holder.specialistDProfile.text = "PHYSIOTHERAPY"
                }

                "19" -> {
                    holder.specialistDProfile.text = "UROLOGIST"
                }

                else -> {
                    holder.specialistDProfile.text = "Other"

                }
            }
            holder.gender.text = list.result[position].gender
            holder.description.text = list.result[position].description
//       // Picasso.get().load(list.result.doctor_list[position].category_image).into(holder.image)
//
            holder.btnBookApp.setOnClickListener {
                sessionManager.pricing = list.result[position].pricing
                // val intent = Intent(context as Activity, PaymentMode::class.java)
                val intent = Intent(context as Activity, MyAvailableSlot::class.java)
                    .putExtra("doctorId", list.result[position].id.toString())
                context.startActivity(intent)
                /*
                            when(sessionManager.bookingType){
                                "1"->{
                                    sessionManager.pricing=list.result[position].pricing
                                   // val intent = Intent(context as Activity, PaymentMode::class.java)
                                    val intent = Intent(context as Activity, MyAvailableSlot::class.java)
                                        .putExtra("dashboard",1)
                                        .putExtra("doctorId",list.result[position].id.toString())
                                    context.startActivity(intent)
                                }
                                "2"->{
                //                    val intent = Intent(context as Activity, DateForConsultation::class.java)
                //                        .putExtra("doctorId",list.result[position].id.toString())
                //                    context.startActivity(intent)
                                    sessionManager.pricing=list.result[position].pricing

                                    val intent = Intent(context as Activity, MyAvailableSlot::class.java)
                                        .putExtra("doctorId",list.result[position].id.toString())
                                    context.startActivity(intent)
                                }
                                "3"->{
                //                    val intent = Intent(context as Activity, DateForConsultation::class.java)
                //                        .putExtra("doctorId",list.result[position].id.toString())
                //                    context.startActivity(intent)
                                    sessionManager.pricing=list.result[position].pricing
                                    val intent = Intent(context as Activity, MyAvailableSlot::class.java)
                                        .putExtra("doctorId",list.result[position].id.toString())
                                    context.startActivity(intent)
                                }
                //                else->{
                //                    sessionManager.pricing=list.result[position].pricing
                //                    consaltaton.consaltationType(list.result[position].id.toString())
                //
                //
                //
                //                }
                            }
                */


//                holder.spinnerBookingType.adapter = ArrayAdapter<ModelConsaltation>(
//                    context,
//                    android.R.layout.simple_list_item_1,
//                    consaltationListNew
//                )


            }

            // Glide.with(hol der.image).load(list[position].url).into(holder.image)

            holder.tvRatingReview.setOnClickListener {
                comment.commentList()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (AdapterAllDoctor.dashboard == "1") {
            AdapterAllDoctor.dashboard = ""
            holder.layspinnerBookingTypeNew.visibility = View.VISIBLE
        }
        holder.spinnerBookingType.adapter = ArrayAdapter<ModelConsaltation>(
            context, R.layout.spinner_layout,
            DoctorProfile.consaltationList
        )
        holder.spinnerBookingType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    if (consaltationList.size > 0) {
                        consultationTypeId = consaltationList[i].id
                        sessionManager.bookingType = consultationTypeId
                        Log.e(ContentValues.TAG, "consultationTypeId: $consultationTypeId")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorDProfile)
        val exeprince: TextView = itemView.findViewById(R.id.tvExprinceDProfile)
        val qualification: TextView = itemView.findViewById(R.id.tvQualificationDProfile)
        val specialistDProfile: TextView = itemView.findViewById(R.id.tvSpecialistDProfile)
        val gender: TextView = itemView.findViewById(R.id.tvGenderDProfile)
        val price: TextView = itemView.findViewById(R.id.tvPriceDProfile)
        val location: TextView = itemView.findViewById(R.id.tvAddressAllDoctor)
        val description: TextView = itemView.findViewById(R.id.tvDescriptionDProfile)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBarDProfile)
        val overAllRating: TextView = itemView.findViewById(R.id.tvOverAllRating)
        val tvNoOfrating: TextView = itemView.findViewById(R.id.tvNoOfrating)
        val tvRatingReview: TextView = itemView.findViewById(R.id.tvRatingReview)
        val btnBookApp: Button = itemView.findViewById(R.id.btnBookAppDProfile)
        val cardView: CardView = itemView.findViewById(R.id.cardDoctorProfile)
        val spinnerBookingType: Spinner = itemView.findViewById(R.id.spinnerBookingTypeNew)
        val layspinnerBookingTypeNew: LinearLayout =
            itemView.findViewById(R.id.layspinnerBookingTypeNew)


    }

    interface CommentList {
        fun commentList()
    }
}