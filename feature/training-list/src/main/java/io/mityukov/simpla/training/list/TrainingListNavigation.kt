package io.mityukov.simpla.training.list

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object TrainingListRoute : NavKey

@Composable
fun TrainingListRouteHost(
    onTrainingReceived: () -> Unit,
) {
    TrainingListScreen(onTrainingReceived = onTrainingReceived)
}