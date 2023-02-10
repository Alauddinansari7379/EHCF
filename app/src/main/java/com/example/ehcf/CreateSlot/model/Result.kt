package com.example.ehcf.CreateSlot.model

data class Result(
    val created_at: String,
    val description: String,
    val doctor_id: String,
    val id: Int,
    val patient_id: String,
    val payment_mode: String,
    val start_time: String,
    val status: Int,
    val title: String,
    val total_amount: String,
    val updated_at: String
)