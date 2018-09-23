package wordtextcounter.details.main.feature.extrastats

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.SharedPreferences

internal class ExtraStatsViewModelFactory(
    private val prefs: SharedPreferences) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(ExtraStatsViewModel::class.java)) {
      return modelClass.getConstructor(SharedPreferences::class.java).newInstance(prefs)
    }
    throw IllegalStateException("Unknown ViewModel class.")
  }
}