package io.mityukov.simpla.app

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
    val backStack = remember {
        mutableStateListOf<Any>(if (trainingLaunched) TrainingRoute else TrainingListRoute)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<TrainingListRoute> {
                TrainingListRouteHost(
                    onTrainingReceived = {
                        backStack.add(TrainingRoute)
                    },
                )
            }
            entry<TrainingRoute> {
                TrainingRouteHost(
                    onBack = {
                        if (backStack.size == 1) {
                            backStack.clear()
                            backStack.add(TrainingListRoute)
                        } else {
                            backStack.removeLastOrNull()
                        }
                    },
                )
            }
        }
    )
}
