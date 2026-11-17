package com.estudo.fintrack.domain

import com.estudo.fintrack.domain.model.SavingsGoal
import com.estudo.fintrack.domain.usecase.CalculateGoalProgressUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateGoalProgressUseCaseTest {

    private val useCase = CalculateGoalProgressUseCase()

    private fun goal(target: Long, saved: Long) =
        SavingsGoal(1, "Vacaciones", target, saved)

    @Test
    fun nothing_saved_means_zero_percent() {
        val progress = useCase(goal(target = 10000, saved = 0))

        assertEquals(0, progress.percent)
        assertEquals(false, progress.completed)
    }

    @Test
    fun half_saved_means_fifty_percent() {
        val progress = useCase(goal(target = 10000, saved = 5000))

        assertEquals(50, progress.percent)
        assertEquals(false, progress.completed)
    }

    @Test
    fun percent_is_truncated_not_rounded() {
        // 3333 / 10000 = 33,33 % -> 33
        val progress = useCase(goal(target = 10000, saved = 3333))

        assertEquals(33, progress.percent)
        assertEquals(false, progress.completed)
    }

    @Test
    fun one_cent_short_is_not_completed() {
        // 9999 / 10000 = 99,99 % -> 99, y todavía no está completado
        val progress = useCase(goal(target = 10000, saved = 9999))

        assertEquals(99, progress.percent)
        assertEquals(false, progress.completed)
    }

    @Test
    fun reaching_exactly_the_target_completes_the_goal() {
        val progress = useCase(goal(target = 10000, saved = 10000))

        assertEquals(100, progress.percent)
        assertEquals(true, progress.completed)
    }

    @Test
    fun saving_more_than_the_target_is_capped_at_100() {
        val progress = useCase(goal(target = 10000, saved = 12000))

        assertEquals(100, progress.percent)
        assertEquals(true, progress.completed)
    }

    @Test
    fun zero_target_does_not_crash_and_is_not_completed() {
        val progress = useCase(goal(target = 0, saved = 500))

        assertEquals(0, progress.percent)
        assertEquals(false, progress.completed)
    }
}