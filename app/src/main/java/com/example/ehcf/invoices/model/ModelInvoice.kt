package com.example.ehcf_doctor.Invoice.model

data class ModelInvoice(
    val message: String,
    val result: ArrayList<Result>,
    val status: Int
)