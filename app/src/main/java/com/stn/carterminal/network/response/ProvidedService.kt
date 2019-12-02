package com.stn.carterminal.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProvidedService(
        @SerializedName("providedServiceId") var providedServiceId: Long,
        @SerializedName("providedServiceNumber") var providedServiceNumber: String,
        @SerializedName("vesselName") var vesselName: String,
        @SerializedName("vesselOwner") var vesselOwner: String,
        @SerializedName("companyName") var companyName: String
) : Serializable