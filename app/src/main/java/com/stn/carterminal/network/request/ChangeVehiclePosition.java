package com.stn.carterminal.network.request;

import lombok.Data;

@Data
public class ChangeVehiclePosition {
    private Long id;
    private final String vehiclePosition = "TERMINAL_IN";

    public ChangeVehiclePosition(Long id) {
        this.id = id;
    }
}
