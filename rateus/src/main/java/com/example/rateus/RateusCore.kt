package com.example.rateus

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit


object RateusCore {
  private const val RATE_PREFERENCE = "rateus.core.preference"
  private const val TIME_SPENT = "rateus.time_spent"
  private const val INTERACTION = "rateus.interaction"

  fun init(app: Application) {
    val ratePreference = app.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    var timeSpent = ratePreference.getLong(TIME_SPENT, 0L)
    var sessionStartTime = 0L

    app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
      override fun onActivityPaused(p0: Activity) {
        //no-op
      }

      override fun onActivityResumed(p0: Activity) {
        //no-op
      }

      override fun onActivityStarted(p0: Activity) {
        timeSpent = ratePreference.getLong(TIME_SPENT, 0L)
        sessionStartTime = System.currentTimeMillis()
      }

      override fun onActivityDestroyed(p0: Activity) {
        //no-op
      }

      override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle?) {
      }

      override fun onActivityStopped(p0: Activity) {
        ratePreference.edit {
          putLong(TIME_SPENT, (System.currentTimeMillis() - sessionStartTime) + timeSpent)
        }
      }

      override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        //no-op
      }

    })
  }

  fun shouldShowRateUsDialog(activity: Activity) : Boolean {
    val ratePreference = activity.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    val interacted = ratePreference.getBoolean(INTERACTION, false)
    val timeSpent = ratePreference.getLong(TIME_SPENT, 0L)
    return timeSpent > 180000 && !interacted
  }

  fun onCancelSelected(activity: Activity) {
    val ratePreference = activity.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    ratePreference.edit {
      putBoolean(RateusCore.INTERACTION, true)
    }
  }

  fun onYesSelected(activity: Activity) {
    val ratePreference = activity.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    ratePreference.edit {
      putBoolean(RateusCore.INTERACTION, true)
    }
  }
}