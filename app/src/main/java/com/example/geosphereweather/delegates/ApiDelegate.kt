package com.example.geosphereweather.delegates

import com.example.geosphereweather.dataclasses.GeoSphereWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiDelegate {

    @GET("weather")
    fun getWeatherData(
        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units") units:String
    ): Call<GeoSphereWeather>
}