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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estudo.fintrack.data.DatabaseDriverFactory
import com.estudo.fintrack.data.SqlDelightBudgetRepository
import com.estudo.fintrack.data.SqlDelightCategoryRepository
import com.estudo.fintrack.data.SqlDelightSavingsGoalRepository
import com.estudo.fintrack.data.SqlDelightTransactionRepository
import com.estudo.fintrack.db.FinTrackDatabase
import com.estudo.fintrack.domain.usecase.SeedDefaultCategoriesUseCase
import com.estudo.fintrack.presentation.SummaryViewModel
import com.estudo.fintrack.presentation.TransactionFormViewModel
import com.estudo.fintrack.presentation.TransactionViewModel
import com.estudo.fintrack.ui.TransactionFormDialog
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

        val budgetRepository = remember {
            SqlDelightBudgetRepository(database)
        }

        val savingsGoalRepository = remember {
            SqlDelightSavingsGoalRepository(database)
        }

        LaunchedEffect(Unit) {
            SeedDefaultCategoriesUseCase(categoryRepository)()
        }

        val summaryViewModel = viewModel { SummaryViewModel(transactionRepository) }
        val transactionViewModel = viewModel { TransactionViewModel(transactionRepository) }

        val summaryState by summaryViewModel.uiState.collectAsStateWithLifecycle()
        val transactionState by transactionViewModel.uiState.collectAsStateWithLifecycle()

        val formViewModel = viewModel { TransactionFormViewModel(transactionRepository, categoryRepository) }
        val formState by formViewModel.state.collectAsStateWithLifecycle()

        var showForm by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){

            Text("Ingresos: ${summaryState.income}")
            Text("Gastos: ${summaryState.expense}")
            Text("Balance: ${summaryState.balance}")

            Button(onClick = { showForm = true}) {
                Text("Nuevo movimiento")
            }

            TransactionList(transactionState.items, modifier = Modifier.weight(1f))


        }


        if(showForm){
            TransactionFormDialog(
                state = formState,
                onAmountChange = formViewModel::onAmountChange,
                onTypeChange = formViewModel::onTypeChange,
                onCategoryChange = formViewModel::onCategoryChange,
                onNoteChange = formViewModel::onNoteChange,
                onSave = formViewModel::save,
                onDismiss = { showForm = false},
            )
        }

    }
}