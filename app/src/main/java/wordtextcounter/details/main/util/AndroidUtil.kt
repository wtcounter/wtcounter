package wordtextcounter.details.main.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.util.TypedValue

fun dpToPx(context: Context, valueInDp: Float): Float {
  val metrics = context.resources.displayMetrics
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}

fun dbExists(context: Context, dbName: String): Boolean {
  val dbFile = context.getDatabasePath(dbName)
  return dbFile.exists()
}

@Throws(ActivityNotFoundException::class)
fun launchActionSendIntent(context: Context, text: String, intentPickerTitle: String) {
  val sharingIntent = Intent(Intent.ACTION_SEND)
  sharingIntent.type = "text/plain"
  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text)
  context.startActivity(createChooser(sharingIntent, intentPickerTitle))
}