package io.mityukov.simpla.app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.mityukov.simpla.training.TrainingRoute
import io.mityukov.simpla.training.TrainingRouteHost
import io.mityukov.simpla.training.list.TrainingListRoute
import io.mityukov.simpla.training.list.TrainingListRouteHost

@Composable
fun AppNavHost(trainingLaunched: Boolean) {
    val snackbarHostState = remember { SnackbarHostState() }

    val backStack = remember {
        mutableStateListOf<Any>(if (trainingLaunched) TrainingRoute else TrainingListRoute)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<TrainingListRoute> {
                TrainingListRouteHost(
                    snackbarHostState = snackbarHostState,
                    onTrainingReceived = {
                        backStack.add(TrainingRoute)
                    },
                )
            }
            entry<TrainingRoute> {
                TrainingRouteHost(
                    snackbarHostState = snackbarHostState,
                    onBack = {
                        backStack.removeLastOrNull()
                    },
                )
            }
        }
    )
}
