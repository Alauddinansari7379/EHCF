package com.example.ehcf.Prescription.model

data class Result1(
    val assessment: String,
    val booking_id: String,
    val category_name: String,
    val created_at: String,
    val date: String,
    val doctor_id: String,
    val doctor_name: String,
    val doctor_notes: String,
    val end_time: String?=null,
    val id: String,
    val is_test: String,
    val objective_information: String,
    val patient_id: String,
    val phone_number: String,
    val plan: String,
    val profile_image: String,
    val report: String,
    val specialist: String,
    val start_time: String?=null,
    val subjective_information: String,
    val updated_at: String
)