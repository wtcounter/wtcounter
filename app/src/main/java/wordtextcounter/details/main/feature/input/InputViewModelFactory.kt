package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import wordtextcounter.details.main.store.daos.DraftDao
import wordtextcounter.details.main.store.daos.ReportDao

internal class InputViewModelFactory(private val reportDao: ReportDao, private val draftDao: DraftDao) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(InputViewModel::class.java)) {
      return modelClass.getConstructor(ReportDao::class.java, DraftDao::class.java).newInstance(reportDao, draftDao)
    }
    throw IllegalStateException("Unknown ViewModel class.")
  }
}