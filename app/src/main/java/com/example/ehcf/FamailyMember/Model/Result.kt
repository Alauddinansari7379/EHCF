package com.example.ehcf.FamailyMember.Model

data class Result(
    val created_at: String,
    val dob: String,
    val email: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val patient_id: String,
    val relation: String,
    val updated_at: String
)