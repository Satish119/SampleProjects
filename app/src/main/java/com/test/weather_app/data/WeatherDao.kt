package com.test.weather_app.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.weather_app.model.WeatherModel

@Dao
interface WeatherDao {
    @Query("Select * from weather")
    suspend fun getLocations():List<WeatherModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherLoc(locationWeather:WeatherModel):Long

    @Update
    suspend fun updateWeather(weatherModel: WeatherModel)

    @Delete
    suspend fun deleteLocation(weatherModel: WeatherModel)
}