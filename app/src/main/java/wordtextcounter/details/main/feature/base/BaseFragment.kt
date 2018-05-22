package wordtextcounter.details.main.feature.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import wordtextcounter.details.main.feature.input.InputFragment

abstract class BaseFragment : Fragment() {

  abstract val baseViewModel: BaseViewModel

  val disposable = CompositeDisposable()

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    retainInstance = true

    baseViewModel.routerState.observe(this, Observer {
      when (it) {
        is Input -> (activity as BaseActivity).replaceFragment(InputFragment.newInstance())
      }
    })
  }

  override fun onDestroyView() {
    disposable.clear()
    super.onDestroyView()
  }

  protected fun showError(message: String) {
    view?.let {
      Snackbar.make(it, message, Snackbar.LENGTH_LONG)
          .show()
    }
  }
}