package com.example.ehcf.report.model

data class UploadResponseModelUploadReport(
    val message:String,
    val data:List<DataItem>
)


data class DataItem(val url:String, val id:String)