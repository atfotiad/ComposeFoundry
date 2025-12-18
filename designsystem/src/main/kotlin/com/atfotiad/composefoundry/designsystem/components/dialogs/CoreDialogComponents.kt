package com.atfotiad.composefoundry.designsystem.components.dialogs

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * ==============================================================================
 * 1. CORE BASE DIALOG COMPONENT
 * ==============================================================================
 * This is the foundational component. It handles the window container,
 * styling, and layout logic, enforcing a consistent look based on the theme.
 */
@Composable
fun CoreBaseDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    icon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize(), // Smooth resize if content changes
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = elevation
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Optional Icon Slot (Pulls primary color from theme)
                if (icon != null) {
                    Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                        CompositionLocalProvider(
                            LocalContentColor provides MaterialTheme.colorScheme.primary
                        ) {
                            icon()
                        }
                    }
                }

                // 2. Optional Title Slot (Standardized styling)
                if (title != null) {
                    ProvideTextStyle(
                        MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    ) {
                        title()
                    }
                }

                // 3. Main Content Slot (Standardized body styling)
                ProvideTextStyle(
                    MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    content()
                }

                // 4. Buttons Slot (Right-aligned, spaced)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                    content = buttons
                )
            }
        }
    }
}

/**
 * ==============================================================================
 * 2. USE CASE: INFO / ERROR DIALOG (One Button)
 * ==============================================================================
 * Best for: Network errors, success messages. The most common dialog type.
 */
@Composable
fun CoreInfoDialog(
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    buttonText: String = "Okay",
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    onButtonClick: () -> Unit = onDismissRequest
) {
    CoreBaseDialog(
        onDismissRequest = onDismissRequest,
        icon = if (icon != null) {
            { Icon(imageVector = icon, contentDescription = null, tint = iconTint) }
        } else null,
        title = { Text(text = title) },
        content = { Text(text = message) },
        buttons = {
            Button(
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth() // Full width for single button
            ) {
                Text(text = buttonText)
            }
        }
    )
}

/**
 * ==============================================================================
 * 3. USE CASE: ACTION / DECISION DIALOG (Two Buttons)
 * ==============================================================================
 * Best for: Confirmation dialogs like delete or save changes.
 */
@Composable
fun CoreActionDialog(
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    positiveButtonText: String = "Confirm",
    negativeButtonText: String = "Cancel",
    isDestructive: Boolean = false, // If true, the positive button uses the error color
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit = onDismissRequest
) {
    CoreBaseDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = title) },
        content = { Text(text = message) },
        buttons = {
            // Negative Action
            TextButton(onClick = onNegativeClick) {
                Text(text = negativeButtonText)
            }

            // Positive Action
            Button(
                onClick = onPositiveClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = positiveButtonText)
            }
        }
    )
}