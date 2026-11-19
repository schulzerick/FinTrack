package com.estudo.fintrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estudo.fintrack.domain.model.SavingsGoal
import com.estudo.fintrack.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoalFormViewModel(
    private val repository: SavingsGoalRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(GoalFormState())
    val state = _state.asStateFlow()

    fun onNameChange(text: String) {
        _state.update { it.copy(name = text, error = null ) }
    }

    fun onTargetChange(text: String) {
        _state.update {it.copy(targetText = text, error = null) }
    }

    fun save(onSuccess: () -> Unit) {
        val current = _state.value

        val name = current.name.trim()
        if (name.isEmpty()) {
            _state.update { it.copy(error = "Escribe un nombre") }
            return
        }

        val cents = parseCents(current.targetText)
        if (cents == null) {
            _state.update { it.copy(error = "Introduce un objetivo válido") }
            return
        }

        // 3. Guardar
        viewModelScope.launch {
            repository.add(SavingsGoal(0, name, cents, 0))
            _state.update { GoalFormState() }
            onSuccess()
        }
    }
}