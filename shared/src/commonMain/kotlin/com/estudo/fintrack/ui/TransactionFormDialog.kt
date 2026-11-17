package com.estudo.fintrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.estudo.fintrack.domain.model.Category
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.presentation.TransactionFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionFormDialog(
    state: TransactionFormState,
    onAmountChange: (String) -> Unit,
    onTypeChange: (MovementType) -> Unit,
    onCategoryChange: (Long) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
){
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text("Nuevo movimiento") },
        text = {Column {
            OutlinedTextField(
                value = state.amountText,
                onValueChange = onAmountChange,
                label = { Text("Importe") })

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                FilterChip(
                    selected = state.type == MovementType.EXPENSE,
                    onClick = { onTypeChange(MovementType.EXPENSE) },
                    label = {Text("Gasto")}, )

                FilterChip(
                    selected = state.type == MovementType.INCOME,
                    onClick = { onTypeChange(MovementType.INCOME) },
                    label = {Text("Ingreso")}, )
            }


            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                for(category in state.categories){
                    FilterChip(
                        selected = state.categoryId == category.id,
                        onClick= { onCategoryChange(category.id) },
                        label={Text(category.name)},
                    )
                }
            }


            OutlinedTextField(
                value = state.note,
                onValueChange = onNoteChange,
                label = { Text("Nota") })

            if(state.error != null){
               Text(state.error, color = MaterialTheme.colorScheme.error)
            }

        }},
        confirmButton = { Button(onClick = onSave) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cerrar") } })
}