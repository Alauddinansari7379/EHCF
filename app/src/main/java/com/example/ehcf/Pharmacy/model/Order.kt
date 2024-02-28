package com.example.ehcf.Pharmacy.model

data class Order(
    val payment_name: String,
    val status: String,
    val totalCoast: Int
)