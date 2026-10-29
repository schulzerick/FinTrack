package com.estudo.fintrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.estudo.fintrack.presentation.TransactionItemUi

@Composable
fun TransactionRow(item: TransactionItemUi){
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(item.title, style = MaterialTheme.typography.bodyLarge)
            Text(item.date, style = MaterialTheme.typography.bodySmall)
        }
        Text(item.amount,
            color =
                if(item.isExpense) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary)
    }
}