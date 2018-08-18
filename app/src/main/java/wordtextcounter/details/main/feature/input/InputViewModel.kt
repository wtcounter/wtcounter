package wordtextcounter.details.main.feature.input

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.entities.Report
import wordtextcounter.details.main.util.EditReport
import wordtextcounter.details.main.util.Helper.calculateSize
import wordtextcounter.details.main.util.Helper.countCharacters
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

  val updateLiveData: PublishSubject<Boolean> = PublishSubject.create()
  val additionLiveData: PublishSubject<Boolean> = PublishSubject.create()
  val viewState: ReplaySubject<ViewState> = ReplaySubject.create()

  private var reportId: Int? = null

  init {
    viewState.onNext(ViewState())

    addDisposable(RxBus.subscribe(EditReport::class.java, Consumer {
      reportId = it.report.id
    }))
  }

  private fun currentViewState(): ViewState = viewState.value!!

  fun calculateInput(input: String) {

    if (input.trim().isEmpty()) {
      viewState.onNext(ViewState(showExpand = false))
      return
    }

    val report = Report("", input.trim()
        , words = countWords(input).toString()
        , characters = countCharacters(input).toString()
        , paragraphs = countParagraphs(input).toString()
        , sentences = countSentences(input).toString()
        , time_added = 0
        , size = calculateSize(input))
    viewState.onNext(currentViewState().copy(reportText = input,
        report = report, showExpand = true, showError = false))
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
        updateReport(report)
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
          additionLiveData.onNext(true)
        }, {
          viewState.onNext(currentViewState().copy(showError = true))
        }))
  }

  private fun updateReport(report: Report) {
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
          reportId = null
          updateLiveData.onNext(true)
        }, {
          viewState.onNext(currentViewState().copy(showError = true))
        }))
  }

  fun cancelEdit() {
    reportId = null
  }
}