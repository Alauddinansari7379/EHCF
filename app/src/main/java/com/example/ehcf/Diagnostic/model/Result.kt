package com.example.ehcf.Diagnostic.model

data class Result(
    val Test_Code: String,
    val Test_Name: String,
    val product_number: String,
    val created_at: String,
    val dos: String,
    val id: Int,
    val status: Int,
    val updated_at: String
)