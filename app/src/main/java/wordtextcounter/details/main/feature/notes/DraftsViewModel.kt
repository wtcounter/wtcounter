package wordtextcounter.details.main.feature.notes

import com.jakewharton.rxrelay2.BehaviorRelay
import com.orhanobut.logger.Logger
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.base.Input
import wordtextcounter.details.main.store.daos.DraftDao
import wordtextcounter.details.main.store.data.DraftWithHistory
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory
import wordtextcounter.details.main.util.DeleteDraft
import wordtextcounter.details.main.util.EditDraft
import wordtextcounter.details.main.util.Helper
import wordtextcounter.details.main.util.RxBus
import java.util.*

class DraftsViewModel(private val draftDao: DraftDao) : BaseViewModel() {
  data class ViewState(val drafts: List<DraftWithHistory>? = null,
                       val successDraftDeletion: Boolean = false,
                       val successHistoryDeletion: Boolean = false,
                       val showError: Boolean = false)

  val viewState: BehaviorRelay<ViewState> = BehaviorRelay.create()

  init {
    viewState.accept(ViewState())
  }

  private fun getCurrentViewState() = viewState.value!!

  fun getAllDrafts() {
    addDisposable(draftDao.getAllDrafts()
        .subscribeOn(Schedulers.io())
        .flatMap { t ->
          return@flatMap Flowable.fromIterable(t)
              .map {
                val histories = it.draftHistories
                it.draftHistories = histories.sortedByDescending { it.createdAt }
                return@map it
              }
              .toList()
              .toFlowable()
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          if (it != null) {
            viewState.accept(getCurrentViewState().copy(drafts = it, showError = false, successHistoryDeletion = false, successDraftDeletion = false))
          }
        }, {
          viewState.accept(getCurrentViewState().copy(showError = true, successHistoryDeletion = false, successDraftDeletion = false))
        }))
  }

  fun editDraft(draft: Draft) {
    RxBus.send(EditDraft(draft.draftData.text))
    routerState.value = Input
  }

  fun deleteDraft(draft: Draft) {
    addDisposable(Single.create<Boolean>({
      draftDao.deleteDraft(draft)
      draft.id?.let { it1 -> DeleteDraft(it1) }?.let { it2 -> RxBus.send(it2) }
      it.onSuccess(true)
    })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          if (it) {
            viewState.accept(ViewState(successDraftDeletion = true, successHistoryDeletion = false, showError = false))
          }
        }, {
          viewState.accept(ViewState(showError = true, successHistoryDeletion = false, successDraftDeletion = false))
        }))
  }

  fun editDraftHistory(draftHistory: DraftHistory) {
    RxBus.send(EditDraft(draftHistory.text))
    routerState.value = Input
  }

  fun deleteDraftHistory(draftHistory: DraftHistory) {
    addDisposable(Single.create<Boolean>({
      draftDao.deleteDraftHisoty(draftHistory)
      it.onSuccess(true)
    })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          if (it) {
            viewState.accept(ViewState(successHistoryDeletion = true, successDraftDeletion = false, showError = false))
          }
        }, {
          viewState.accept(ViewState(showError = true, successHistoryDeletion = false, successDraftDeletion = false))
        }))
  }

}