package com.atfotiad.composefoundry.annotations

enum class NavigationStrategy {
    JetpackCompose,
    // TypeSafeNav3 // Future use
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ComposeFoundryNavigation(
    val strategy: NavigationStrategy = NavigationStrategy.JetpackCompose,
    val graphName: String = "AppNavigator"
)
