package com.test.weather_app.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.test.weather_app.data.DataConverter

data class Coord(
    var lon :Double = 0.0,
    var lat :Double = 0.0
)

data class Weather(
//    var id: Int,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null
)

data class Main(
    var temp : Float,
    var pressure: Int,
    var humidity: Int,
    var temp_min : Double,
    var temp_max : Double,
    var feels_like : Double = 0.0,
    var sea_level :Int = 0,
    var grnd_level :Int = 0
)

data class Wind(
    var speed : Double,
    var deg: Int
)

data class Clouds (
    var all:Int
)

data class Sys (
    var type:Int,
//    var id:Int,
    var message:Int,
    var country: String? =null,
    var sunrise:Int,
    var sunset:Long
)
@Entity(tableName = "weather")
data class WeatherModel(
    @Embedded
    var coord: Coord?,
    @TypeConverters(DataConverter::class)
        var weather: List<Weather>?,
    var base: String?,
    @Embedded
    var main: Main?,
    var visibility:Int,
    @Embedded
    var wind: Wind?,
    @Embedded
    var clouds: Clouds?,
    var dt: Int,
    @Embedded
    var sys: Sys?,
    var name: String?,
    var cod: Int,
    var id: Int,

    @PrimaryKey(autoGenerate = true)
    var weatherId:Int
)