package io.mityukov.geo.tracking.core.data.repository.geo

import android.Manifest
import android.location.Location
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

sealed interface GeolocationUpdateException {
    data object Initialization : GeolocationUpdateException
    data object LocationDisabled : GeolocationUpdateException
    data object LocationIsNull : GeolocationUpdateException
    data object PermissionsNotGranted : GeolocationUpdateException
}

data class PlatformLocationUpdateResult(
    val location: Location?,
    val error: GeolocationUpdateException?,
)

interface GeolocationProvider {
    @RequiresPermission(
        allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]
    )
    suspend fun getLastKnownLocation(): PlatformLocationUpdateResult

    @RequiresPermission(
        allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]
    )
    fun locationUpdates(
        interval: Duration,
        minDistance: Float = 0f
    ): Flow<PlatformLocationUpdateResult>
}
