package com.estudo.fintrack.domain.usecase

import com.estudo.fintrack.domain.model.Category
import com.estudo.fintrack.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first

class SeedDefaultCategoriesUseCase(private val repository: CategoryRepository) {

    suspend operator fun invoke(){

        val categories = repository.observeAll().first()

        if(categories.isNotEmpty()){
            return
        }

        val defaultList = listOf("Comida", "Transporte", "Ocio", "Hogar",
            "Salud", "Nómina", "Otros")

        for (categoryName in defaultList){
            repository.add(Category(0, categoryName))
        }

    }


}