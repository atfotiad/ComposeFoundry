package com.atfotiad.composefoundry.ui.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DebugOverlay(
    state: DebugState,
    onEnvChange: (String) -> Unit,
    onWipe: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text("Foundry Expert Mode") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("API Environment", style = MaterialTheme.typography.labelLarge)
                Row {
                    listOf("dev", "staging", "production").forEach { env ->
                        FilterChip(
                            selected = state.env == env,
                            onClick = { onEnvChange(env) },
                            label = { Text(env) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }

                Text("Current Token", style = MaterialTheme.typography.labelLarge)
                SelectionContainer { // Allows copying the token for testing
                    Text(state.token ?: "No Token", style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = onWipe,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Wipe All Local Data")
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}