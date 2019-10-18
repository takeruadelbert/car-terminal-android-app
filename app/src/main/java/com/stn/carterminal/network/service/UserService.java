package com.stn.carterminal.network.service;

import com.stn.carterminal.network.endpoint.Endpoint;
import com.stn.carterminal.network.request.Login;
import com.stn.carterminal.network.response.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @POST(Endpoint.API_SIGN_IN)
    Call<ResponseBody> apiSignIn(@Body Login login);

    @GET(Endpoint.API_GET_SESSION)
    Call<User> apiGetDataSession();
}
