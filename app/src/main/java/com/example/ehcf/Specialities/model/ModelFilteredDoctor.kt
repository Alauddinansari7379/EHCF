package com.example.ehcf.Specialities.model

data class ModelFilteredDoctor(
    val message: String,
    val result: List<ResultFilter>,
    val status: Int
)