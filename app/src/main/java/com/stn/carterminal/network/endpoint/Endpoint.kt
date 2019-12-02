package com.stn.carterminal.network.endpoint

object Endpoint {
    const val API_SIGN_IN = "login"
    const val API_GET_SESSION = "users/heartbeat"
    const val API_GET_PROVIDED_SERVICE = "provided_services/search"
    const val API_GET_PROVIDED_SERVICE_BY_ID = "provided_services/mobile/{providedServiceId}"
    const val API_GET_VEHICLE = "vehicles/mobile/search/{providedServiceId}"
    const val API_GET_VEHICLE_BY_TAG = "vehicles/mobile/get/{uhfTag}"
    const val API_GET_VEHICLE_BY_NIK = "vehicles/mobile/search"
    const val API_GET_VEHICLE_CLASS = "vehicle_classes/mobile/list"
    const val API_CHANGE_DATA_VEHICLE_POSITION = "vehicles/mobile/change_vehicle_position/{vehicleId}"
    const val API_SEARCH_PROVIDED_SERVICE_WITH_EXCLUDED_ID = "provided_services/mobile/search/exclude/{providedServiceId}"
    const val API_CHANGE_DATA_MANIFEST = "vehicles/mobile/change_data_manifest/{vehicleId}/to/{providedServiceId}"
    const val API_CHANGE_UHF_TAG = "vehicles/mobile/change_uhf_tag/{vehicleId}"
    const val API_ADD_NEW_VEHICLE = "vehicles/mobile"
    const val API_CHECK_UHF_TAG_VEHICLE = "vehicles/mobile/check_uhf_tag/{uhfTag}"
    const val API_GET_ALL_WHOLE_VEHICLE_ITS_TAG_IS_USED = "vehicles/mobile/search_used_tag_vehicle"
    const val API_CHECK_PROVIDED_SERVICE_CONFIRMATION_STATUS = "vehicles/mobile/check_provided_confirmation_status/{uhfTag}"
    const val API_SEARCH_PROVIDED_SERVICE_CONFIRMATION_STATUS_NOT_APPROVED = "vehicles/mobile/search_provided_service_confirmation_status_not_approved"
}