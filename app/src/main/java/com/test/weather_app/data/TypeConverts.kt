package com.test.weather_app.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.weather_app.model.Coord
import com.test.weather_app.model.Weather
import java.io.Serializable
import java.lang.reflect.Type


class DataConverter : Serializable {
    @TypeConverter // note this annotation
    fun fromWeatherList(optionValues: List<Weather?>?): String? {
        if (optionValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather?>?>() {}.type
        return gson.toJson(optionValues, type)
    }

    @TypeConverter // note this annotation
    fun toWeatherList(optionValuesString: String?): List<Weather>? {
        if (optionValuesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather?>?>() {}.type
        return gson.fromJson<List<Weather>>(optionValuesString, type)
    }
}