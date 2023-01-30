package com.example.ehcf.Testing.Interface

import com.example.ehcf.Specialities.model.ModelSplic
import com.example.ehcf.Testing.Model.User
import retrofit2.Call
import retrofit2.http.GET

interface apiInterface {

    //https://jsonplaceholder.typicode.com/users
    //https://ehcf.thedemostore.in/api/customer/register
    @GET("users")
    fun getUser(): Call<List<User>>

//    @GET("photos")
//    fun getPhotos(): Call<List<ModelPhotos>>

    @GET("get_doctor_categories")
    fun getPhotos(): Call<ModelSplic>

//    @POST("register")
//    fun getRegister(): Call<ModelRegisteration>

}
