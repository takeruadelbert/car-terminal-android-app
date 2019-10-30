package com.stn.carterminal.network.service;

import com.stn.carterminal.network.endpoint.Endpoint;
import com.stn.carterminal.network.request.ChangeVehiclePosition;
import com.stn.carterminal.network.response.Vehicle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.OPTIONS;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VehicleService {

    @OPTIONS(Endpoint.API_GET_VEHICLE)
    Call<ArrayList<Vehicle>> apiGetVehicle(@Path("providedServiceId") Long providedServiceId, @Query("keyword") String keyword);

    @PUT(Endpoint.API_CHANGE_DATA_VEHICLE_POSITION)
    Call<Vehicle> apiChangeVehiclePosition(@Path("vehicleId") Long vehicleId, @Body ChangeVehiclePosition changeVehiclePosition);

    @GET(Endpoint.API_GET_VEHICLE_BY_TAG)
    Call<Vehicle> apiGetVehicleByTag(@Path("uhfTag") String uhfTag);

    @OPTIONS(Endpoint.API_GET_VEHICLE_BY_NIK)
    Call<ArrayList<Vehicle>> apiGetVehicleByNIK(@Query("keyword") String keyword);
}
