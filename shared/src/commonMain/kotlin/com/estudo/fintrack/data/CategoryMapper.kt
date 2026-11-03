package com.estudo.fintrack.data


import com.estudo.fintrack.db.CategoryEntity
import com.estudo.fintrack.domain.model.Category


fun CategoryEntity.toDomain(): Category {
    return Category(id, name)
}