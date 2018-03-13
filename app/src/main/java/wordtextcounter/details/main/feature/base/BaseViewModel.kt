package wordtextcounter.details.main.feature.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

  private val compositeDisposable = CompositeDisposable()
  
  override fun onCleared() {
    if (!compositeDisposable.isDisposed) {
      compositeDisposable.clear()
    }
    super.onCleared()
  }

  fun addDisposible(disposable: Disposable) {
    compositeDisposable.add(disposable)
  }
}