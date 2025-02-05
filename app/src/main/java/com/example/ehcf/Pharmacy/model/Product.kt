package com.example.ehcf.Pharmacy.model

data class Product(
    val category_id: Int,
    val created_at: String,
    val description: String,
    val discount: String,
    val id: Int,
    val image: String,
    val marked_price: Double,
    val price: Double,
    val product_name: String,
    val slug: Any,
    val status: Int,
    val sub_category_id: Int,
    val unit_id: Int,
    val updated_at: String,
    val vendor_id: Int
)