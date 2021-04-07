package com.test.weather_app.model

import com.google.gson.annotations.SerializedName

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */

class Snow {
    @SerializedName("3h")
    var _3h = 0.0
}

class WeatherForecast {
    var dt:Long = 0
    var main: Main? = null
    var weather: List<Weather>? = null
    var clouds: Clouds? = null
    var wind: Wind? = null
    var snow: Snow? = null
    var sys: Sys? = null
    var dt_txt: String? = null
}


class City {
    var id = 0
    var name: String? = null
    var coord: Coord? = null
    var country: String? = null
}

class ForecastModel {
    var cod: String? = null
    var message = 0.0
    var cnt = 0
    var list: ArrayList<WeatherForecast>? = null
    var city: City? = null
}


