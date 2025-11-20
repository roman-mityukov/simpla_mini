package io.mityukov.simpla.training

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.mapview.MapView
import io.mityukov.simpla.core.domain.training.TrainingProgress
import io.mityukov.simpla.core.yandexmap.MapViewHolder

@Composable
internal fun TrackMapWidget(
    modifier: Modifier = Modifier,
    progress: TrainingProgress
) {
    val context = LocalContext.current
    val mapViewHolder = remember { MapViewHolder(MapView(context), context.applicationContext) }

    LaunchedEffect(progress.track.size) {
        mapViewHolder.updateTrack(geolocations = progress.track, moveCamera = true)
    }

    TrackMapLifecycle(
        onStart = {
            mapViewHolder.onStart()
        },
        onResume = {},
        onStop = {
            mapViewHolder.onStop()
        },
    )
    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .keepScreenOn(),
        factory = { context ->
            val mapView = mapViewHolder.mapView
            mapView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            mapView.setNoninteractive(false)
            mapView
        }
    )
}