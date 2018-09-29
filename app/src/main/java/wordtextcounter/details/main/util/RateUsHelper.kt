package wordtextcounter.details.main.util

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.KeyEvent.KEYCODE_BACK
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import com.example.rateus.RateusCore.onNoSelected
import com.example.rateus.RateusCore.onRemindLaterSelected
import com.example.rateus.RateusCore.onYesSelected
import com.example.rateus.RateusCore.shouldShowRateUsDialog
import kotlinx.android.synthetic.main.rate_us_dialog.btnLater
import kotlinx.android.synthetic.main.rate_us_dialog.btnNever
import kotlinx.android.synthetic.main.rate_us_dialog.btnSave
import kotlinx.android.synthetic.main.rate_us_dialog.ivCross
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Event
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics
import wordtextcounter.details.main.util.Constants.PLAY_STORE_MARKET_DEEPLINK
import wordtextcounter.details.main.util.Constants.PLAY_STORE_URL

object RateUsHelper {
  fun showRateUsDialog(activity: Activity) {
    if (shouldShowRateUsDialog(activity)) {
      val cView = LayoutInflater.from(activity)
          .inflate(R.layout.rate_us_dialog, null)


      val dialog = Dialog(activity)
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
      dialog.setContentView(cView)
      dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      dialog.ivCross.setOnClickListener {
        logAnalytics(Click("rateus_close"))
        onRemindLaterSelected(activity)
        dialog.dismiss()
      }

      dialog.btnSave.setOnClickListener {
        logAnalytics(Click("rateus_rate_now"))
        dialog.dismiss()
        onYesSelected(activity)
        redirectToPlayStore(activity)
      }

      dialog.btnLater.setOnClickListener {
        logAnalytics(Click("rateus_later"))
        onRemindLaterSelected(activity)
        dialog.dismiss()
      }

      dialog.btnNever.setOnClickListener {
        logAnalytics(Click("rateus_never"))
        onNoSelected(activity)
        dialog.dismiss()
      }

      dialog.setOnKeyListener { _, keyCode, _ ->
        if (keyCode == KEYCODE_BACK) {
          logAnalytics(Click("rateus_backpress"))
          onRemindLaterSelected(activity)
          dialog.dismiss()
        }
        return@setOnKeyListener true
      }

      dialog.setCancelable(false)
      logAnalytics(Event("rateus_dialog_shown"))
      dialog.show()
    }
  }

  fun redirectToPlayStore(activity: Activity) {
    try {
      val viewIntent = Intent(Intent.ACTION_VIEW,
          Uri.parse(PLAY_STORE_MARKET_DEEPLINK))
      activity.startActivity(viewIntent)
    } catch (e: ActivityNotFoundException) {
      activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL)))
    } catch (e : Exception) {
      Toast.makeText(activity, activity.getString(R.string.rate_us_error), Toast.LENGTH_LONG).show()
    }
  }
}