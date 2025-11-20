package io.mityukov.simpla.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.mityukov.simpla.core.designsystem.icon.AppIcons
import io.mityukov.simpla.core.designsystem.R

@Composable
fun CommonAlertDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    showDismissButton: Boolean = true
) {
    AlertDialog(
        modifier = modifier,
        icon = {
            Icon(
                imageVector = AppIcons.Attention,
                contentDescription = stringResource(R.string.core_designsystem_content_description_attention)
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.core_designsystem_dialog_yes))
            }
        },
        dismissButton = if (showDismissButton) {
            {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(stringResource(R.string.core_designsystem_dialog_no))
                }
            }
        } else {
            null
        }
    )
}
