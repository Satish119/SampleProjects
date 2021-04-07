package com.test.weather_app.views

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.test.weather_app.R
import com.test.weather_app.model.WeatherModel
import com.test.weather_app.utils.Constants
import com.test.weather_app.utils.Utils.Companion.getIconName
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.list_item_view.view.*
import kotlinx.android.synthetic.main.list_item_view.view.locName
import org.jetbrains.annotations.NotNull
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var locations: List<WeatherModel?> = ArrayList()

    class ListViewHolder(@NotNull view: View) : RecyclerView.ViewHolder(view)

    fun setData(data: List<WeatherModel?>) {
        locations = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.locName.text = locations[position]?.name
        holder.itemView.locTemp.text = "${locations[position]?.main?.temp}${Constants.CELSIUS}"

        var sunset = false
        locations[position]?.sys?.sunset?.let {
            sunset = (((Calendar.getInstance().timeInMillis - it)/1000)/60)/60 > 0
        }

        holder.itemView.weatherIcon.setImageResource(getIconName(locations[position]?.weather?.get(0)?.main,sunset))
        holder.itemView.setOnClickListener {
            val directions = HomeFragmentDirections.actionHomeFragmentToCityFragment((locations[position]?.name)?:"",locations[position]?.main?.temp?: 0.0F)
            Navigation.findNavController(holder.itemView).navigate(directions)
        }
//        holder.itemView.dayNight.text = TextUtils.concat(
//            "${locations[position]?.main?.temp_max}",
//            "/${locations[position]?.main?.temp_min}"
//        )
    }

}