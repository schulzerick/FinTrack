package com.estudo.fintrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.estudo.fintrack.domain.model.BudgetLevel
import com.estudo.fintrack.presentation.BudgetItemUi

@Composable
fun BudgetRow(item: BudgetItemUi) {

    val color = when(item.level){
        BudgetLevel.OK -> MaterialTheme.colorScheme.primary
        BudgetLevel.WARNING -> Color(0xFFF9A825)
        BudgetLevel.EXCEEDED -> MaterialTheme.colorScheme.error
    }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.categoryName, style = MaterialTheme.typography.bodyLarge)
                Text("${item.spent} / ${item.limit}", color = color)
            }

            LinearProgressIndicator(
                progress = {item.progress},
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                color = color,
            )
        }
}