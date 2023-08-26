package com.example.geosphereweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geosphereweather.databinding.ActivityMainBinding
import com.example.geosphereweather.dataclasses.GeoSphereWeather
import com.example.geosphereweather.delegate.MainDelegate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

class MainActivity : AppCompatActivity(), MainDelegate {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var controller: MainController
    private var isInCelsius = true
    private var cityName = "kolkata"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        controller = MainController(this)
        controller.retrieveWeatherDetails(cityName)
        getSearchCity()

        binding.temp2.setOnClickListener {
            if(isInCelsius){
                isInCelsius = false
                controller.retrieveWeatherDetails(cityName)
            }else{
                isInCelsius = true
                controller.retrieveWeatherDetails(cityName)
            }
        }
    }

    private fun getSearchCity(){
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    cityName = query
                    controller.retrieveWeatherDetails(cityName)
                }
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    override fun setAllUIElements(responseBody: GeoSphereWeather){
        val cityName = responseBody.name
        val temperature = if(isInCelsius) "${responseBody.main.temp}" else celsiusToFahrenheit(responseBody.main.temp)
        val tempUnit1 = if(isInCelsius) "°C" else "°F"
        val tempUnit2 = if(isInCelsius) "°F" else "°C"
        val maxTemp = if(isInCelsius) "${responseBody.main.temp_max}" else celsiusToFahrenheit(responseBody.main.temp_max)
        val minTemp = if(isInCelsius) "${responseBody.main.temp_min}" else celsiusToFahrenheit(responseBody.main.temp_min)
        val humidity = responseBody.main.humidity
        val windSpeed = responseBody.wind.speed * 3.6
        val sunrise = responseBody.sys.sunrise.toLong()
        val sunset = responseBody.sys.sunset.toLong()
        val airPressure = responseBody.main.pressure
        val conditions = responseBody.weather.firstOrNull()?.main?: "Unknown"

        //Log.d("temp", "onResponse: $temperature")
        binding.searchedCityTextView.text = cityName
        binding.tempTextView.text = "$temperature"
        binding.temp1.text = tempUnit1
        binding.temp2.text = tempUnit2
        binding.maxTempTextView.text = "Max Temp: $maxTemp °C"
        binding.minTempTextView.text = "Min Temp: $minTemp °C"
        binding.humidityTextView.text = "$humidity %"
        binding.windSpeedTextView.text = "${ceil(windSpeed * 10) / 10} km/hr"
        binding.sunriseTextView.text = "${getFormattedTime(sunrise)}"
        binding.sunsetTextView.text = "${getFormattedTime(sunset)}"
        binding.airPressureTextView.text = "$airPressure hPa"
        binding.weatherTextView.text = conditions
        binding.conditionsTextView.text = conditions
        binding.dayTextView.text = getDayName()
        binding.dateTextView.text = getCurrentDate()

        alterImageForWeatherConditions(conditions)
    }

    override fun alterImageForWeatherConditions(conditions: String){
        when(conditions){
            "Clear Sky", "Sunny", "Clear" ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)

            }
            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy", "Haze"->{
                binding.root.setBackgroundResource(R.drawable.cloud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)

            }
            "Rain","Light Rain", "Drizzle" , "Moderate Rain", "Heavy Rain", "Showers"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)

            }
            "Snow","Blizzard" , " Light Snow" , "Moderate Snow", "Heavy Snow" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)

            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)

            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun getCurrentDate(): String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getDayName(): String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getFormattedTime(timestamp: Long): String{
        val sdf = SimpleDateFormat("HH: mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))
    }

    private fun celsiusToFahrenheit(celsius: Double): String {
        val fahrenheit = celsius * 9 / 5 + 32
        return "%.2f".format(fahrenheit)
    }

}