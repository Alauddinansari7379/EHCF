package com.example.ehcf.Prescription.model

data class ResultX(
    val assessment: String,
    val booking_id: String,
    val created_at: String,
    val date: String,
    val doctor_id: String,
    val doctor_notes: String,
    val id: String,
    val is_test: String,
    val objective_information: String,
    val patient_id: String,
    val plan: String,
    val doctor_name: String?=null,
    val start_time: String?=null,
    val end_time: String?=null,
    val category_name: String,
    val report: String?=null,
    val subjective_information: String,
    val updated_at: String
)