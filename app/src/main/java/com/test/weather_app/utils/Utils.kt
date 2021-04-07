package com.test.weather_app.utils

import android.R.bool
import com.test.weather_app.R


class Utils {
    companion object {
        fun convertTempToCelsius(temp: Double?): Double? {
            return (temp?.minus(32))?.times((9 / 5))
        }

        fun getIconName(weathertype: String?, isDayTime: Boolean): Int {
            val iconName: Int = if (weathertype == Constants.Rain) {
                if (isDayTime) R.drawable.ic_rainy else R.drawable.ic_rainy_moon
            } else if (weathertype == Constants.Clear) {
                R.drawable.ic_clear_moon
            } else if (weathertype == Constants.Clouds) {
                if (isDayTime) R.drawable.ic_cloudy else R.drawable.ic_cloudy_moon
            } else {
                R.drawable.ic_sunny
            }
            return iconName
        }
    }
}