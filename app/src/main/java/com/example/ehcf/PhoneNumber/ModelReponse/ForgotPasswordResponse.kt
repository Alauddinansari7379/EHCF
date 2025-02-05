package com.example.ehcf.PhoneNumber.ModelReponse

data class ForgotPasswordResponse(
    val message: String,
    val result: ResultKt,
    val status: Int
)