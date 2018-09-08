package wordtextcounter.details.main.analytics

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.AppCompatCheckBox
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import androidx.core.content.edit
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger.disableAnalytics
import wordtextcounter.details.main.util.Constants.PREF_ANALYTICS_CONSENT
import wordtextcounter.details.main.util.Constants.PREF_ANALYTICS_ENABLED
import wordtextcounter.details.main.util.extensions.getPreference

object AnalyticsConsent {

  fun init(app: Application) {
    app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
      override fun onActivityPaused(p0: Activity?) {
        //no-op
      }

      override fun onActivityResumed(p0: Activity?) {
        //no-op
      }

      override fun onActivityStarted(p0: Activity?) {
        val pf = p0?.getPreference()
        val consent = pf?.getBoolean(PREF_ANALYTICS_CONSENT, false)
        consent?.let {
          if (!it) {
            showConsentDialog(p0, pf)
          }
        }
      }

      override fun onActivityDestroyed(p0: Activity?) {
        //no-op
      }

      override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
        //no-op
      }

      override fun onActivityStopped(p0: Activity?) {
        //no-op
      }

      override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        //no-op
      }

    })
  }

  internal fun showConsentDialog(activity: Activity, pf: SharedPreferences) {
    val cView = LayoutInflater.from(activity)
        .inflate(R.layout.analytics_consent, null)

    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(cView)

    val cbConsent = dialog.findViewById<AppCompatCheckBox>(R.id.cbConsent)
    val btSave = dialog.findViewById<Button>(R.id.btSave)

    btSave.setOnClickListener {
      pf.edit {
        putBoolean(PREF_ANALYTICS_CONSENT, true)
        cbConsent?.isChecked?.let {
          putBoolean(PREF_ANALYTICS_ENABLED, it)
          if (!it) {
            disableAnalytics()
          }
        }
      }
      dialog.dismiss()
    }

    dialog.setCanceledOnTouchOutside(false)
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
  }
}