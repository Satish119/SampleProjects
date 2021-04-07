package com.test.weather_app.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.test.weather_app.R
import com.test.weather_app.viewmodels.MapViewModel
import kotlinx.android.synthetic.main.map_marker_custom_view.view.*


class CustomMarkerInfoWindowView(
    context: Context?,
    private val mapViewModel: MapViewModel
) : InfoWindowAdapter {
    private val markerItemView: View

    override fun getInfoWindow(marker: Marker): View { // 2
        markerItemView.locName.text = "Picking location...wait..."
//        markerItemView.locAddress.text = marker.position.toString()
        return markerItemView
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    init {
        markerItemView = LayoutInflater.from(context).inflate(R.layout.map_marker_custom_view, null)
    }
}