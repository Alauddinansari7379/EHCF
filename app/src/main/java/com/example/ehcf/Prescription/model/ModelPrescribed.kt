package com.example.ehcf.Prescription.model

data class ModelPrescribed(
    val message: String,
    val result: List<ResultX>,
    val status: Int
)