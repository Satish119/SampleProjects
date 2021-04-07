package com.test.weather_app.model

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */

class WeatherCities {
    var cnt = 0
    var list: List<WeatherModel>? = null
}


