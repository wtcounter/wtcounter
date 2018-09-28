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

//@Throws(ActivityNotFoundException::class)
//fun launchActionSendIntent(context: Context, text: String, intentPickerTitle: String) {
//  val sharingIntent = Intent(Intent.ACTION_SEND)
//  sharingIntent.type = "text/plain"
//  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text)
//  context.startActivity(createChooser(sharingIntent, intentPickerTitle))
//}

@Throws(ActivityNotFoundException::class)
fun launchActionSendIntent(context: Context, text: String, intentPickerTitle: String) {
  val targetedShareIntents = ArrayList<Intent>()
  val resInfo = context.packageManager.queryIntentActivities(createShareIntent(text),
      0)
  if (!resInfo.isEmpty()) {
    for (info in resInfo) {
      val targetedShare = createShareIntent(text)
      if (!info.activityInfo.packageName.equals(context.packageName, true)) {
        targetedShare.setPackage(info.activityInfo.packageName)
        targetedShareIntents.add(targetedShare)
      }
    }

    val chooserIntent = createChooser(targetedShareIntents.removeAt(0),
        intentPickerTitle)
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
        targetedShareIntents.toTypedArray())
    context.startActivity(chooserIntent)
  }
}

private fun createShareIntent(text: String): Intent {
  val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
  sharingIntent.type = "text/plain"
  sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
  return sharingIntent
}