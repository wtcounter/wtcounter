package wordtextcounter.details.main.feature.base

import android.app.Service
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * Created by hirak on 02/12/17.
 */
abstract class BaseFragment : Fragment() {


  var imm: InputMethodManager? = null
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    retainInstance = true

    imm = activity?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
  }

  protected fun hideSoftKeyboard() {
    val view = activity?.currentFocus
    if (view != null) {
      imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
  }

  protected fun showError(message: String) {
    view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
  }

  protected fun showSoftKeyboard(focus: View) {
    imm?.showSoftInputFromInputMethod(focus.windowToken, 0)
  }

  override fun onDestroyView() {
    super.onDestroyView()
  }
}