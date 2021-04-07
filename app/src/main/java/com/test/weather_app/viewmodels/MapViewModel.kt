package com.test.weather_app.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.weather_app.data.WeatherDatabase
import com.test.weather_app.model.WeatherModel
import com.test.weather_app.repository.Repository
import com.test.weather_app.utils.Constants
import kotlinx.coroutines.launch

class MapViewModel(private val repository: Repository, private val context: Context) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData("")
    val progress: MutableLiveData<Boolean> = MutableLiveData(false)
    val data: MutableLiveData<List<WeatherModel>> = MutableLiveData()

    fun addWeatherLocation(lat: Double, lon: Double) {
            viewModelScope.launch {
                try {
                    progress.value = true
                    val response = repository.getWeather(
                        lat,
                        lon,
                        Constants.UNIT_METRIC,
                        Constants.WEATHER_API_KEY
                    )
                    response.body()?.let {
                        insertNewLocation(it)
                    }

                } catch (e: Exception) {
                    Log.e("error", e.message ?: "")
                    progress.value = true
                    error.value = e.message
                }
            }
    }

    private fun insertNewLocation(newLoc:WeatherModel){
        viewModelScope.launch {
            val database: WeatherDatabase? = WeatherDatabase.getAppDataBase(context)
            val success:Long? = database?.weatherDao()?.insertWeatherLoc(newLoc)
            if(success!=null && success>0.0)
                data.value = database.weatherDao().getLocations()
        }

    }


}