package wordtextcounter.details.main.analytics

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.Window
import androidx.core.content.edit
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.analytics_consent.view.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.analytics.AnalyticsLogger.disableAnalytics
import wordtextcounter.details.main.analytics.AnalyticsLogger.enableAnalytics
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics
import wordtextcounter.details.main.util.Constants
import wordtextcounter.details.main.util.Constants.PREF_ANALYTICS_CONSENT
import wordtextcounter.details.main.util.extensions.getPreference

object AnalyticsConsent {
  internal fun showConsentDialog(activity: Activity, pf: SharedPreferences) {
    val cView = LayoutInflater.from(activity)
        .inflate(R.layout.analytics_consent, null)

    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(cView)

    cView.btSave.setOnClickListener {
      pf.edit {
        cView.cbConsent?.isChecked?.let {
          markConsentAsked(activity)
          changeAnalyticsInPref(activity, it)
          if (!it) {
            disableAnalytics()
          } else {
            enableAnalytics(activity)
          }
        }
      }
      logAnalytics(Click("consent_save"))
      dialog.dismiss()
    }

    dialog.setCancelable(false)
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
  }

  fun changeAnalyticsInPref(context: Context, newValue: Boolean) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    pref.edit {
      putBoolean(Constants.PREF_USAGE_STATS, newValue)
    }
  }

  fun shouldLogAnalytics(context: Context) : Boolean {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getBoolean(Constants.PREF_USAGE_STATS, false)
  }


  fun markConsentAsked(context: Context) {
    val pref = context.getPreference()
    pref.edit {
      putBoolean(PREF_ANALYTICS_CONSENT, true)
    }
  }
}