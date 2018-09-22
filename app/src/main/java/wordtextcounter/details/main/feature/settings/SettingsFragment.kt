package wordtextcounter.details.main.feature.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import wordtextcounter.details.main.R

class SettingsFragment: PreferenceFragmentCompat()  {


  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.settings)
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