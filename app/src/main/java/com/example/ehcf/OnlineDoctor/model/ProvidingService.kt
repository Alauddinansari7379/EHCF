package com.example.ehcf.OnlineDoctor.model

data class ProvidingService(
    val created_at: String,
    val id: String,
    val specialist_id: String,
    val status: String,
    val symptom_image: String,
    val symptom_name: String,
    val updated_at: String
)