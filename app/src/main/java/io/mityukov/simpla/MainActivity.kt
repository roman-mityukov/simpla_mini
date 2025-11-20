package io.mityukov.simpla

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import io.mityukov.simpla.app.AppNavHost
import io.mityukov.simpla.app.AppProps
import io.mityukov.simpla.core.designsystem.theme.GeoAppTheme
import io.mityukov.simpla.core.domain.training.TrainingController
import io.mityukov.simpla.core.domain.training.TrainingStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var trainingController: TrainingController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(
            AppProps.TRACK_CAPTURE_CHANNEL_ID,
            resources.getString(R.string.track_capture_notification_channel_description),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)

        MapKitFactory.initialize(this@MainActivity)

        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            true
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                val currentTrainingStatus = trainingController.status.first()
                splashScreen.setKeepOnScreenCondition {
                    false
                }
                setContent {
                    GeoAppTheme {
                        AppNavHost(currentTrainingStatus is TrainingStatus.Progress)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }
}
