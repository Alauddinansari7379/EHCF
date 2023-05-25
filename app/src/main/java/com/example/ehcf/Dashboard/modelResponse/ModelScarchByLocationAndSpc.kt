package com.example.ehcf.Dashboard.modelResponse

data class ModelScarchByLocationAndSpc(
    val message: String,
    val result: List<ResultX>,
    val status: Int
)