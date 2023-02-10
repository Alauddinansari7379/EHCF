package com.example.ehcf.CreateSlot.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class ModelSlotRes {
    @SerializedName("result")
    @Expose
    var result: List<Result>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    inner class Result {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null
    }
}