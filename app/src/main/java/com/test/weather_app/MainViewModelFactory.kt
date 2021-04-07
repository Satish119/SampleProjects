package com.test.weather_app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.weather_app.repository.Repository
import com.test.weather_app.viewmodels.CityWeatherViewModel
import com.test.weather_app.viewmodels.MainViewModel
import com.test.weather_app.viewmodels.MapViewModel

class MainViewModelFactory(
    private val repository: Repository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MapViewModel::class.java -> MapViewModel(repository, context) as T
            CityWeatherViewModel::class.java -> CityWeatherViewModel(repository) as T
            else ->
                MainViewModel(repository, context) as T
        }
    }
}