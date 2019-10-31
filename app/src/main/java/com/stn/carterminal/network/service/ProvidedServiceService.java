package com.stn.carterminal.network.service;

import com.stn.carterminal.network.endpoint.Endpoint;
import com.stn.carterminal.network.response.ProvidedService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.OPTIONS;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProvidedServiceService {

    @GET(Endpoint.API_GET_PROVIDED_SERVICE)
    Call<ArrayList<ProvidedService>> apiGetProvidedService(@Query("keyword") String keyword);

    @GET(Endpoint.API_GET_PROVIDED_SERVICE_BY_ID)
    Call<ProvidedService> apiGetProvidedServiceById(@Path("providedServiceId") Long providedServiceId);

    @GET(Endpoint.API_SEARCH_PROVIDED_SERVICE_WITH_EXCLUDED_ID)
    Call<ArrayList<ProvidedService>> apiSearchProvidedServiceWithExcludedId(@Path("providedServiceId") Long providedServiceId, @Query("keyword") String keyword);

    @PUT(Endpoint.API_CHANGE_DATA_MANIFEST)
    Call<ProvidedService> apiChangeDataManifest(@Path("vehicleId") Long vehicleId, @Path("providedServiceId") Long providedServiceId);
}
