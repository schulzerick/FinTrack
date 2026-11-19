package com.estudo.fintrack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.estudo.fintrack.presentation.GoalFormState

@Composable
fun GoalFormDialog(
    state: GoalFormState,
    onNameChange: (String) -> Unit,
    onTargetChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva meta") },
        text = {
            Column {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre") },
                )

                OutlinedTextField(
                    value = state.targetText,
                    onValueChange = onTargetChange,
                    label = { Text("Objetivo") },
                )

                if (state.error != null) {
                    Text(state.error, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = { Button(onClick = onSave) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cerrar") } },
    )
}