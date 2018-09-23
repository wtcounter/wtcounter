package com.example.rateus

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import java.util.concurrent.TimeUnit

object RateusCore {
  private const val RATE_PREFERENCE = "rateus.core.preference"
  private const val TIME_SPENT = "rateus.time_spent"
  private const val INTERACTION = "rateus.interaction"
  private const val REMIND_LATER_TIME = "rateus.remindlater.time"

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

  private fun isOver24Hours(time: Long) = TimeUnit.DAYS.toMillis(1) < (System.currentTimeMillis() - time)

  fun shouldShowRateUsDialog(activity: Activity) : Boolean {
    val ratePreference = activity.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    val remindLaterTime = ratePreference.getLong(REMIND_LATER_TIME, 0)
    if (remindLaterTime > 0) {
      if (!isOver24Hours(remindLaterTime)) {
        //user has selected remide me later option less than 24 hours ago.
        return false
      } else {
        ratePreference.edit {
          putLong(REMIND_LATER_TIME, 0)
        }
      }
    }
    val interacted = ratePreference.getBoolean(INTERACTION, false)
    val timeSpent = ratePreference.getLong(TIME_SPENT, 0L)
    return timeSpent > TimeUnit.MINUTES.toMillis(5) && !interacted
  }

  private fun markInteractionDone(activity: Activity) {
    val ratePreference = activity.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    ratePreference.edit {
      putBoolean(RateusCore.INTERACTION, true)
    }
  }

  fun onYesSelected(activity: Activity) {
    markInteractionDone(activity)
  }

  fun onRemindLaterSelected(activity: Activity) {
    val ratePreference = activity.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    ratePreference.edit {
      putLong(RateusCore.REMIND_LATER_TIME, System.currentTimeMillis())
    }
  }

  fun onNoSelected(activity: Activity) {
    markInteractionDone(activity)
  }

}