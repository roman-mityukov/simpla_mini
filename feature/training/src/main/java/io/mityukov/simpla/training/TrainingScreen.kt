package io.mityukov.simpla.training

import android.Manifest
import android.os.Build
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.mityukov.simpla.core.designsystem.component.ButtonBack
import io.mityukov.simpla.core.domain.training.IntervalProgress
import io.mityukov.simpla.core.domain.training.IntervalType
import io.mityukov.simpla.core.domain.training.TrainingLaunchStatus
import io.mityukov.simpla.core.domain.training.TrainingProgress
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
internal fun TrainingScreen(
    onBack: () -> Unit,
    viewModel: TrainingViewModel = hiltViewModel(),
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = buildList {
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.add(TrainingEvent.SetupTraining)
    }

    val viewModelState by viewModel.stateFlow.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.feature_training_label))
                },
                navigationIcon = {
                    ButtonBack(onBack = onBack)
                },
            )
        },
        contentWindowInsets = WindowInsets.safeContent
    ) { innerPadding ->
        if (multiplePermissionsState.allPermissionsGranted) {
            when (viewModelState) {
                is TrainingState.Progress -> {
                    TrainingWithMap(
                        modifier = Modifier.padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = innerPadding.calculateBottomPadding(),
                        ),
                        trainingProgress = (viewModelState as TrainingState.Progress).data,
                        onSwitchTraining = {
                            viewModel.add(TrainingEvent.SwitchTraining)
                        }
                    )
                }

                TrainingState.Pending -> {
                    // no op
                }
            }
        } else {
            LaunchedEffect(Unit) {
                multiplePermissionsState.launchMultiplePermissionRequest()
            }
        }
    }
}

@Composable
private fun TrainingWithMap(
    modifier: Modifier = Modifier,
    trainingProgress: TrainingProgress,
    onSwitchTraining: () -> Unit,
) {
    var mapEnabled by rememberSaveable {
        mutableStateOf(false)
    }
    val mapEnabledToPage = {
        if (mapEnabled) 1 else 0
    }

    val pagerState = rememberPagerState(mapEnabledToPage()) { 2 }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LaunchedEffect(mapEnabled) {
                coroutineScope.launch {
                    pagerState.scrollToPage(mapEnabledToPage())
                }
            }
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                beyondViewportPageCount = 1
            ) { page ->
                if (page == 0) {
                    TrainingProgressWidget(progress = trainingProgress)
                } else {
                    TrackMapWidget(progress = trainingProgress)
                }
            }
        }
        TrainingControls(
            mapEnabled = mapEnabled,
            trainingLaunched = trainingProgress.launched,
            onSwitchTraining = onSwitchTraining,
            onButtonMapClick = {
                mapEnabled = !mapEnabled
            }
        )
    }
}

@Composable
private fun TrainingProgressWidget(modifier: Modifier = Modifier, progress: TrainingProgress) {
    Surface {
        Box(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                progress.intervalProgress.forEach {
                    IntervalProgressWidget(intervalProgress = it)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(
                        R.string.feature_training_duration,
                        DateUtils.formatElapsedTime(progress.currentDuration.inWholeSeconds),
                            DateUtils.formatElapsedTime(progress.totalDuration.inWholeSeconds)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun IntervalProgressWidget(
    modifier: Modifier = Modifier,
    intervalProgress: IntervalProgress
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = intervalProgress.title)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                modifier = Modifier.weight(1f),
                progress = { intervalProgress.progress },
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "${
                    DateUtils.formatElapsedTime(
                        intervalProgress.currentDuration.inWholeSeconds
                    )
                }/${
                    DateUtils.formatElapsedTime(
                        intervalProgress.duration.inWholeSeconds
                    )
                }"
            )
        }
    }
}

@Composable
private fun TrainingControls(
    mapEnabled: Boolean,
    trainingLaunched: Boolean,
    onSwitchTraining: () -> Unit,
    onButtonMapClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = onSwitchTraining) {
            Text(
                if (trainingLaunched) stringResource(R.string.feature_training_button_stop) else stringResource(
                    R.string.feature_training_button_start
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onButtonMapClick,
            colors = if (mapEnabled) ButtonDefaults.buttonColors()
                .copy(containerColor = Color.Green) else ButtonDefaults.buttonColors()
        ) {
            Text(stringResource(R.string.feature_training_button_map))
        }
    }
}

@Preview
@Composable
fun TrainingWithMapPreview() {
    TrainingWithMap(
        trainingProgress = TrainingProgress(
            intervalProgress = listOf(
                IntervalProgress(
                    progress = 0.2f,
                    title = "title",
                    duration = 10.seconds,
                    currentDuration = 5.seconds
                ),
                IntervalProgress(
                    progress = 0f,
                    title = "title",
                    duration = 20.seconds,
                    currentDuration = 0.seconds
                ),
            ),
            currentDuration = 5.seconds,
            totalDuration = 10.seconds,
            track = listOf(),
            status = TrainingLaunchStatus.NotStarted,
        ),
        onSwitchTraining = {}
    )
}