@file:Suppress("NOTHING_TO_INLINE")

package wordtextcounter.details.main.util.extensions

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.View

inline fun Activity.showSnackBar(message: String) {
  val snackbar = Snackbar.make(findViewById<View>(android.R.id.content),
      message, Snackbar.LENGTH_LONG)
  snackbar.show()
}