package com.example.ehcf.Prescription.model

data class ModelPreList(
    val message: String,
    val result: ArrayList<ResultPrePending>,
    val status: Int
)