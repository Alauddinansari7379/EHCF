package com.example.ehcf.Upload.model

data class Result(
    val created_at: String,
    val id: Int,
    val patient_id: String,
    val report: String,
    val updated_at: String,
    val ayusynk_report: String
)