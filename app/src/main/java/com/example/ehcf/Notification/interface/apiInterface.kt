package com.example.ehcf.Notification.Interface

import com.example.ehcf.Dashboard.modelResponse.ModelSpecilList
import com.example.ehcf.Fragment.test.UploadResponse
import com.example.ehcf.Specialities.model.ModelSplic
import com.example.ehcf.Notification.Model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface apiInterface {

    //https://jsonplaceholder.typicode.com/users
    //https://ehcf.in/api/customer/register
    @GET("users")
    fun getUser(): Call<List<User>>

//    @GET("photos")
//    fun getPhotos(): Call<List<ModelPhotos>>

    @GET("get_doctor_categories")
    fun getPhotos(): Call<ModelSplic>
    @GET("specialist_category")
    fun specialistCategory(): Call<ModelSpecilList>

//    @POST("register")
//    fun getRegister(): Call<ModelRegisteration>

    @Multipart
    @POST("upload_report")
    fun uploadImage(
        @Query("id") id: String,
        @Part image: MultipartBody.Part,
        create: RequestBody,
    ): Call<UploadResponse>

}
