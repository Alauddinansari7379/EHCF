package com.example.ehcf.retrofit

import com.example.ehcf.Address.ModelResponse.AddAddressResponse
import com.example.ehcf.Address.ModelResponse.AddressListResponse
import com.example.ehcf.Appointments.Cancelled.model.ModelCancelled
import com.example.ehcf.Appointments.Consulted.model.ModelConsultedResponse
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentBySlag
import com.example.ehcf.Appointments.UpComing.model.ModelAppointments
import com.example.ehcf.Appointments.UpComing.model.ModelAppointmentsDetails
import com.example.ehcf.Appointments.UpComing.model.ModelUpComingResponse
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.Dashboard.modelResponse.SearchbyLocationRes
import com.example.ehcf.CreateSlot.model.ModelCreateBooking
import com.example.ehcf.CreateSlot.model.ModelSlotResNew
import com.example.ehcf.OnlineDoctor.model.ModelCreateConsultation
import com.example.ehcf.OnlineDoctor.model.ModelOnlineDoctor
import com.example.ehcf.PhoneNumber.ModelReponse.ForgotPasswordResponse
import com.example.ehcf.Profile.modelResponse.ResetPassResponse
import com.example.ehcf.RatingAndReviews.model.ModelRating
import com.example.ehcf.Registration.ModelResponse.RegistationResponse
import com.example.ehcf.Specialities.model.ModelDoctorProfile
import com.example.ehcf.Specialities.model.ModelFilteredDoctor
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
    fun myAppointmentCancelled(
        @Field("customer_id") customer_id:String,
    ): Call<ModelCancelled>

    @FormUrlEncoded
    @POST("get_booking_requests")
    fun myAppointmentConsulted(
        @Field("customer_id") customer_id:String,
    ): Call<ModelConsultedResponse>

    @POST("get_time_slots")
    fun getTimeSlot(
        @Query("doctor_id") doctorid: String?,
        @Query("date") date: String?
    ): Call<ModelSlotResNew>

    @POST("get_nearest_doctors")
    fun getAllDoctor(
        @Query("lat") lat: String?,
        @Query("lng") lng: String?,
        @Query("search") search: String?
    ): Call<ModelAllDoctorNew>

    @POST("create_consultation")
    fun createBooking(
        @Query("patient_id") patient_id: String?,
        @Query("doctor_id") doctor_id: String?,
        @Query("start_time") start_time: String?,
        @Query("title") title: String?,
        @Query("description") description: String?,
        @Query("total_amount") total_amount: String?,
        @Query("payment_mode") payment_mode: String?,
    ): Call<ModelCreateBooking>

    @POST("get_all")
    fun filteredDoctor(
        @Query("specialist") specialist: String?,
    ): Call<ModelFilteredDoctor>

    @POST("get_doctor_details")
    fun doctorProfile(
        @Query("doctor_id") doctor_id: String?,
    ): Call<ModelDoctorProfile>

    @POST("get_online_doctors")
    fun onlineDoctors(
        @Query("specialist") specialist: String?,
        @Query("search") search: String?,
    ): Call<ModelOnlineDoctor>

    @POST("create_consultation")
    fun createConsultation(
        @Query("patient_id") patient_id: String?,
        @Query("doctor_id") doctor_id: String?,
        @Query("total") total: String?,
        @Query("payment_mode") payment_mode: String?,
        @Query("consultation_type") consultation_type: String?,
        @Query("date") date: String?,
        @Query("time") time: String?,
    ): Call<ModelCreateConsultation>

    @POST("get_consultation_requests")
    fun appointments(
        @Query("customer_id") customer_id: String?,
    ): Call<ModelAppointments>

    @POST("get_consultation_details")
    fun appointmentsDetails(
        @Query("id") id: String?,
    ): Call<ModelAppointmentsDetails>

    @POST("get_consultation_details")
    fun rating(
        @Query("id") id: String?,
        @Query("rating") rating: String?,
        @Query("comments") comments: String?,
    ): Call<ModelRating>

    @POST("get_consultation_requests")
    fun getConsultation(
        @Query("customer_id") customer_id: String?,
        @Query("slug") slug: String?,
    ): Call<ModelAppointmentBySlag>

}