package wordtextcounter.details.main.feature.notes

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.base.Input
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.entities.Report
import wordtextcounter.details.main.util.EditReport
import wordtextcounter.details.main.util.RxBus

class NotesViewModel(private val dao: ReportDao) : BaseViewModel() {
  data class ViewState(val reports: List<Report>? = null,
      val showError: Boolean = false,
      val successDeletion: Boolean = false,
      val noReports: Boolean = false,
      val errorMessage: String? = null)

  val viewState: BehaviorRelay<ViewState> = BehaviorRelay.create()

  init {
    viewState.accept(ViewState())
  }

  private fun getCurrentViewState() = viewState.value!!

  fun getAllSavedNotes() {
    loaderState.value = true
    addDisposable(dao.getAllReports()
        .subscribeOn(io())
        .observeOn(mainThread())
        .subscribe({
          loaderState.value = false
          if (it.isEmpty()) {
            viewState.accept(getCurrentViewState().copy(noReports = true, reports = null,
                showError = false,
                successDeletion = false))
          } else {
            viewState.accept(getCurrentViewState().copy(noReports = false, reports = it,
                showError = false,
                successDeletion = false))
          }
        }, {
          loaderState.value = false
          viewState.accept(getCurrentViewState().copy(errorMessage = null, showError = true))
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
              viewState.accept(getCurrentViewState().copy(successDeletion = true))
            }
          }, {
            it.printStackTrace()
            viewState.accept(getCurrentViewState().copy(errorMessage = null, showError = true))
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