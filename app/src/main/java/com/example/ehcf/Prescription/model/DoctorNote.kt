package com.example.ehcf.Prescription.model

data class DoctorNote(
    val address: String,
    val assessment: String,
    val booking_id: String,
    val city: String,
    val clinic_name: String,
    val country: String,
    val created_at: String,
    val date: String,
    val doctor_name: String,
    val doctor_notes: String,
    val follow_up: Any,
    val gender: String,
    val id: String,
    val instructions: String,
    val objective_information: String,
    val plan: String,
    val registration: String,
    val report: Any,
    val subjective_information: String,
    val test_name: String
)