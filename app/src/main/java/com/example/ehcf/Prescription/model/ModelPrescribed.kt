package com.example.ehcf.Prescription.model

data class ModelPrescribed(
    val message: String,
    val result: ArrayList<ResultPrescribed>,
    val status: Int
)