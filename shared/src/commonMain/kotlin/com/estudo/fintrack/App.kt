package com.estudo.fintrack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estudo.fintrack.data.DatabaseDriverFactory
import com.estudo.fintrack.data.SqlDelightCategoryRepository
import com.estudo.fintrack.data.SqlDelightTransactionRepository
import com.estudo.fintrack.db.FinTrackDatabase
import com.estudo.fintrack.domain.usecase.SeedDefaultCategoriesUseCase
import com.estudo.fintrack.presentation.SummaryViewModel
import com.estudo.fintrack.presentation.TransactionViewModel
import com.estudo.fintrack.ui.TransactionList


@Composable

fun App(driverFactory: DatabaseDriverFactory) {
    MaterialTheme {


        val database = remember {
            val driver = driverFactory.createDriver()
            FinTrackDatabase(driver)
        }

        val transactionRepository = remember {
            SqlDelightTransactionRepository(database)
        }

        val categoryRepository = remember {
            SqlDelightCategoryRepository(database)
        }

        LaunchedEffect(Unit) {
            SeedDefaultCategoriesUseCase(categoryRepository)()
        }

        val summaryViewModel = viewModel { SummaryViewModel(transactionRepository) }
        val transactionViewModel = viewModel { TransactionViewModel(transactionRepository) }

        val summaryState by summaryViewModel.uiState.collectAsStateWithLifecycle()
        val transactionState by transactionViewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){

            Text("Ingresos: ${summaryState.income}")
            Text("Gastos: ${summaryState.expense}")
            Text("Balance: ${summaryState.balance}")

            Button(onClick = { summaryViewModel.addTestExpense() }) {
                Text("Añadir gasto de prueba")
            }

            TransactionList(transactionState.items, modifier = Modifier.weight(1f))


        }


    }
}