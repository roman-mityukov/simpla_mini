package io.mityukov.simpla.core.data.repository.training

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import io.mityukov.simpla.core.domain.training.TrainingController
import io.mityukov.simpla.log.logw
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi

@AndroidEntryPoint
class ForegroundTrainingService : LifecycleService() {
    @Inject
    lateinit var trainingController: TrainingController

    @Inject
    lateinit var notification: Notification

    // TODO возможно обойтись без вэйклока и считать продолжительность трека? Но пользователь может
    // спуфить системное время.
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    @SuppressLint("WakelockTimeout")
    override fun onCreate() {
        super.onCreate()
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "io.mityukov.geo.tracking:ForegroundTrainingService"
        )
        wakeLock.acquire()
    }

    @SuppressLint("WakelockTimeout")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::wakeLock.isInitialized && wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun startForeground() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            logw("ForegroundGeolocationService no permissions - stopSelf")
            stopSelf()
            return
        } else {
            ServiceCompat.startForeground(
                this@ForegroundTrainingService,
                1,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                } else {
                    0
                },
            )
        }
    }
}