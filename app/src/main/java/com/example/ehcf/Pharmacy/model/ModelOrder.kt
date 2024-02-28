package com.example.ehcf.Pharmacy.model

data class ModelOrder(
    val message: String,
    val order_id: OrderId,
    val status: Int
)