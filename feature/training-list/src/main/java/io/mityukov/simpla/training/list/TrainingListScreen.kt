package io.mityukov.simpla.training.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.mityukov.training_list.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrainingListScreen(
    viewModel: TrainingListViewModel = hiltViewModel(),
    onTrainingReceived: () -> Unit,
) {
    val selectedTraining = remember {
        mutableStateOf("68")
    }
    val viewModelState = viewModel.stateFlow.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(stringResource(R.string.feature_training_list_selection))
            })
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TrainingListContent(
                viewModelState = viewModelState.value,
                selectedTrainingId = selectedTraining.value,
                onGetTraining = {
                    viewModel.add(TrainingListEvent.LoadTraining(selectedTraining.value))
                },
                onTrainingReceived = onTrainingReceived,
                onChangeTrainingId = { value ->
                    selectedTraining.value = value
                }
            )
        }
    }
}

@Composable
private fun TrainingListContent(
    modifier: Modifier = Modifier,
    viewModelState: TrainingListState,
    selectedTrainingId: String,
    onGetTraining: () -> Unit,
    onTrainingReceived: () -> Unit,
    onChangeTrainingId: (String) -> Unit,
) {
    when (viewModelState) {
        TrainingListState.DataReceived -> {
            LaunchedEffect(Unit) {
                onTrainingReceived()
            }
        }

        is TrainingListState.Default -> {
            val enabled = viewModelState.pending.not()
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = selectedTrainingId,
                    onValueChange = {
                        onChangeTrainingId(it)
                    },
                    enabled = enabled,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onGetTraining, enabled = enabled) {
                    Text(stringResource(R.string.feature_training_list_button_load))
                }
                if (viewModelState.error) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(R.string.feature_training_list_error), color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                    if (viewModelState.pending) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTrainingListContent(
    @PreviewParameter(provider = TrainingListStateProvider::class) state: TrainingListState
) {
    TrainingListContent(
        viewModelState = state,
        selectedTrainingId = "68",
        onGetTraining = {},
        onTrainingReceived = {},
        onChangeTrainingId = {},
    )
}

private class TrainingListStateProvider : PreviewParameterProvider<TrainingListState> {
    override val values: Sequence<TrainingListState>
        get() = sequenceOf(
            TrainingListState.Default(),
            TrainingListState.Default(error = true),
            TrainingListState.Default(pending = true),
        )
}