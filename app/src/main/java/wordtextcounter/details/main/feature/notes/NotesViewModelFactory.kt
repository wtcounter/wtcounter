package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import wordtextcounter.details.main.store.daos.ReportDao

class NotesViewModelFactory(private val reportDao: ReportDao) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass::class.java == NotesViewModel::class.java) {
      return modelClass.getConstructor(ReportDao::class.java).newInstance(reportDao)
    }
    throw IllegalStateException("Unknown ViewModel class.")
  }
}