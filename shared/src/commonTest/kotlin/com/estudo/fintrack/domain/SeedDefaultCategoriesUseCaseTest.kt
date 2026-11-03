package com.estudo.fintrack.domain

import com.estudo.fintrack.FakeCategoryRepository
import com.estudo.fintrack.data.toDomain
import com.estudo.fintrack.db.TransactionEntity
import com.estudo.fintrack.domain.model.Category
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import com.estudo.fintrack.domain.usecase.SeedDefaultCategoriesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class SeedDefaultCategoriesUseCaseTest {

    @Test
    fun creates_default_category_when_repository_is_empty() = runTest {
        val repository = FakeCategoryRepository()
        val useCase = SeedDefaultCategoriesUseCase(repository)

        useCase()

        val saved = repository.observeAll().first()

        assertEquals(7,saved.size)
    }

    @Test
    fun does_not_duplicate_categories_when_called_twice() = runTest {
        val repository = FakeCategoryRepository()
        val useCase = SeedDefaultCategoriesUseCase(repository)

        useCase()
        useCase()

        val saved = repository.observeAll().first()

        assertEquals(7,saved.size)
    }

    @Test
    fun does_nothing_if_categories_already_exist() = runTest {
        val repository = FakeCategoryRepository(listOf(Category(1, "Mascota")))
        val useCase = SeedDefaultCategoriesUseCase(repository)

        useCase()

        val saved = repository.observeAll().first()

        assertEquals(1,saved.size)
        assertEquals("Mascota",saved.first().name)
    }

    @Test
    fun adds_category_to_default_list() = runTest {
        val repository = FakeCategoryRepository()

        val useCase = SeedDefaultCategoriesUseCase(repository)
        useCase()

        repository.add(Category(0, "Mascota"))

        useCase()

        val saved = repository.observeAll().first()

        assertEquals(8,saved.size)
        assertEquals("Mascota",saved.last().name)
        assertEquals("Comida",saved.first().name)

    }


}