package com.example.ehcf.Pharmacy.model

data class OrderId(
    val created_at: String,
    val id: Int,
    val price: String,
    val product_id: Int,
    val qty: String,
    val updated_at: String
)