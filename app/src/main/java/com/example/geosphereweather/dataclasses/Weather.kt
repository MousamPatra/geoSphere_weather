package com.example.geosphereweather.dataclasses

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)