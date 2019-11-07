package com.stn.carterminal.network.endpoint;

public class Endpoint {
    public static final String API_SIGN_IN = "login";
    public static final String API_GET_SESSION = "users/heartbeat";
    public static final String API_GET_PROVIDED_SERVICE = "provided_services/search";
    public static final String API_GET_PROVIDED_SERVICE_BY_ID = "provided_services/mobile/{providedServiceId}";
    public static final String API_GET_VEHICLE = "vehicles/mobile/search/{providedServiceId}";
    public static final String API_GET_VEHICLE_BY_TAG = "vehicles/mobile/get/{uhfTag}";
    public static final String API_GET_VEHICLE_BY_NIK = "vehicles/simplesearch";
    public static final String API_GET_VEHICLE_CLASS = "vehicle_classes";
    public static final String API_CHANGE_DATA_VEHICLE_POSITION = "vehicles/mobile/change_vehicle_position/{vehicleId}";
    public static final String API_SEARCH_PROVIDED_SERVICE_WITH_EXCLUDED_ID = "provided_services/mobile/search/exclude/{providedServiceId}";
    public static final String API_CHANGE_DATA_MANIFEST = "vehicles/mobile/change_data_manifest/{vehicleId}/to/{providedServiceId}";
    public static final String API_CHANGE_UHF_TAG = "vehicles/mobile/change_uhf_tag/{vehicleId}";
    public static final String API_ADD_NEW_VEHICLE = "vehicles/mobile";
    public static final String API_CHECK_UHF_TAG_VEHICLE = "vehicles/mobile/check_uhf_tag/{uhfTag}";
}
