package com.example.ehcf.Pharmacy.model

data class ResultXXX(
    val cust_id: Int,
    val customer_name: String,
    val order_id: Int,
    val payment_name: String,
    val product_names: ArrayList<String>,
    val test_names: ArrayList<String>,
    val status: String,
    val totalCoast: Int
)