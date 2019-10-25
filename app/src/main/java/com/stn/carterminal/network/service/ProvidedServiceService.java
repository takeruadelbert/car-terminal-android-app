package com.stn.carterminal.network.service;

        import com.stn.carterminal.network.endpoint.Endpoint;
        import com.stn.carterminal.network.response.ProvidedService;

        import java.util.ArrayList;

        import retrofit2.Call;
        import retrofit2.http.OPTIONS;
        import retrofit2.http.Query;

public interface ProvidedServiceService {

    @OPTIONS(Endpoint.API_GET_PROVIDED_SERVICE)
    Call<ArrayList<ProvidedService>> apiGetProvidedService(@Query("keyword") String keyword);
}
