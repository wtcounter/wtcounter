package wordtextcounter.details.main.util

import android.content.Context
import android.util.TypedValue

fun dpToPx(context: Context, valueInDp: Float): Float {
  val metrics = context.resources.displayMetrics
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}