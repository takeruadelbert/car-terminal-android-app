package com.stn.carterminal.network.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewVehicle {
    private String vehicleIdNumber;
    private String description;
    private Long vehicleClassId;
    private Long providedServiceId;
}
