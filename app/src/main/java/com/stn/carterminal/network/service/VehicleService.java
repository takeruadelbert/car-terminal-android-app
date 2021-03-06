package com.stn.carterminal.network.service;

import com.stn.carterminal.network.endpoint.Endpoint;
import com.stn.carterminal.network.request.ChangeUhfTag;
import com.stn.carterminal.network.request.ChangeVehiclePosition;
import com.stn.carterminal.network.request.NewVehicle;
import com.stn.carterminal.network.response.Status;
import com.stn.carterminal.network.response.UhfTag;
import com.stn.carterminal.network.response.Vehicle;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VehicleService {

    @GET(Endpoint.API_GET_VEHICLE)
    Call<ArrayList<Vehicle>> apiGetVehicle(@Path("providedServiceId") Long providedServiceId, @Query("keyword") String keyword);

    @PUT(Endpoint.API_CHANGE_DATA_VEHICLE_POSITION)
    Call<Vehicle> apiChangeVehiclePosition(@Path("vehicleId") Long vehicleId, @Body ChangeVehiclePosition changeVehiclePosition);

    @GET(Endpoint.API_GET_VEHICLE_BY_TAG)
    Call<Vehicle> apiGetVehicleByTag(@Path("uhfTag") String uhfTag);

    @GET(Endpoint.API_GET_VEHICLE_BY_NIK)
    Call<ArrayList<Vehicle>> apiGetVehicleByNIK(@Query("keyword") String keyword);

    @PUT(Endpoint.API_CHANGE_UHF_TAG)
    Call<Vehicle> apiChangeUHFTag(@Path("vehicleId") Long vehicleId, @Body ChangeUhfTag changeUhfTag);

    @POST(Endpoint.API_ADD_NEW_VEHICLE)
    Call<Vehicle> apiAddNewVehicle(@Body NewVehicle newVehicle);

    @GET(Endpoint.API_GET_VEHICLE_CLASS)
    Call<Map<Long, String>> apiGetVehicleClass();

    @GET(Endpoint.API_CHECK_UHF_TAG_VEHICLE)
    Call<UhfTag> apiCheckUhfTag(@Path("uhfTag") String uhfTag);

    @GET(Endpoint.API_GET_ALL_WHOLE_VEHICLE_ITS_TAG_IS_USED)
    Call<ArrayList<Vehicle>> apiGetAllVehicleItsTagIsUsed(@Query("keyword") String keyword);

    @GET(Endpoint.API_CHECK_PROVIDED_SERVICE_CONFIRMATION_STATUS)
    Call<Status> apiCheckProvidedConfirmationStatusByUhfTag(@Path("uhfTag") String uhfTag);

    @GET(Endpoint.API_SEARCH_PROVIDED_SERVICE_CONFIRMATION_STATUS_NOT_APPROVED)
    Call<ArrayList<Vehicle>> apiSearchProvidedServiceConfirmationStatusNotApproved(@Query("keyword") String keyword);
}
