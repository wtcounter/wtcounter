package com.example.rateus

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.content.Context.MODE_PRIVATE
import android.util.Log
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

  fun showRateUsDialog() {

  }

}