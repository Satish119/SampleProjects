package com.test.weather_app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.weather_app.model.ForecastModel
import com.test.weather_app.repository.Repository
import com.test.weather_app.utils.Constants
import kotlinx.coroutines.launch

class CityWeatherViewModel(private val repository: Repository) : ViewModel() {
    val weatherForecast: MutableLiveData<ForecastModel> = MutableLiveData()
    val error : MutableLiveData<String> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData(false)

    fun getWeatherForecast(cityName: String) {
        viewModelScope.launch {
            try {
                loading.value = true
                val response = repository.getWeatherForecast(
                    cityName,
                    Constants.UNIT_METRIC,
                    Constants.WEATHER_API_KEY
                )
                weatherForecast.value = response.body()
            } catch (e: Exception) {
                e.message?.let {
                    Log.e("Forecast_Error", it)
                    error.value = it
                }
            }
        }
    }
}