package com.example.geosphereweather

import com.example.geosphereweather.constants.AppConstants
import com.example.geosphereweather.dataclasses.GeoSphereWeather
import com.example.geosphereweather.delegates.ApiDelegate
import com.example.geosphereweather.delegates.MainDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainController(
    private val delegate: MainDelegate) {

    //retrieve the date from open weather using api call
    fun retrieveWeatherDetails(cityName: String){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AppConstants.API_BASE_URL)
            .build().create(ApiDelegate::class.java)

        val response = retrofit.getWeatherData(cityName, AppConstants.API_KEY, "metric")
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
            }
        })

    }
}