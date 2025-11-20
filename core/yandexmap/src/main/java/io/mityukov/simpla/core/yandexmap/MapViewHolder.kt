package io.mityukov.simpla.core.yandexmap

import android.content.Context
import com.yandex.mapkit.mapview.MapView
import io.mityukov.simpla.core.domain.geo.Geolocation

class MapViewHolder(val mapView: MapView, private val applicationContext: Context) {
    fun updateTrack(geolocations: List<Geolocation>, moveCamera: Boolean = false) {
        if (geolocations.isNotEmpty()) {
            mapView.showTrack(applicationContext, geolocations, moveCamera)
        } else {
            clearMap()
        }
    }

    fun clearMap() {
        mapView.map.mapObjects.clear()
    }

    fun onStart() {
        mapView.onStart()
    }

    fun onStop() {
        mapView.onStop()
    }
}
