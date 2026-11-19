package com.estudo.fintrack.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.estudo.fintrack.presentation.BudgetItemUi
import com.estudo.fintrack.presentation.TransactionItemUi

@Composable

fun BudgetList (items: List<BudgetItemUi>, modifier: Modifier){
    LazyColumn(modifier = modifier) {
        items(items, key = {it.id}) {
                item -> BudgetRow(item)
            HorizontalDivider()
        }
    }
}