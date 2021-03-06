package wordtextcounter.details.main.feature.notes

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.base.Input
import wordtextcounter.details.main.store.daos.DraftDao
import wordtextcounter.details.main.store.data.DraftWithHistory
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory
import wordtextcounter.details.main.util.DeleteDraft
import wordtextcounter.details.main.util.EditDraft
import wordtextcounter.details.main.util.EditDraftHistory
import wordtextcounter.details.main.util.RxBus

class DraftsViewModel(private val draftDao: DraftDao) : BaseViewModel() {
  data class ViewState(
    val drafts: List<DraftWithHistory>? = null,
    val successDraftDeletion: Boolean = false,
    val successHistoryDeletion: Boolean = false,
    val showError: Boolean = false,
    val noReports: Boolean = false
  )

  val viewState: BehaviorRelay<ViewState> = BehaviorRelay.create()

  init {
    viewState.accept(ViewState())
  }

  private fun getCurrentViewState() = viewState.value!!

  fun getAllDrafts() {
    loaderState.value = true
    addDisposable(draftDao.getAllDrafts()
        .subscribeOn(Schedulers.io())
        .flatMap { t ->
          return@flatMap Flowable.fromIterable(t)
              .map { history ->
                val histories = history.draftHistories
                history.draftHistories = histories.sortedByDescending { it.createdAt }
                return@map history
              }
              .toList()
              .toFlowable()
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          loaderState.value = false
          if (it != null && !it.isEmpty()) {
            viewState.accept(
                getCurrentViewState().copy(
                    drafts = it, showError = false, successHistoryDeletion = false,
                    successDraftDeletion = false, noReports = false
                )
            )
          } else {
            viewState.accept(
                getCurrentViewState().copy(
                    showError = false, successHistoryDeletion = false,
                    successDraftDeletion = false, noReports = true
                )
            )
          }
        }, {
          loaderState.value = false
          viewState.accept(
              getCurrentViewState().copy(
                  showError = true, successHistoryDeletion = false, successDraftDeletion = false, noReports = false
              )
          )
        })
    )
  }

  fun editDraft(draft: Draft) {
    RxBus.send(EditDraft(draft.draftData.text, draft.id))
    routerState.value = Input
  }

  fun deleteDraft(draft: Draft) {
    addDisposable(
        Single.create<Boolean> {
          draftDao.deleteDraft(draft)
          RxBus.send(DeleteDraft(draft.id))
          it.onSuccess(true)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              if (it) {
                viewState.accept(
                    ViewState(
                        successDraftDeletion = true, successHistoryDeletion = false,
                        showError = false, noReports = false
                    )
                )
              }
            }, {
              viewState.accept(
                  ViewState(
                      showError = true, successHistoryDeletion = false, successDraftDeletion = false, noReports = false
                  )
              )
            })
    )
  }

  fun editDraftHistory(draftHistory: DraftHistory, parentDraft: DraftWithHistory) {
    RxBus.send(EditDraftHistory(draftHistory.text, draftHistory.draftId, parentDraft.draft.draftData.text))
    routerState.value = Input
  }

  fun deleteDraftHistory(draftHistory: DraftHistory) {
    addDisposable(
        Single.create<Boolean> {
          draftDao.deleteDraftHistory(draftHistory)
          it.onSuccess(true)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              if (it) {
                viewState.accept(
                    ViewState(
                        successHistoryDeletion = true, successDraftDeletion = false,
                        showError = false, noReports = false
                    )
                )
              }
            }, {
              viewState.accept(
                  ViewState(
                      showError = true, successHistoryDeletion = false, successDraftDeletion = false, noReports = false
                  )
              )
            })
    )
  }

}