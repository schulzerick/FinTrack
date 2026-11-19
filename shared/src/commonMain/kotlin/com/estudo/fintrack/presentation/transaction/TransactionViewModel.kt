package com.estudo.fintrack.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estudo.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TransactionViewModel(private val repository: TransactionRepository): ViewModel() {

    val uiState: StateFlow<TransactionUiState> = repository.observeAll()
        .map { transactions -> TransactionUiState(transactions
            .map { it.toItemUi() }) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionUiState(emptyList()),
        )

}