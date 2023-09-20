package com.example.ehcf.retrofit

import com.example.ehcf.Address.ModelResponse.AddAddressResponse
import com.example.ehcf.Address.ModelResponse.AddressListResponse
import com.example.ehcf.Appointments.Cancelled.model.ModelCancelled
import com.example.ehcf.Appointments.Consulted.model.ModelConsultedResponse
import com.example.ehcf.Appointments.UpComing.model.*
import com.example.ehcf.CreateSlot.model.ModelCreateBooking
import com.example.ehcf.CreateSlot.model.ModelSlotResNew
import com.example.ehcf.Dashboard.modelResponse.ModelAllDoctorNew
import com.example.ehcf.Dashboard.modelResponse.ModelScarchByLocationAndSpc
import com.example.ehcf.Dashboard.modelResponse.ModelSpecilList
import com.example.ehcf.Dashboard.modelResponse.SearchbyLocationRes
import com.example.ehcf.FamailyMember.Model.*
import com.example.ehcf.Fragment.Model.ModelNearestDoctor
import com.example.ehcf.Fragment.test.UploadResponse
import com.example.ehcf.MyDoctor.Model.ModelMyDoctor
import com.example.ehcf.OnlineDoctor.model.ModelCreateConsultation
import com.example.ehcf.OnlineDoctor.model.ModelOnlineDoctor
import com.example.ehcf.PhoneNumber.ModelReponse.ModelForgotPass
import com.example.ehcf.Prescription.model.*
import com.example.ehcf.Profile.modelResponse.ModelProfilePic
import com.example.ehcf.Profile.modelResponse.ModelUpdateNameEmail
import com.example.ehcf.Profile.modelResponse.ResetPassResponse
import com.example.ehcf.RatingAndReviews.model.ModelRating
import com.example.ehcf.Registration.ModelResponse.ModelOTP
import com.example.ehcf.Registration.ModelResponse.RegistationResponse
import com.example.ehcf.Specialities.model.ModelCommentList
import com.example.ehcf.Specialities.model.ModelDoctorProfile
import com.example.ehcf.Specialities.model.ModelFilteredDoctor
import com.example.ehcf.Specialities.model.ModelSplic
import com.example.ehcf.Upload.model.ModelDeleteRep
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.Upload.model.ModelUploadReport
import com.example.ehcf.login.modelResponse.LogInResponse
import com.example.ehcf.report.model.ModelGetTest
import com.example.ehcf_doctor.Invoice.model.ModelInvoice
import com.example.ehcf_doctor.Invoice.model.ModelInvoiceDetial
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {


    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("phone_with_code") phone_with_code: String,
        @Field("password") password: String,
        @Field("fcm_token") fcm_token: String,
    ): Call<LogInResponse>

    @Multipart
    @POST("register")
    fun register(
        @Query("customer_name") customer_name: String,
        @Query("phone_number") phone_number: String,
        @Query("phone_with_code") phone_with_code: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("blood_group") blood_group: String,
        @Query("gender") gender: Int,
        @Query("fcm_token") fcm_token: String,
        @Part signature: MultipartBody.Part,
        @Part("desc") desc: RequestBody,
        @Query("dob") dob: String,
        ): Call<RegistationResponse>

    @FormUrlEncoded
    @POST("forget_password")
    fun forgotPassword(
        @Field("phone_with_code") phone_with_code: String,
    ): Call<ModelForgotPass>

    @FormUrlEncoded
    @POST("add_address")
    fun addAddress(
        @Field("customer id") customer_id: Int,
        @Field("address") address: String,
        @Field("landmark") landmark: String,
        @Field("lat") lat: String,
        @Field("lng") lng: String,
    ): Call<AddAddressResponse>


    @POST("all_addresses")
    fun allAddress(
        @Query("customer id") customer_id: Int
    ): Call<AddressListResponse>

    @FormUrlEncoded
    @POST("reset_password")
    fun resetPassword(
        @Field("id") id: String,
        @Field("password") password: String,
    ): Call<ResetPassResponse>

    @FormUrlEncoded
    @POST("get_nearest_doctors")
    fun searchByLocation(
        @Field("lat") lat: String,
        @Field("lng") lng: String,
        @Field("search") search: String,
    ): Call<SearchbyLocationRes>

    @GET("get_doctor_categories")
    fun getPhotos(): Call<ModelSplic>

    @GET("doctor_all_comments")
    fun doctorAllComments(
        @Query("doctor_id") doctor_id: String
    ): Call<ModelCommentList>



    @FormUrlEncoded
    @POST("get_booking_requests")
    fun myAppointmentCancelled(
        @Field("customer_id") customer_id: String,
    ): Call<ModelCancelled>

    @FormUrlEncoded
    @POST("get_booking_requests")
    fun myAppointmentConsulted(
        @Field("customer_id") customer_id: String,
    ): Call<ModelConsultedResponse>

    @POST("get_time_slots")
    fun getTimeSlot(
        @Query("doctor_id") doctorid: String?,
        @Query("day") day: String?,
        @Query("date") date: String?,
        @Query("consultation_type") consultation_type: String?
    ): Call<ModelSlotResNew>

    @POST("get_customer_slot")
    fun getOnlineSlot(
        @Query("doctor_id") doctorid: String?,
        @Query("day") day: String?,
    ): Call<ModelSlotResNew>

    @POST("get_nearest_doctors")
    fun getAllDoctor(
        @Query("lat") lat: String?,
        @Query("lng") lng: String?,
        @Query("search") search: String?
    ): Call<ModelAllDoctorNew>

    @POST("get_nearest_doctors")
    fun getAllDoctorNew(
        @Query("lat") lat: String?,
        @Query("lng") lng: String?,
        @Query("search") search: String?
    ): Call<ModelNearestDoctor>

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
        @Query("slot_id") slot_id: String?,
        @Query("member_id") member_id: String?,
    ): Call<ModelCreateConsultation>

    @POST("get_consultation_requests")
    fun appointments(
        @Query("customer_id") customer_id: String?,
    ): Call<ModelAppointments>

    @POST("get_consultation_details")
    fun appointmentsDetails(
        @Query("id") id: String?,
    ): Call<ModelAppointmentsDetails>

    @POST("doctor_rating")
    fun rating(
        @Query("id") id: String?,
        @Query("rating") rating: String?,
        @Query("comments") comments: String?,
    ): Call<ModelRating>

    @POST("get_consultations_pat")
    fun getConsultationAccpted(
        @Query("patient_id") patient_id: String?,
        @Query("slug") slug: String?,
    ): Call<ModelUpComingNew>

    @POST("get_consultations_pat")
    fun getConsultationAccptedHome(
        @Query("patient_id") patient_id: String?,
        @Query("slug") slug: String?,
    ): Call<ModelUpComingHome>

    @POST("get_consultations_pat")
    fun getConsultation(
        @Query("patient_id") customer_id: String?,
        @Query("slug") slug: String?,
    ): Call<ModelAppointmentBySlag>

    @POST("get_patient_member_histories")
    fun memberAppointmentHistory(
        @Query("customer_id") customer_id: String?,
        @Query("member_id") member_id: String?,
    ): Call<ModelFamily>


    @Headers("Accept: application/json")
    @POST("pending_pre_list")
    fun pendingPreList(
        @Query("patient_id") customer_id: String?,
    ): Call<ModelPreList>

    @Headers("Accept: application/json")
    @POST("serch_prescription")
    fun searchPrescription(
        @Query("patient_id") customer_id: String?,
        @Query("doctor_name") doctor_name: String?,
    ): Call<ModelPreList>

    @Headers("Accept: application/json")
    @POST("serch_prescription")
    fun searchPrescribed(
        @Query("patient_id") customer_id: String?,
        @Query("doctor_name") doctor_name: String?,
    ): Call<ModelPrescribed>

    @Headers("Accept: application/json")
    @POST("pending_pre_list")
    fun therapiess(
        @Query("patient_id") patientId: String?,
    ): Call<ModelPrePending>

    //   Call<List<model>>

    // @Headers("Accept: application/json")
    @POST("pre_list")
    fun prescribedList(
        @Query("patient_id") customer_id: String?,
    ): Call<ModelPrescribed>

    @POST("search_filter_doctor")
    fun searchFilterDoctor(
        @Query("patient_id") patient_id: String?,
        @Query("customer_name") customer_name: String?,
        @Query("specialist") specialist: String?,
    ): Call<ModelPrescribed>

    @POST("get_test")
    fun getTest(
        @Query("customer_prescription") customer_prescription: String?,
    ): Call<ModelGetTest>

    @POST("get_doctors")
    fun myDoctors(
        @Query("patient_id") customer_id: String?,
    ): Call<ModelMyDoctor>

    @POST("view_prescription_pat")
    fun viewPrescriptionDetial(
        @Query("id") customer_id: String?,
    ): Call<ModelPreDetJava>

    @POST("view_prescription_pat")
    fun viewPrescriptionDetial1(
        @Query("id") customer_id: String?,
    ): Call<ModelPreDetial>

//    @Multipart
//    @POST("upload_report")
//    fun uploadImage( @Part image: MultipartBody.Part
//    ):Call<ModelUploadReport>

    @Multipart
    @POST("upload_report")
    fun uploadImage(
        @Query("id") id: String,
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody,
    ): Call<UploadResponse>

    @Multipart
    @POST("patient_test_report")
    fun UploadPatientTestReport(
        @Query("id") id: String,
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody,
    ): Call<UploadResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("create_report")
    fun uploadReport(
        @Query("patient_id") patient_id: String,
        @Query("title") tile: String,
        @Part report: MultipartBody.Part,
        @Part("desc") desc: RequestBody,
        @Query("member_id") member_id: String,
        @Query("date") date: String,
    ): Call<ModelUploadReport>

    @POST("get_lo_spc")
    fun searchByLocationAndSpec(
        @Query("address") id: String,
        @Query("specialist") specialist: String,
    ): Call<ModelScarchByLocationAndSpc>

    @GET("specialist_category")
    fun specialistCategoryTest(
    ): Call<ModelSpecilList>

    @GET("get_relation")
    fun getRelation(
    ): Call<ModelFamilyNew>

    @POST("profile_update")
    fun updateNameEmail(
        @Query("id") id: String?,
        @Query("customer_name") doctor_name: String?,
        @Query("email") email: String?,
    ): Call<ModelUpdateNameEmail>

    @POST("get_invoice_list_ptient")
    fun invoiceList(
        @Query("patient_id") id: String?,
    ): Call<ModelInvoice>

    @POST("search_doctor")
    fun searchDoctor(
        @Query("patient_id") id: String?,
        @Query("doctor_name") doctor_name: String?,
    ): Call<ModelMyDoctor>

    @POST("search_doctor_by_date")
    fun searchDoctorByDate(
        @Query("patient_id") id: String?,
        @Query("doctor_name") doctor_name: String?,
        @Query("date") date: String?,
    ): Call<ModelInvoice>

    @POST("invoice_details")
    fun invoiceDetails(
        @Query("invoice_id") id: String?,
    ): Call<ModelInvoiceDetial>

    @POST("search_appointments")
    fun searchAppointments(
        @Query("patient_id") patient_id: String?,
        @Query("doctor_name") doctor_name: String?,
        @Query("slug") slug: String?,
    ): Call<ModelUpComingNew>

    @POST("search_appointments")
    fun searchAppointmentsCompleted(
        @Query("patient_id") patient_id: String?,
        @Query("doctor_name") doctor_name: String?,
        @Query("slug") slug: String?,
    ): Call<ModelAppointmentBySlag>

    @POST("get_repo")
    fun getReport(
        @Query("patient_id") patient_id: String?,
    ): Call<ModelGetAllReport>

    @POST("get_patient_member_report_histories")
    fun reportHistory(
        @Query("customer_id") customer_id: String?,
        @Query("member_id") member_id: String?,
    ): Call<ModelGetAllReport>

    @POST("delete_report")
    fun deleteReport(
        @Query("id") patient_id: String?,
    ): Call<ModelDeleteRep>

    @POST("check_phone")
    fun checkPhone(
        @Query("phone_with_code") phone_with_code: String?,
    ): Call<ModelOTP>

    @POST("get_family")
    fun getFamilyList(
        @Query("patient_id") patient_id: String?,
    ): Call<ModelFamilyList>

    @POST("get_family")
    fun getFamilyListJava(
        @Query("patient_id") patient_id: String?,
    ): Call<ModelFamilyListJava>

    @POST("delete_family")
    fun deleteFamily(
        @Query("id") id: String?,
    ): Call<ModelDelete>
    @Multipart
    @Headers("Accept: application/json")
    @POST("add_family")
    fun addFamily(
        @Query("patient_id") patient_id: String?,
        @Query("first_name") first_name: String?,
        @Query("last_name") last_name: String?,
        @Query("dob") dob: String?,
        @Query("gender") gender: String?,
        @Query("relation") relation: String?,
        @Query("email") email: String?,
        @Query("description") description: String?,
        @Part profile_picture: MultipartBody.Part,
        @Part("desc") desc: RequestBody,
    ): Call<ModelFamilyMember>

    @POST("edit_family")
    fun editFamily(
        @Query("id") patient_id: String?,
        @Query("first_name") first_name: String?,
        @Query("last_name") last_name: String?,
        @Query("dob") dob: String?,
        @Query("gender") gender: String?,
        @Query("relation") relation: String?,
        @Query("email") email: String?,
        @Query("description") description: String?,
     ): Call<ModelFamilyMember>

    @Multipart
    @Headers("Accept: application/json")
    @POST("profile_picture")
    fun profilePicture(
        @Query("id") id: String,
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody,
    ): Call<ModelProfilePic>
}