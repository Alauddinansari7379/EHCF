package com.example.ehcf.Pharmacy.model

data class Cart(
    val created_at: String,
    val customer_id: String,
    val id: Int,
    val product_id: String,
    val quantity: String,
    val updated_at: String
)