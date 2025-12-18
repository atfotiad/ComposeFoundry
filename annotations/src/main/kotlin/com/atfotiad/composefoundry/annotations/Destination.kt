package com.atfotiad.composefoundry.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Destination(
    val route: String = "",
    val isStartDestination: Boolean = false,
    val isTopLevel: Boolean = false
)
