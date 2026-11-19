package com.estudo.fintrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estudo.fintrack.domain.model.Budget
import com.estudo.fintrack.domain.repository.BudgetRepository
import com.estudo.fintrack.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetFormViewModel(
    private val budgetRepository: BudgetRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetFormState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                categoryRepository.observeAll(),
                budgetRepository.observeAll(),
            ) { categories, budgets ->
                categories.filter{ category -> budgets.none{
                    it.categoryId == category.id
                }}
            }.collect { available ->
                _state.update { it.copy(categories = available) }
            }
        }
    }

    fun onLimitChange(text: String) {
        _state.update { it.copy(limitText = text, error = null) }
    }

    fun onCategoryChange(id: Long) {
        _state.update { it.copy(categoryId = id, error = null) }
    }

    fun save(onSuccess: () -> Unit) {
        val current = _state.value

        val cents = parseCents(current.limitText)
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
            budgetRepository.add(
                Budget(0, categoryId,
                    cents)
            )
            _state.update { it.copy(limitText = "", categoryId = null, error = null) }
            onSuccess()
        }
    }
}