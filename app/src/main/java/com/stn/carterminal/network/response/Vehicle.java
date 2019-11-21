package com.stn.carterminal.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vehicle implements Serializable {
    @SerializedName("vehicleId")
    private Long vehicleId;

    @SerializedName("vehicleIdNumber")
    private String NIK;

    @SerializedName("description")
    private String description;

    @SerializedName("vehicleClass")
    private String vehicleClass;

    @SerializedName("entryDate")
    private String entryDate;

    @SerializedName("numDaysBuildUp")
    private Long numDaysBuildUp;

    @SerializedName("providedServiceNumber")
    private String providedServiceNumber;

    @SerializedName("providedServiceId")
    private Long providedServiceId;

    @SerializedName("vehicleClassId")
    private Long vehicleClassId;

    @SerializedName("vehicleManifestStatus")
    private String vehicleManifestStatus;

    private boolean isDataVehicleChanged = false;
}
