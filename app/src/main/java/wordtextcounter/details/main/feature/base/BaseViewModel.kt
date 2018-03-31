package wordtextcounter.details.main.feature.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

  private val compositeDisposable = CompositeDisposable()

  val routerState: MutableLiveData<Router> = MutableLiveData()

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