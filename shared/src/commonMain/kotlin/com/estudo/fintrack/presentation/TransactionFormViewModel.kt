package com.estudo.fintrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import com.estudo.fintrack.domain.repository.CategoryRepository
import com.estudo.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class TransactionFormViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _state = MutableStateFlow(TransactionFormState())

    val state: StateFlow<TransactionFormState> = _state.asStateFlow()

    init {
        viewModelScope.launch{
            categoryRepository.observeAll().collect{
                list -> _state.update { it.copy(categories = list) }
            }
        }
    }

    fun onAmountChange(text: String) {
        _state.update { it.copy(amountText = text) }
    }
    fun onTypeChange(type: MovementType) {
        _state.update { it.copy(type = type) }
    }
    fun onCategoryChange(id: Long) {
        _state.update { it.copy(categoryId = id) }
    }
    fun onNoteChange(text: String) {
        _state.update { it.copy(note = text) }
    }


    fun save(){
        val current = _state.value

        val cents = parseCents(current.amountText)
        if(cents == null){
            _state.update { it.copy(error = "Introduce un importe válido") }
            return
        }

        val categoryId = current.categoryId
        if(categoryId == null){
            _state.update { it.copy(error = "Introduce una categoria válida") }
            return
        }

        viewModelScope.launch {
            transactionRepository.add(Transaction(0, cents,
                current.type, categoryId,
                Clock.System.todayIn(TimeZone.currentSystemDefault()),
                current.note)
            )
            _state.update { it.copy(amountText = "", note = "", categoryId = null, error = null) }
        }



    }
}