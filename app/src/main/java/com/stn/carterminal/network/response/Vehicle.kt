package com.stn.carterminal.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Vehicle(
        @SerializedName("vehicleId") var vehicleId: Long,
        @SerializedName("vehicleIdNumber") var NIK: String,
        @SerializedName("description") var description: String,
        @SerializedName("vehicleClass") var vehicleClass: String,
        @SerializedName("entryDate") var entryDate: String,
        @SerializedName("numDaysBuildUp") var numDaysBuildUp: Number,
        @SerializedName("providedServiceNumber") var providedServiceNumber: String,
        @SerializedName("providedServiceId") var providedServiceId: Long,
        @SerializedName("vehicleClassId") var vehicleClassId: Long,
        @SerializedName("vehicleManifestStatus") var vehicleManifestStatus: String
) : Serializable {
    var isDataVehicleChanged: Boolean = false
}