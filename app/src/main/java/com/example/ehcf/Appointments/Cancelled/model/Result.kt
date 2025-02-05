package com.example.ehcf.Appointments.Cancelled.model

data class Result(
    val cancelled: List<Cancelled>,
    val completed: List<Any>,
    val upcoming: List<Upcoming>
)