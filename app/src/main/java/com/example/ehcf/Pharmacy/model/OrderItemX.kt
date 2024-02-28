package com.example.ehcf.Pharmacy.model

data class OrderItemX(
    val created_at: String,
    val cust_id: Int,
    val customer_name: String,
    val description: String,
    val id: Int,
    val image: String,
    val order_id: String,
    val price: Int,
    val product_id: Int,
    val product_name: String,
    val qty: Int,
    val status: String,
    val unit: String,
    val updated_at: String
)