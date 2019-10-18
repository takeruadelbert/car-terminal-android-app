package com.stn.carterminal.network;

import android.content.Context;

import com.stn.carterminal.BuildConfig;
import com.stn.carterminal.activity.SignInActivity;
import com.stn.carterminal.constant.sharedPreference.SharedPreferenceDataKey;
import com.stn.carterminal.helper.SharedPreferencesHelper;
import com.stn.carterminal.network.endpoint.Endpoint;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final Integer TIMEOUT = 5; // in second(s)

    private static OkHttpClient.Builder builder() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        return okhttpBuilder;
    }

    private static HttpLoggingInterceptor interceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    public static <S> S createBaseService(Context context, Class<S> serviceClass) {
        OkHttpClient.Builder builder = builder();

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor());
        }

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request newReq = request.newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization", SharedPreferencesHelper.getData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_TOKEN_BEARER))
                        .build();
                return chain.proceed(newReq);
            }
        });

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Endpoint.HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }
}
