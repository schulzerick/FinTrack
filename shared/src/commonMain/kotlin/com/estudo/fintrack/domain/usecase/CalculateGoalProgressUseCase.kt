package com.estudo.fintrack.domain.usecase

import com.estudo.fintrack.domain.model.GoalProgress
import com.estudo.fintrack.domain.model.SavingsGoal

class CalculateGoalProgressUseCase {

    operator fun invoke(goal: SavingsGoal): GoalProgress {

        if (goal.targetCents <= 0) {
            return GoalProgress(goal, 0, false)
        }

        val rawPercent = goal.savedCents * 100 / goal.targetCents

        val percent = minOf(rawPercent, 100).toInt()

        val completed = goal.savedCents >= goal.targetCents

        return GoalProgress(goal, percent, completed)
    }
}