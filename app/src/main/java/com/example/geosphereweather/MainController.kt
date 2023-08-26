package com.example.geosphereweather

import com.example.geosphereweather.dataclasses.GeoSphereWeather
import com.example.geosphereweather.delegate.ApiDelegate
import com.example.geosphereweather.delegate.MainDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainController(
    private val delegate: MainDelegate) {

    fun retrieveWeatherDetails(cityName: String){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiDelegate::class.java)

        val response = retrofit.getWeatherData(cityName, "933ca73bf89c355b946c16ee5b239d78", "metric")
        response.enqueue(object : Callback<GeoSphereWeather> {
            override fun onResponse(
                call: Call<GeoSphereWeather>,
                response: Response<GeoSphereWeather>,
            ) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){
                    delegate.setAllUIElements(responseBody)

//                    val conditions = responseBody.weather.firstOrNull()?.main?: "Unknown"
//                    delegate.alterImageForWeatherConditions(conditions)
                }
            }

            override fun onFailure(call: Call<GeoSphereWeather>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
}