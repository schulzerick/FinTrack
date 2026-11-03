package com.estudo.fintrack

import com.estudo.fintrack.domain.model.Category
import com.estudo.fintrack.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeCategoryRepository(private val initial: List<Category> = emptyList()) : CategoryRepository {


    private val _categories = MutableStateFlow(initial)

    override fun observeAll(): Flow<List<Category>> {
        return _categories
    }

    override suspend fun add(category: Category) {
        _categories.update { current -> current + category }
    }
}