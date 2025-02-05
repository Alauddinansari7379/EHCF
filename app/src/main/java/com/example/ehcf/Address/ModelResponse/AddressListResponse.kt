package com.example.ehcf.Address.ModelResponse

data class AddressListResponse(
    val message: String,
    val result: List<Result>,
    val status: Int
)