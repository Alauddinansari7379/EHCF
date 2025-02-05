package com.example.ehcf.Profile.modelResponse

data class Result(
    val blood_group: String,
    val customer_name: String,
    val email: String,
    val gender: String,
    val id: Int,
    val phone_number: String,
    val pre_existing_desease: Any,
    val profile_picture: String,
    val status: String
)