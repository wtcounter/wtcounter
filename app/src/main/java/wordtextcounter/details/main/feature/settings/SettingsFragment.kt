package wordtextcounter.details.main.feature.settings

import android.os.Bundle
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import wordtextcounter.details.main.R
import wordtextcounter.details.main.util.Constants
import wordtextcounter.details.main.util.extensions.logAnalyticsOnClick
import wordtextcounter.details.main.util.extensions.onPreferenceChanged

class SettingsFragment : PreferenceFragmentCompat() {

  override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.settings)

    val readingTimePref = preferenceManager.findPreference(
        Constants.PREF_READING_SPEED_WORDS_PER_MINUTE)
    readingTimePref.summary = preferenceManager.sharedPreferences.getString(
        Constants.PREF_READING_SPEED_WORDS_PER_MINUTE, "")
    readingTimePref.logAnalyticsOnClick()

    readingTimePref.onPreferenceChanged { _, newValue ->
      readingTimePref.summary = newValue.toString()
      true
    }

    val speakingTimePref = preferenceManager.findPreference(
        Constants.PREF_SPEAKING_SPEED_WORDS_PER_MINUTE)
    speakingTimePref.summary = preferenceManager.sharedPreferences.getString(
        Constants.PREF_SPEAKING_SPEED_WORDS_PER_MINUTE, "")
    speakingTimePref.logAnalyticsOnClick()
    speakingTimePref.onPreferenceChanged { _, newValue ->
      speakingTimePref.summary = newValue.toString()
      true
    }

    val writingTimePref = preferenceManager.findPreference(
        Constants.PREF_WRITING_SPEED_LETTERS_PER_MINUTE)
    writingTimePref.summary = preferenceManager.sharedPreferences.getString(
        Constants.PREF_WRITING_SPEED_LETTERS_PER_MINUTE, "")
    writingTimePref.logAnalyticsOnClick()
    writingTimePref.onPreferenceChanged { _, newValue ->
      writingTimePref.summary = newValue.toString()
      true
    }

    val encodingPref = preferenceManager.findPreference(
        Constants.PREF_ENCODING_SET)
    encodingPref.logAnalyticsOnClick()
    encodingPref.onPreferenceChanged { _, _ ->
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