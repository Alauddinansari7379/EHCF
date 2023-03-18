package com.example.ehcf.Prescription.model

data class ModelPrePending(
    val message: String,
    val result: List<Result>,
    val status: Int
)