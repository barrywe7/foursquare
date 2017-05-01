package com.barryirvine.foursquare.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FourSquareService {

    private static volatile FoursquareAPI sFoursquareAPI;

    public static FoursquareAPI get() {
        if (sFoursquareAPI == null) {
            synchronized (FourSquareService.class) {
                if (sFoursquareAPI == null) {
                    final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                    sFoursquareAPI = new Retrofit.Builder()
                            .baseUrl(FoursquareAPI.BASE_URL)
                            .client(client)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
                            .build()
                            .create(FoursquareAPI.class);


                }
            }
        }
        return sFoursquareAPI;
    }
}
