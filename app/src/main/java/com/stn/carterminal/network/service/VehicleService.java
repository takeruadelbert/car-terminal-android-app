package com.stn.carterminal.network.service;

import com.stn.carterminal.network.endpoint.Endpoint;
import com.stn.carterminal.network.response.Vehicle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.OPTIONS;
import retrofit2.http.Query;

public interface VehicleService {

    @OPTIONS(Endpoint.API_GET_VEHICLE)
    Call<ArrayList<Vehicle>> apiGetVehicle(@Query("keyword") String keyword);
}
