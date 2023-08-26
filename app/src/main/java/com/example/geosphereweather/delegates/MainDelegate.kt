package com.example.geosphereweather.delegates

import com.example.geosphereweather.dataclasses.GeoSphereWeather

interface MainDelegate {

    fun setAllUIElements(responseBody: GeoSphereWeather)
    fun alterImageForWeatherConditions(conditions: String)

}