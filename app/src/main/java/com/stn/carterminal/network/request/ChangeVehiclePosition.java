package com.stn.carterminal.network.request;

import lombok.Data;

@Data
public class ChangeVehiclePosition {
    private Long id;
    private final String vehiclePosition = "TERMINAL_IN";
    private String uhfTag;
    private String vehicleIdNumber;
    private String description;
    private Long vehicleClassId;

    public ChangeVehiclePosition(Long id, String uhfTag, String vehicleIdNumber, String description, Long vehicleClassId) {
        this.id = id;
        this.uhfTag = uhfTag;
        this.vehicleIdNumber = vehicleIdNumber;
        this.description = description;
        this.vehicleClassId = vehicleClassId;
    }
}
