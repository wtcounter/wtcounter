package wordtextcounter.details.main.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsLogger {
  private var analyticsInstance: FirebaseAnalytics? = null

  fun init(context: Context) {
    analyticsInstance = FirebaseAnalytics.getInstance(context)
  }

  fun logAnalytics(event: AnalyticsEvents) {
    analyticsInstance?.logEvent(event.eventName, null)
  }

  fun disableAnalytics() {
    analyticsInstance?.setAnalyticsCollectionEnabled(false)
  }

  sealed class AnalyticsEvents(val eventName: String) {
    data class Click(val name :String) : AnalyticsEvents(name)
    data class NoteMilestone(val int: Int) : AnalyticsEvents(int.toString())
  }

}