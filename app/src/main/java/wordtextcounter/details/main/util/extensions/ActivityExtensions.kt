@file:Suppress("NOTHING_TO_INLINE")

package wordtextcounter.details.main.util.extensions

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager

inline fun Activity.showSnackBar(message: String) {
  val snackbar = Snackbar.make(findViewById<View>(android.R.id.content),
      message, Snackbar.LENGTH_LONG)
  snackbar.show()
}

inline fun Activity.hideKeyboard() {
  val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromInputMethod(window.currentFocus.windowToken, 0)
}