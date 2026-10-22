package com.estudo.fintrack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estudo.fintrack.data.InMemoryTransactionRepository
import com.estudo.fintrack.presentation.SummaryViewModel


@Composable
@Preview
fun App() {
    MaterialTheme {

        val viewModel = viewModel { SummaryViewModel(InMemoryTransactionRepository()) }

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){

            Text("Ingresos: ${state.income}")
            Text("Gastos: ${state.expense}")
            Text("Balance: ${state.balance}")

            Button(onClick = { viewModel.addTestExpense() }) {
                Text("Añadir gasto de prueba")
            }


        }


    }
}