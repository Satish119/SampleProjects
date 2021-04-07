package com.test.weather_app.api

import com.test.weather_app.model.ForecastModel
import com.test.weather_app.model.WeatherCities
import com.test.weather_app.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherInfo(
        @Query("lat") latitude:Double,
        @Query("lon") longitude:Double,
        @Query("units") units:String?,
        @Query("appid") apiKey:String
    ):Response<WeatherModel>

    @GET("group")
    suspend fun getListOfWeatherInfo(
        @Query("id") ids:String,
        @Query("units") units:String?,
        @Query("appid") apiKey:String
    ):Response<WeatherCities>

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") cityName:String,
        @Query("units") units:String?,
        @Query("appid") apiKey:String
    ):Response<ForecastModel>
}