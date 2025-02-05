package com.example.ehcf.OnlineDoctor.model

data class ResultX(
    val consultation_type: String,
    val created_at: String,
    val date: String,
    val doctor_id: String,
    val id: Int,
    val patient_id: String,
    val payment_mode: String,
    val status: Int,
    val time: String,
    val total: String,
    val updated_at: String
)