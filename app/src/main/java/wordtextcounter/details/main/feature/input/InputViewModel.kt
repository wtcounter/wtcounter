@file:Suppress("MemberVisibilityCanBePrivate")

package wordtextcounter.details.main.feature.input

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers.io
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.daos.DraftDao
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.data.DraftData
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory
import wordtextcounter.details.main.store.entities.Report
import wordtextcounter.details.main.util.DeleteDraft
import wordtextcounter.details.main.util.EditDraft
import wordtextcounter.details.main.util.EditReport
import wordtextcounter.details.main.util.Helper.calculateSize
import wordtextcounter.details.main.util.Helper.countCharacters
import wordtextcounter.details.main.util.Helper.countParagraphs
import wordtextcounter.details.main.util.Helper.countSentences
import wordtextcounter.details.main.util.Helper.countWords
import wordtextcounter.details.main.util.RxBus
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit

class InputViewModel(internal val dao: ReportDao, internal val draftDao : DraftDao) : BaseViewModel() {

  data class ViewState(
      val showError: Boolean = false,
      val errorMessage: String = "",
      val report: Report? = null,
      val reportText: String = "",
      val showExpand: Boolean = false
      )

  internal data class DraftState(
      var draftId: Long? = null,
      var text: String? = null,
      var lastUpdatedTime: Long = 0
  )

  internal val draftState = DraftState()
  val updateLiveData: PublishRelay<Boolean> = PublishRelay.create()
  val additionLiveData: PublishRelay<Boolean> = PublishRelay.create()
  val toastLiveData: PublishRelay<Int> = PublishRelay.create()
  val viewState: BehaviorRelay<ViewState> = BehaviorRelay.create()
  private var counterDisposable: Disposable? = null
  val deletedDrafts = mutableListOf<Long>()

  private var reportId: Int? = null

  init {
    viewState.accept(ViewState())

    addDisposable(RxBus.subscribe(EditReport::class.java, Consumer {
      reportId = it.report.id
    }))

    addDisposable(RxBus.subscribe(DeleteDraft::class.java, Consumer {
      deletedDrafts.add(it.id)
    }))

    addDisposable(RxBus.subscribe(EditDraft::class.java, Consumer {
      draftState.draftId = it.id
      draftState.lastUpdatedTime = System.currentTimeMillis()
    }))
  }

  private fun currentViewState(): ViewState = viewState.value!!

  fun calculateInput(input: String) {

    if (input.trim().isEmpty()) {
      viewState.accept(ViewState(showExpand = false))
      return
    }

    counterDisposable?.dispose()
    counterDisposable = Singles.zip(countWords(input), countCharacters(input),
        countParagraphs(input),
        countSentences(input),
        calculateSize(input))
    { words, characters, paragraphs, sentences, size ->
      Report("", input.trim(), words.toString(), characters.toString(),
          paragraphs.toString(),
          sentences.toString(), 0, size)
    }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { t1: Report?, t2: Throwable? ->
          if (t1 != null) {
            viewState.accept(currentViewState().copy(reportText = input,
                report = t1, showExpand = true, showError = false))
          }
          if (t2 != null) {
            //TODO handle error
          }
        }
  }

  fun onTextCleared() {
   resetDraftState()
  }

  private fun resetDraftState() {
    draftState.draftId = null
    draftState.lastUpdatedTime = 0
    draftState.text = null
  }

  fun addOrUpdateDraftIfTextChanged(text: String) {
    //do nothing if text is empty.
    if (text.trim().isEmpty()) {
      return
    }

    val lastSavedText = draftState.text
    if (lastSavedText != null) {
      if (text.trim() == lastSavedText.trim()) {
        // both the texts are same. no work to do
        return
      }
    }

    var addNewDraft = false

    if (TimeUnit.HOURS.toMillis(2) < (System.currentTimeMillis() - draftState.lastUpdatedTime) || draftState.draftId == null) {
      addNewDraft = true
    }

    if (!addNewDraft && draftState.draftId != null && deletedDrafts.contains(draftState.draftId!!)) {
      addNewDraft = true
    }

    addDisposable(Flowable.create<Int>({
      if (addNewDraft) {
        handleAddNewDraft(text)
        it.onNext(DRAFT_ADDED)
      } else {
        handleAddNewHistory(text)
        it.onNext(DRAFT_UPDATED)
      }
      it.onComplete()
    }, BackpressureStrategy.BUFFER)
        .subscribeOn(io())
        .observeOn(mainThread())
        .subscribe({
          if (it == DRAFT_ADDED) {
            toastLiveData.accept(R.string.draft_saved)
          } else if (it == DRAFT_UPDATED) {
            toastLiveData.accept(R.string.draft_updated)
          }
        }, {
          //ignored
        }))
  }

  private fun handleAddNewHistory(text: String) {
    var histotyText = draftState.text
    if (histotyText == null) {
      histotyText = text
    }
    val draftHistory = DraftHistory(histotyText, System.currentTimeMillis(), draftState.draftId!!)
    draftDao.saveDraftHistory(draftHistory)
    val draftData = DraftData(text, System.currentTimeMillis())
    val draft = Draft(draftData, System.currentTimeMillis())
    draft.id = draftState.draftId!!
    draftDao.updateDraft(draft)
    draftState.text = text
    draftState.lastUpdatedTime = System.currentTimeMillis()
  }

  private fun handleAddNewDraft(text: String) {
    val draftData = DraftData(text, System.currentTimeMillis())
    val draft = Draft(draftData, System.currentTimeMillis())
    val id = draftDao.saveDraft(draft)
    draftState.draftId = id
    draftState.text = text
    draftState.lastUpdatedTime = System.currentTimeMillis()
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
          additionLiveData.accept(true)
        }, {
          viewState.accept(currentViewState().copy(showError = true))
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
          updateLiveData.accept(true)
        }, {
          viewState.accept(currentViewState().copy(showError = true))
        }))
  }

  fun cancelEdit() {
    reportId = null
  }

  companion object {
    private const val DRAFT_ADDED = 0
    private const val DRAFT_UPDATED = 1
  }
}