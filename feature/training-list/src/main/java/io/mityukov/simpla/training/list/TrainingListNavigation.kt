package io.mityukov.simpla.training.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object TrainingListRoute : NavKey

@Composable
fun TrainingListRouteHost(
    snackbarHostState: SnackbarHostState,
    onTrainingReceived: () -> Unit,
) {
    TrainingListScreen(snackbarHostState, onTrainingReceived = onTrainingReceived)
}