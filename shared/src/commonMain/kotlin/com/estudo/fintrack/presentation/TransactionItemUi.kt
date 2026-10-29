package com.estudo.fintrack.presentation

import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction

data class TransactionItemUi(val id: Long, val title: String, val amount: String,
                             val date: String, val isExpense: Boolean)

fun Transaction.toItemUi(): TransactionItemUi{

    val amount = if (this.type == MovementType.INCOME) "+" + formatCents(amountCents)
    else "-" + formatCents(amountCents)

    val day = date.dayOfMonth.toString().padStart(2, '0')
    val month = date.monthNumber.toString().padStart(2, '0')

    val formattedDate = "$day/$month/${date.year}"

    return TransactionItemUi(id, note.ifBlank { "Sin nota" }, amount,
        formattedDate, type == MovementType.EXPENSE)
}



