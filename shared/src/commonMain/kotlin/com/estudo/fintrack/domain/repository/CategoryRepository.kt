package com.estudo.fintrack.domain.repository

import com.estudo.fintrack.domain.model.Category
import com.estudo.fintrack.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeAll(): Flow<List<Category>>
    suspend fun add(category: Category)
}