@file:Suppress("NOTHING_TO_INLINE")

package wordtextcounter.details.main.util.extensions

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager

inline fun Activity.showSnackBar(message: String) {
  val snackbar = Snackbar.make(
      findViewById<View>(android.R.id.content),
      message, Snackbar.LENGTH_LONG
  )
  snackbar.show()
}

inline fun Activity.hideKeyboard(view: View) {
  try {
    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0);
  } catch (ignored: Exception) {
    //ignored.
  }
}

inline fun Activity.showKeyBoard() {
  try {
    val imm: InputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
  } catch (ignored: Exception) {
    //ignored.
  }
}