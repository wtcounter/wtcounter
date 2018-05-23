package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers.io
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.entities.Report
import wordtextcounter.details.main.util.EditReport
import wordtextcounter.details.main.util.Helper.calculateSize
import wordtextcounter.details.main.util.Helper.countParagraphs
import wordtextcounter.details.main.util.Helper.countSentences
import wordtextcounter.details.main.util.Helper.countWords
import wordtextcounter.details.main.util.RxBus
import java.lang.System.currentTimeMillis

class InputViewModel(private val dao: ReportDao) : BaseViewModel() {

  data class ViewState(
      val showError: Boolean = false,
      val errorMessage: String = "",
      val report: Report? = null,
      val reportText: String = "",
      val showExpand: Boolean = false
  )
  
  val updateLiveData: MutableLiveData<Boolean> = MutableLiveData()
  val additionLiveData: MutableLiveData<Boolean> = MutableLiveData()
  val viewState: MutableLiveData<ViewState> = MutableLiveData()

  private var reportId: Int? = null

  init {
    viewState.value = ViewState()

    addDisposable(RxBus.subscribe(EditReport::class.java, Consumer {
      reportId = it.report.id
    }))
  }

  private fun currentViewState(): ViewState = viewState.value!!

  fun calculateInput(input: String) {

    if (input.trim().isEmpty()) {
      viewState.value = ViewState(showExpand = false)
      return
    }

    val report = Report("", input.trim()
        , countWords(input).toString()
        , input.length.toString()
        , countParagraphs(input).toString()
        , countSentences(input).toString()
        , 0
        , calculateSize(input))
    viewState.value = currentViewState().copy(reportText = input,
        report = report, showExpand = true, showError = false)

  }

  fun onClickSaveCurrent(name: String) {
    val reportState = viewState.value
    val report = reportState?.report
    if (report != null) {
      report.name = name
      report.time_added = currentTimeMillis()

      if (reportId == null) {
        saveReport(report)
      } else {
        report.id = reportId
        updateeport(report)
      }
    }

  }

  private fun saveReport(report: Report) {
    addDisposable(Single.create<Boolean> {
      try {
        dao.saveReport(report)
        it.onSuccess(true)
      } catch (e: Exception) {
        it.onError(e)
      }
    }.subscribeOn(io())
        .observeOn(mainThread())
        .subscribe({
          additionLiveData.value = true
          // This is to prevent showing msg continuously when fragment is popped out from backstack.
          // because liveData's onChanged() gets fired every time that happens.
          // TODO Remove this once said bug is resolved.
          additionLiveData.value = false
        }, {
          viewState.value = currentViewState().copy(showError = true)
        }))
  }

  private fun updateeport(report: Report) {
    addDisposable(Single.create<Boolean> {
      try {
        dao.updateReport(report)
        it.onSuccess(true)
      } catch (e: Exception) {
        it.onError(e)
      }
    }.subscribeOn(io())
        .observeOn(mainThread())
        .subscribe({
          updateLiveData.value = true
          // This is to prevent showing msg continuously when fragment is popped out from backstack.
          // because liveData's onChanged() gets fired every time that happens.
          // TODO Remove this once said bug is resolved.
          updateLiveData.value = false
        }, {
          viewState.value = currentViewState().copy(showError = true)
        }))
  }

  fun cancelEdit() {
    reportId = null
  }
}