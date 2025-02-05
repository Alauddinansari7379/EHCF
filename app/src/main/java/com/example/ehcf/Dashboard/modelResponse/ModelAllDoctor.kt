package com.example.ehcf.Dashboard.modelResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class ModelAllDoctor {
    @SerializedName("result")
    @Expose
    var result: List<Result>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

     inner class Result {
         @SerializedName("doctor_list")
         @Expose
         var doctor_list: List<Doctor>?=null
         @SerializedName("recommended")
         @Expose
         val recommended: List<String>?=null

     }
     inner class Doctor{
         @SerializedName("address")
         @Expose
         val address: String?=null
         @SerializedName("admin_user_id")
         @Expose
         val admin_user_id:String?=null
         @SerializedName("appointment_fee")
         @Expose
         val appointment_fee:String?=null
         @SerializedName("closing_time")
         @Expose
         val closing_time:String?=null
         @SerializedName("created_at")
         @Expose
         val created_at: String?=null
         @SerializedName("departments")
         @Expose
         val departments: List<String>?=null
         @SerializedName("description")
         @Expose
         val description: String?=null
         @SerializedName("doctors")
         @Expose
         val doctors: List<String>?=null
         @SerializedName("email")
         @Expose
         val email: String?=null
         @SerializedName("facilities")
         @Expose
         val facilities: List<String>?=null
         @SerializedName("galleries")
         @Expose
         val galleries: List<String>?=null
         @SerializedName("hospital_logo")
         @Expose
         val hospital_logo: String?=null
         @SerializedName("hospital_name")
         @Expose
         val hospital_name: String?=null
         @SerializedName("hospital_services")
         @Expose
         val hospital_services: List<String>?=null
         @SerializedName("id")
         @Expose
         val id: String?=null
         @SerializedName("insurances")
         @Expose
         val insurances: List<String>?=null
         @SerializedName("is_recommended")
         @Expose
         val is_recommended: String?=null
         @SerializedName("latitude")
         @Expose
         val latitude:String?=null
         @SerializedName("longitude")
         @Expose
         val longitude: String?=null
         @SerializedName("no_of_ratings")
         @Expose
         val no_of_ratings: String?=null
         @SerializedName("opening_time")
         @Expose
         val opening_time: String?=null
         @SerializedName("our_labs")
         @Expose
         val our_labs: List<String>?=null
         @SerializedName("our_vendors")
         @Expose
         val our_vendors: List<String>?=null
         @SerializedName("overall_ratings")
         @Expose
         val overall_ratings: String?=null
         @SerializedName("password")
         @Expose
         val password:String?=null
         @SerializedName("phone_number")
         @Expose
         val phone_number: String?=null
         @SerializedName("phone_with_code")
         @Expose
         val phone_with_code: String?=null
         @SerializedName("status")
         @Expose
         val status: String?=null
         @SerializedName("type")
         @Expose
         val type: String?=null
         @SerializedName("updated_at")
         @Expose
         val updated_at: String?=null
         @SerializedName("username")
         @Expose
         val username: String?=null
         @SerializedName("waiting_time")
         @Expose
         val waiting_time: String?=null
         @SerializedName("wallet")
         @Expose
         val wallet: String?=null
         @SerializedName("website")
         @Expose
         val website: String?=null

     }
}