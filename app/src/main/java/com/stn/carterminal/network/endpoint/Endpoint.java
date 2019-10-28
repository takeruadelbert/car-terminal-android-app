package com.stn.carterminal.network.endpoint;

public class Endpoint {
    public static final String API_SIGN_IN = "/login";
    public static final String API_GET_SESSION = "/users/heartbeat";
    public static final String API_GET_PROVIDED_SERVICE = "/provided_services/search";
    public static final String API_GET_VEHICLE = "/vehicles/mobile/search/{providedServiceId}";
    public static final String API_CHANGE_DATA_VEHICLE_POSITION = "/vehicles/mobile/change_vehicle_position/{vehicleId}";
}
