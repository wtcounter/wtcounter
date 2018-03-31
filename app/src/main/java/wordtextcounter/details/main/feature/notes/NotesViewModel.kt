package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.base.Input
import wordtextcounter.details.main.feature.base.Router
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.entities.Report

class NotesViewModel(private val reportDao: ReportDao) : BaseViewModel() {
  data class ViewState(val reports: List<Report>? = null)

  val viewState: MutableLiveData<ViewState> = MutableLiveData()

  init {
    viewState.value = ViewState()
  }

  private fun getCurrentViewState() = viewState.value!!

  fun getAllSavedNotes() {
    addDisposable(reportDao.getAllReports()
        .subscribeOn(io())
        .observeOn(mainThread())
        .subscribe({
          viewState.value = getCurrentViewState().copy(reports = it)
        }, {
          // TODO Implement this once we decide how to show errors.
        }))
  }

  fun editReport(position: Int) {
      routerState.value = Input
  }
}