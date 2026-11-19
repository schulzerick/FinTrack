package com.estudo.fintrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estudo.fintrack.domain.repository.SavingsGoalRepository
import com.estudo.fintrack.domain.usecase.CalculateGoalProgressUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SavingsGoalViewModel(
    private val repository: SavingsGoalRepository,
) : ViewModel() {

    private val calculateProgress = CalculateGoalProgressUseCase()

    val uiState: StateFlow<GoalUiState> = repository.observeAll()
        .map { goals ->
            GoalUiState(goals.map { goal ->
                val progress = calculateProgress(goal)
                    GoalItemUi(
                        id =  goal.id ,
                        name = goal.name,
                        saved = formatCents(goal.savedCents),
                        target = formatCents(goal.targetCents),
                        percent = progress.percent,
                        progress = progress.percent / 100f,
                        complete = progress.completed,
                    )
            })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GoalUiState(emptyList()),
        )
}