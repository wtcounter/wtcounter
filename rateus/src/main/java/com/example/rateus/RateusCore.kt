package com.example.rateus

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.core.content.edit

object RateusCore {
  private const val RATE_PREFERENCE = "rateus.core.preference"
  private const val TIME_SPENT = "rateus.time_spent"

  fun init(app: Application, config: Config) {
    val ratePreference = app.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    val timeSpent = ratePreference.getLong(TIME_SPENT, 0L)
    var sessionStartTime = 0L

    app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
      override fun onActivityPaused(p0: Activity?) {
      }

      override fun onActivityResumed(p0: Activity?) {
      }

      override fun onActivityStarted(p0: Activity?) {
        sessionStartTime = System.currentTimeMillis()
      }

      override fun onActivityDestroyed(p0: Activity?) {
      }

      override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
      }

      override fun onActivityStopped(p0: Activity?) {
        ratePreference.edit {
          Log.d("onActivityStopped", ""+timeSpent)
          Log.d("onActivityStopped", "time spent " + ((System.currentTimeMillis() - sessionStartTime) + timeSpent))
          //putLong(TIME_SPENT, (System.currentTimeMillis() - sessionStartTime) + timeSpent)
        }
      }

      override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
      }

    })
  }

  fun showRateUsDialog(activity: Activity) {
    val cView = LayoutInflater.from(activity)
        .inflate(R.layout.rate_us_dialog, null)

    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(cView)
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
  }

}