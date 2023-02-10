package com.example.ehcf.Dashboard.modelResponse

data class Result(
    val doctor_list: List<Doctor>,
    val recommended: List<Any>
)