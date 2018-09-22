package wordtextcounter.details.main.feature.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

  private val compositeDisposable = CompositeDisposable()
  val routerState: MutableLiveData<Router> = MutableLiveData()
  val loaderState: MutableLiveData<Boolean> = MutableLiveData()
  //Int needs to be string resourceId
  //TODO https://issuetracker.google.com/issues/109714923
  val toastLiveData: PublishRelay<Int> = PublishRelay.create()

  override fun onCleared() {
    if (!compositeDisposable.isDisposed) {
      compositeDisposable.clear()
    }
    super.onCleared()
  }

  fun addDisposable(disposable: Disposable) {
    compositeDisposable.add(disposable)
  }
}