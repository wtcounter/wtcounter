@file:Suppress("NOTHING_TO_INLINE")

package wordtextcounter.details.main.util.extensions

import android.support.v4.app.Fragment

inline fun Fragment.runIfAdded(block : () -> Unit) {
  if (isAdded) {
    block()
  }
}