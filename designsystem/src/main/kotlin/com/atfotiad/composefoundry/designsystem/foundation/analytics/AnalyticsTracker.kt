package com.atfotiad.composefoundry.designsystem.foundation.analytics

interface AnalyticsTracker {
    fun trackEvent(name: String, params: Map<String, Any> = emptyMap())
    fun setUserProperty(name: String, value: String)
}