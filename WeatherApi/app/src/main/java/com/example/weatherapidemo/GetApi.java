package com.example.weatherapidemo;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetApi {
    @GET("F-C0032-001")
    Observable<WeatherResponse> getWeatherApi(
            @Query("Authorization") String Authorization,
            @Query("locationName") String locationName,
            @Query("elementName") String elementName
    );
}
