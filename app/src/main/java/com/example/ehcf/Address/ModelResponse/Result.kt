package com.example.ehcf.Address.ModelResponse

data class Result(
    val address: String,
    val created_at: String,
    val customer_id: String,
    val customer_name: String,
    val id: String,
    val landmark: String,
    val lat: String,
    val lng: String,
    val status: String,
    val updated_at: String
)