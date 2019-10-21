package com.stn.carterminal.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProvidedService implements Serializable {
    @SerializedName("providedServiceId")
    private Long providedServiceId;

    @SerializedName("providedServiceNumber")
    private String providedServiceNumber;

    @SerializedName("vesselName")
    private String vesselName;

    @SerializedName("vesselOwner")
    private String vesselOwner;

    @SerializedName("companyName")
    private String companyName;
}
