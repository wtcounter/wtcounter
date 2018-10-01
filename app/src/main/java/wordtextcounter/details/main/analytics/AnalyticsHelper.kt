package wordtextcounter.details.main.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import wordtextcounter.details.main.BuildConfig

object AnalyticsLogger {
  private lateinit var analyticsInstance: FirebaseAnalytics

  fun init(context: Context) {
    analyticsInstance = FirebaseAnalytics.getInstance(context)
  }

  fun logAnalytics(event: AnalyticsEvents) {
    if (!BuildConfig.DEBUG) {
      analyticsInstance.logEvent(event.eventName, null)
    }
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
    data class Click(val name :String) : AnalyticsEvents("click_$name")
    data class Event(val name :String) : AnalyticsEvents("event_$name")
    data class NoteMilestone(val int: Int) : AnalyticsEvents("total_notes_" + int.toString())
  }
}