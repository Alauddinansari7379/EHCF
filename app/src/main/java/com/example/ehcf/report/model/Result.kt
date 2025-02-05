package com.example.ehcf.report.model

data class Result(
    val after: String,
    val created_at: String,
    val instructions: String,
    val prescriptionid: String,
    val id: String?,
    val test_name: String,
    val test_report: String?
)