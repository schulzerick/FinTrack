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
import androidx.compose.ui.unit.dp
import com.estudo.fintrack.presentation.GoalItemUi

@Composable
fun GoalRow(item: GoalItemUi) {

    val color = if(item.complete){
        MaterialTheme.colorScheme.primary
    }else{
        MaterialTheme.colorScheme.secondary
    }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.name, style = MaterialTheme.typography.bodyLarge)
                Text("${item.saved} / ${item.target}", color = color)
            }

            LinearProgressIndicator(
                progress = {item.progress},
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                color = color,
            )

            Text(
                text = if(item.complete){
                    "¡Conseguida!"
                }else{
                    "${item.percent} %"
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
}