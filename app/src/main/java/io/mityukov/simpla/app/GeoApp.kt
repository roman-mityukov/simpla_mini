package io.mityukov.simpla.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import io.mityukov.simpla.BuildConfig
import io.mityukov.simpla.core.common.di.LogsDirectory
import io.mityukov.simpla.log.Logger
import java.io.File
import javax.inject.Inject

@HiltAndroidApp
class GeoApp : Application() {
    @Inject
    @LogsDirectory
    lateinit var logsDirectory: File

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPKIT_API_KEY)

        Logger.initLogs(logsDirectory)
    }
}
