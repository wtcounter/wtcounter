package wordtextcounter.details.main.feature.base

import android.app.Service
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import butterknife.ButterKnife
import butterknife.Unbinder


/**
 * Created by hirak on 02/12/17.
 */
abstract class BaseFragment : Fragment() {

  private var unbinder: Unbinder? = null

  var imm: InputMethodManager? = null
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unbinder = view.let { ButterKnife.bind(this, it) }

    imm = activity?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
  }

  protected fun hideSoftKeyboard() {
    val view = activity?.currentFocus
    if (view != null) {
      imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
  }

  protected fun showSoftKeyboard() {
    val view = activity?.currentFocus
    if (view != null) {
      imm?.showSoftInputFromInputMethod(view.windowToken, 0)
    }
  }

  override fun onDestroyView() {
    unbinder?.unbind()
    super.onDestroyView()
  }
}