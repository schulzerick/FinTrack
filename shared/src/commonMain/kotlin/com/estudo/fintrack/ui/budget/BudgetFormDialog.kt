package com.estudo.fintrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.estudo.fintrack.presentation.BudgetFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BudgetFormDialog(
    state: BudgetFormState,
    onLimitChange: (String) -> Unit,
    onCategoryChange: (Long) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo presupuesto") },
        text = {
            Column {
                OutlinedTextField(
                    value = state.limitText ,
                    onValueChange = onLimitChange,
                    label = { Text("Límite mensual") },
                )

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (category in state.categories) {
                        FilterChip(
                            selected = state.categoryId == category.id,
                            onClick = { onCategoryChange(category.id) },
                            label = { Text(category.name ) },
                        )
                    }
                }

                if (state.error != null) {
                    Text(state.error, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = { Button(onClick = onSave) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cerrar") } },
    )
}