package wordtextcounter.details.main.feature.notes

import com.jakewharton.rxrelay2.BehaviorRelay
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.daos.DraftDao
import wordtextcounter.details.main.store.data.DraftWithHistory

class DraftsViewModel(private val draftDao: DraftDao) : BaseViewModel() {
  data class ViewState(val drafts: List<DraftWithHistory>? = null,
                       val showError: Boolean = false)

  val viewState: BehaviorRelay<ViewState> = BehaviorRelay.create()

  init {
    viewState.accept(ViewState())
  }

  private fun getCurrentViewState() = viewState.value!!

  fun getAllDrafts() {
    addDisposable(draftDao.getAllDrafts()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          if (it != null) {
            viewState.accept(getCurrentViewState().copy(drafts = it, showError = false))
          } else {
            Logger.d("list is null")
          }
        }, {
          viewState.accept(getCurrentViewState().copy(showError = true))
        }))
  }

}