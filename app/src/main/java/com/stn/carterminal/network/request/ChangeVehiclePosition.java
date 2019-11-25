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
    private String vehicleManifestStatus;
    private boolean isDataVehicleChanged;

    public ChangeVehiclePosition(Long id, String uhfTag, String vehicleIdNumber, String description, Long vehicleClassId, String vehicleManifestStatus) {
        this.id = id;
        this.uhfTag = uhfTag;
        this.vehicleIdNumber = vehicleIdNumber;
        this.description = description;
        this.vehicleClassId = vehicleClassId;
        this.vehicleManifestStatus = vehicleManifestStatus;
        this.isDataVehicleChanged = setDataVehicleChanged();
    }

    private boolean setDataVehicleChanged() {
        return vehicleManifestStatus.equals("MATCH");
    }
}
