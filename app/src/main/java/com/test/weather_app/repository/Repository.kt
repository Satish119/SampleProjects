package com.test.weather_app.repository

import com.test.weather_app.api.RetrofitInstance
import com.test.weather_app.model.ForecastModel
import com.test.weather_app.model.WeatherCities
import com.test.weather_app.model.WeatherModel
import retrofit2.Response

class Repository {
    suspend fun getWeather(
        lat: Double,
        lon: Double,
        units:String,
        weatherAPIKey: String
    ): Response<WeatherModel> {
        return RetrofitInstance.weatherApi.getWeatherInfo(lat, lon, units,weatherAPIKey)
    }

    suspend fun getListOfWeather(
        ids:String,
        units:String,
        weatherAPIKey: String
    ): Response<WeatherCities> {
        return RetrofitInstance.weatherApi.getListOfWeatherInfo(ids, units,weatherAPIKey)
    }

    suspend fun getWeatherForecast(
        cityName:String,
        units:String,
        weatherAPIKey: String
    ): Response<ForecastModel> {
        return RetrofitInstance.weatherApi.getWeatherForecast(cityName, units,weatherAPIKey)
    }
}