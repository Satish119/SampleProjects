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
import com.test.weather_app.utils.Strings
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository, private val context: Context) :
    ViewModel() {
    val weatherInfo: MutableLiveData<List<WeatherModel?>> = MutableLiveData()
    val progress: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData()


    fun getWeather() {
        viewModelScope.launch {
            try {
                progress.value = true;
                var locations: List<WeatherModel> = emptyList()
                val locIds: ArrayList<Int> = ArrayList()
                val database: WeatherDatabase? = WeatherDatabase.getAppDataBase(context)
                if (database != null) {
                    locations = database.weatherDao().getLocations()
                    locations.let{
                        it.distinctBy { it.id }
                        it.forEach {
                            if (it.sys != null)
                                locIds.add(it.id)
                        }
                    }
                }

                when {
                    locations.isNotEmpty() -> {
                        weatherInfo.value = locations
                        val response = repository.getListOfWeather(
                            locIds.joinToString(separator = ","),
                            Constants.UNIT_METRIC,
                            Constants.WEATHER_API_KEY
                        )
                        weatherInfo.value = response.body()?.list
                    }
                    locations.isEmpty() -> error.value = Strings.NO_LOCATIONS
                }
                progress.value = false;
            } catch (e: Exception) {
                Log.e("error", e.message ?: "")
                progress.value = false
                error.value = e.message
            }
        }
    }

    fun deleteLocation(position: Int) {
        viewModelScope.launch {
            val database: WeatherDatabase? = WeatherDatabase.getAppDataBase(context)
            val locations = database?.weatherDao()?.getLocations()
            locations?.get(position)?.let {
                database?.weatherDao()?.deleteLocation(it)
            }
            weatherInfo.value = database?.weatherDao()?.getLocations()
        }
    }
    fun insertData(data: WeatherModel) {
        viewModelScope.launch {
            val database: WeatherDatabase? = WeatherDatabase.getAppDataBase(context)
            val success:Long? = database?.weatherDao()?.insertWeatherLoc(data)
            if(success!=null && success>0.0)
                weatherInfo.value = database.weatherDao().getLocations()
        }
    }
}