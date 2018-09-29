@file:Suppress("NOTHING_TO_INLINE")

package wordtextcounter.details.main.util.extensions

import android.support.v7.preference.Preference
import wordtextcounter.details.main.analytics.AnalyticsLogger
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Event

inline fun Preference.logAnalyticsOnClick() {
  setOnPreferenceClickListener {
    AnalyticsLogger.logAnalytics(Click(this.key))
    return@setOnPreferenceClickListener false
  }
}

inline fun Preference.onPreferenceChanged(crossinline block : (preference: Preference, newValue: Any) -> Boolean) {
  setOnPreferenceChangeListener { pref, newValue ->
    AnalyticsLogger.logAnalytics(Event(this.key + "_change"))
    block(pref, newValue)
  }
}