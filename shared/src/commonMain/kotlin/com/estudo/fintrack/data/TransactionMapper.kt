package com.estudo.fintrack.data

import com.estudo.fintrack.db.TransactionEntity
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import kotlinx.datetime.LocalDate


    fun TransactionEntity.toDomain(): Transaction {
        val parsedType = MovementType.valueOf(type)
        val parsedDate = LocalDate.parse(date)

        return Transaction(id, amountCents, parsedType, categoryId, parsedDate, note)
    }

