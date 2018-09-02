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

  fun logNoteMilestones(totalNotes : Int) {
    var count : Int? = null
    when {
      totalNotes in 5..9 -> count = 5
      totalNotes in 10..19 -> count = 10
      totalNotes in 20..49 -> count = 20
      totalNotes >= 50 -> count = 50
    }
    if (count != null) {
      logAnalytics(AnalyticsEvents.NoteMilestone(count))
    }
  }

  sealed class AnalyticsEvents(val eventName: String) {
    data class Click(val name :String) : AnalyticsEvents(name)
    data class NoteMilestone(val int: Int) : AnalyticsEvents(int.toString())
  }

}