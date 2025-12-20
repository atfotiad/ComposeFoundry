package com.atfotiad.composefoundry.analytics
import com.atfotiad.composefoundry.designsystem.foundation.analytics.AnalyticsTracker
import com.atfotiad.composefoundry.designsystem.foundation.logging.FoundryLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoundryAnalyticsTracker @Inject constructor() : AnalyticsTracker {
    override fun trackEvent(name: String, params: Map<String, Any>) {
        // Example: FirebaseAnalytics.getInstance(context).logEvent(name, bundle)
        FoundryLogger.d("Analytics", "Event: $name | Params: $params")
    }

    override fun setUserProperty(name: String, value: String) {
        FoundryLogger.d("Analytics", "User Prop: $name = $value")
    }
}
