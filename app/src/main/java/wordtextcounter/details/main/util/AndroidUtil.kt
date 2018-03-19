package wordtextcounter.details.main.util

import android.content.Context
import android.util.TypedValue
import android.util.DisplayMetrics



/**
 * Created by myglamm on 19/03/18.
 */
fun dpToPx(context: Context, valueInDp: Float): Float {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}