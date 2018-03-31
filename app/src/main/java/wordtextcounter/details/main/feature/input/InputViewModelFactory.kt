package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import wordtextcounter.details.main.store.daos.ReportDao

internal class InputViewModelFactory(private val reportDao: ReportDao) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(InputViewModel::class.java)) {
      return modelClass.getConstructor(ReportDao::class.java).newInstance(reportDao)
    }
    throw IllegalStateException("Unknown ViewModel class.")
  }
}