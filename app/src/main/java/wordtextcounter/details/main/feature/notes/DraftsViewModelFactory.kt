package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import wordtextcounter.details.main.store.daos.DraftDao

internal class DraftsViewModelFactory(private val draftDao: DraftDao) : ViewModelProvider.Factory {

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(DraftsViewModel::class.java)) {
      return modelClass.getConstructor(DraftDao::class.java)
          .newInstance(draftDao)
    }
    throw IllegalStateException("Unknown ViewModel class.")
  }
}