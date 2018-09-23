package wordtextcounter.details.main.feature.settings

import android.os.Bundle
import android.preference.Preference
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Event
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics
import wordtextcounter.details.main.util.Constants

class SettingsFragment : PreferenceFragmentCompat() {

  override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.settings)

    val clipboardPref = preferenceManager.findPreference(
        Constants.PREF_CLIPBOARD)
    clipboardPref.setOnPreferenceChangeListener { _, _ ->
      logAnalytics(Event("settings_clipboard_change"))
      true
    }

    val readingTimePref = preferenceManager.findPreference(
        Constants.PREF_READING_SPEED_WORDS_PER_MINUTE)
    readingTimePref.summary = preferenceManager.sharedPreferences.getString(
        Constants.PREF_READING_SPEED_WORDS_PER_MINUTE, "")
    readingTimePref.setOnPreferenceClickListener {
      logAnalytics(Click("settings_reading_time"))
      return@setOnPreferenceClickListener false
    }

    readingTimePref.setOnPreferenceChangeListener { _, newValue ->
      logAnalytics(Event("settings_reading_time_change"))
      readingTimePref.summary = newValue.toString()
      true
    }

    val speakingTimePref = preferenceManager.findPreference(
        Constants.PREF_SPEAKING_SPEED_WORDS_PER_MINUTE)
    speakingTimePref.summary = preferenceManager.sharedPreferences.getString(
        Constants.PREF_SPEAKING_SPEED_WORDS_PER_MINUTE, "")
    speakingTimePref.setOnPreferenceClickListener {
      logAnalytics(Click("settings_speaking_time"))
      return@setOnPreferenceClickListener false
    }
    speakingTimePref.setOnPreferenceChangeListener { _, newValue ->
      logAnalytics(Event("settings_speaking_time_change"))
      speakingTimePref.summary = newValue.toString()
      true
    }

    val writingTimePref = preferenceManager.findPreference(
        Constants.PREF_WRITING_SPEED_LETTERS_PER_MINUTE)
    writingTimePref.summary = preferenceManager.sharedPreferences.getString(
        Constants.PREF_WRITING_SPEED_LETTERS_PER_MINUTE, "")
    writingTimePref.setOnPreferenceClickListener {
      logAnalytics(Click("settings_writing_time"))
      return@setOnPreferenceClickListener false
    }
    writingTimePref.setOnPreferenceChangeListener { _, newValue ->
      logAnalytics(Event("settings_writing_time_change"))
      writingTimePref.summary = newValue.toString()
      true
    }
  }

  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    fun newInstance(): SettingsFragment {
      val fragment = SettingsFragment()
      return fragment
    }
  }
}