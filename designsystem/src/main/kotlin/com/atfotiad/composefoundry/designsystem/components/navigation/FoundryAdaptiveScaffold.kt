package com.atfotiad.composefoundry.designsystem.components.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class for the App to provide UI specifics for Top Level items
 */
data class TopLevelNavConfig(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun FoundryAdaptiveScaffold(
    navConfigs: List<TopLevelNavConfig>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteItems = {
            navConfigs.forEach { config ->
                item(
                    selected = currentRoute == config.route,
                    onClick = { onNavigate(config.route) },
                    icon = { Icon(config.icon, contentDescription = config.label) },
                    label = { Text(config.label) }
                )
            }
        }
    ) {
        content()
    }
}
