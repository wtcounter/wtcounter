package wordtextcounter.details.main.feature.base

import android.app.Service
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.reactivex.disposables.CompositeDisposable
import wordtextcounter.details.main.feature.input.InputFragment

abstract class BaseFragment : Fragment() {

  var imm: InputMethodManager? = null

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

    imm = activity?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
  }

  override fun onDestroyView() {
    disposable.clear()
    super.onDestroyView()
  }

  protected fun hideSoftKeyboard() {
    val view = activity?.currentFocus
    if (view != null) {
      imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
  }

  protected fun showError(message: String) {
    view?.let {
      Snackbar.make(it, message, Snackbar.LENGTH_LONG)
          .show()
    }
  }

  protected fun showSoftKeyboard(focus: View) {
    imm?.showSoftInputFromInputMethod(focus.windowToken, 0)
  }

}