package io.mityukov.simpla.training

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
import io.mityukov.simpla.core.yandexmap.showTrack

@Composable
internal fun TrackMapWidget(
    modifier: Modifier = Modifier,
    progress: TrainingProgress
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }

    LaunchedEffect(progress.track.size) {
        if (progress.track.isNotEmpty()) {
            mapView.showTrack(context.applicationContext, progress.track, true)
        } else {
            mapView.map.mapObjects.clear()
        }
    }

    TrackMapLifecycle(
        onStart = {
            mapView.onStart()
        },
        onResume = {},
        onStop = {
            mapView.onStop()
        },
    )
    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .keepScreenOn(),
        factory = { _ ->
            mapView
        },
    )
}