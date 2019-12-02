package com.stn.carterminal.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Biodata(
        @SerializedName("id") var biodataId: Long,
        @SerializedName("firstName") var firstName: String,
        @SerializedName("lastName") var lastName: String,
        @SerializedName("fullname") var fullName: String
) : Serializable