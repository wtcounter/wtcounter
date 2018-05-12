package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.base.Input
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.entities.Report
import wordtextcounter.details.main.util.DeleteReport
import wordtextcounter.details.main.util.EditReport
import wordtextcounter.details.main.util.RxBus

class NotesViewModel(private val dao: ReportDao) : BaseViewModel() {
  data class ViewState(val reports: List<Report>? = null,
                       val showError: Boolean = false,
                       val successDeletion: Boolean = false,
                       val errorMessage: String? = null)

  val viewState: MutableLiveData<ViewState> = MutableLiveData()

  init {
    viewState.value = ViewState()
  }

  private fun getCurrentViewState() = viewState.value!!

  fun getAllSavedNotes() {
    addDisposable(dao.getAllReports()
        .subscribeOn(io())
        .observeOn(mainThread())
        .subscribe({
          viewState.value = getCurrentViewState().copy(reports = it)
        }, {
          viewState.value = getCurrentViewState().copy(errorMessage = null, showError = true)
        }))
  }
  
  fun deleteReport(position: Int) {
    viewState.value?.reports?.get(position)?.let { report ->
      addDisposable(Single.create<Boolean> { e ->
        dao.deleteReport(report)
        e.onSuccess(true)
      }.subscribeOn(io())
          .observeOn(mainThread())
          .subscribe({
            if (it) {
              getAllSavedNotes()
              RxBus.send(DeleteReport(report))
              viewState.value = getCurrentViewState().copy(successDeletion = true)
            }
          }, {
            it.printStackTrace()
            viewState.value = getCurrentViewState().copy(errorMessage = null, showError = true)
          }))
    }
  }

  fun editReport(position: Int) {
    viewState.value?.reports?.get(position)?.let {
      RxBus.send(EditReport(it))
      routerState.value = Input
    }
  }
}