package com.example.ehcf.retrofit

import com.example.ehcf.Address.ModelResponse.AddAddressResponse
import com.example.ehcf.Address.ModelResponse.AddressListResponse
import com.example.ehcf.Appointments.Consulted.model.ModelConsultedResponse
import com.example.ehcf.DateForConsultaion.model.ModelSlotRes
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingResponse
import com.example.ehcf.Dashboard.modelResponse.SearchbyLocationRes
import com.example.ehcf.PhoneNumber.ModelReponse.ForgotPasswordResponse
import com.example.ehcf.Profile.modelResponse.ResetPassResponse
import com.example.ehcf.Registration.ModelResponse.RegistationResponse
import com.example.ehcf.Specialities.model.ModelSplic
import com.example.ehcf.login.modelResponse.LogInResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("phone_with_code") phone_with_code:String,
        @Field("password") password:String,
        @Field("fcm_token") fcm_token:String,
    ):Call<LogInResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
            @Field("customer_name") customer_name:String,
            @Field("phone_number") phone_number:String,
            @Field("phone_with_code") phone_with_code:String,
            @Field("email") email:String,
            @Field("password") password:String,
            @Field("blood_group") blood_group:String,
            @Field("gender") gender:Int,
            @Field("fcm_token") fcm_token:String,
    ): Call<RegistationResponse>

    @FormUrlEncoded
    @POST("forget_password")
    fun forgotPassword(
        @Field("phone_with_code") phone_with_code:String,
    ): Call<ForgotPasswordResponse>

    @FormUrlEncoded
    @POST("add_address")
    fun addAddress(
        @Field("customer id") customer_id:Int,
        @Field("address") address:String,
        @Field("landmark") landmark:String,
        @Field("lat") lat:String,
        @Field("lng") lng:String,
    ): Call<AddAddressResponse>


    @POST("all_addresses")
    fun allAddress(@Query("customer id") customer_id: Int
    ): Call<AddressListResponse>

    @FormUrlEncoded
    @POST("reset_password")
    fun resetPassword(
        @Field("id") id:String,
        @Field("password") password:String,
    ): Call<ResetPassResponse>

    @FormUrlEncoded
    @POST("get_nearest_doctors")
    fun searchByLocation(
        @Field("lat") lat:String,
        @Field("lng") lng:String,
        @Field("search") search:String,
    ): Call<SearchbyLocationRes>

    @GET("get_doctor_categories")
    fun getPhotos(): Call<ModelSplic>

    @FormUrlEncoded
    @POST("get_booking_requests")
    fun myAppointmentUpComing(
        @Field("customer_id") customer_id:String,
    ): Call<ModelUpComingResponse>

    @FormUrlEncoded
    @POST("get_booking_requests")
    fun myAppointmentConsulted(
        @Field("customer_id") customer_id:String,
    ): Call<ModelConsultedResponse>

    @POST("get_time_slots")
    fun getTimeSlot(
        @Query("doctor_id") doctorid: String?,
        @Query("date") date: String?
    ): Call<ModelSlotRes>

}