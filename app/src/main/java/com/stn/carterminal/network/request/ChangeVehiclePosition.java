package com.stn.carterminal.network.request;

import lombok.Data;

@Data
public class ChangeVehiclePosition {
    private Long id;
    private final String vehiclePosition = "TERMINAL_IN";
    private String uhfTag;

    public ChangeVehiclePosition(Long id, String uhfTag) {
        this.id = id;
        this.uhfTag = uhfTag;
    }
}
