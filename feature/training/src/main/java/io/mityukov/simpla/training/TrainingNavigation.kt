package io.mityukov.simpla.training

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object TrainingRoute : NavKey

@Composable
fun TrainingRouteHost(snackbarHostState: SnackbarHostState, onBack: () -> Unit) {
    TrainingScreen(snackbarHostState = snackbarHostState, onBack = onBack)
}