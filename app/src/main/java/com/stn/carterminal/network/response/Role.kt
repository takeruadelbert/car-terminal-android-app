package com.stn.carterminal.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Role(
        @SerializedName("id") var id: Long,
        @SerializedName("label") var label: String,
        @SerializedName("name") var name: String
) : Serializable