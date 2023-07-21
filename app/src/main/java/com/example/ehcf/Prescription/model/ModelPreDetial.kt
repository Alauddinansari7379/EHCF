package com.example.ehcf.Prescription.model

data class ModelPreDetial(
    val diagnosis: List<Diagnosi>,
    val doctor_notes: List<DoctorNote>,
    val medicine: List<Medicine>,
    val message: String,
    val status: Int
)