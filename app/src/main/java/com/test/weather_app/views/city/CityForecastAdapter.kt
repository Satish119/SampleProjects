package com.test.weather_app.views.city

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.weather_app.R
import com.test.weather_app.model.WeatherForecast
import com.test.weather_app.utils.Constants
import com.test.weather_app.utils.Strings
import com.test.weather_app.utils.Utils.Companion.getIconName
import kotlinx.android.synthetic.main.city_forecast_item.view.*
import org.jetbrains.annotations.NotNull
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CityForecastAdapter : RecyclerView.Adapter<CityForecastAdapter.CityForecastViewHolder>() {

    var locations: ArrayList<WeatherForecast> = ArrayList()

    class CityForecastViewHolder(@NotNull view: View) : RecyclerView.ViewHolder(view)

    fun setData(data: ArrayList<WeatherForecast>) {
//        locations = ArrayList(data)
        for (i in 0 until data.size-1){
            if(i>0 && i%8==0)
                locations.add(data.get(i))

        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityForecastViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.city_forecast_item, parent, false)
        return CityForecastViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CityForecastViewHolder, position: Int) {
        locations[position]?.let {
            holder.itemView.temp.text = "${it.main?.temp.toString()}${Constants.CELSIUS}"
            holder.itemView.humidity.text = "${Strings.HUMIDITY} : ${it.main?.humidity?.toString()}%"
            val dateFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            holder.itemView.date.text = SimpleDateFormat("dd MMM").format(dateFormat.parse(it.dt_txt))
            holder.itemView.wind.text = "${Strings.WIND_SPEED} : ${it.wind?.speed}"

            var sunset = false
            it.sys?.sunset?.let {
                sunset = (((Calendar.getInstance().timeInMillis - it)/1000)/60)/60 > 0
            }
            holder.itemView.weatherCityIcon.setImageResource(getIconName(it.weather?.get(0)?.main,sunset))
        }
    }

}