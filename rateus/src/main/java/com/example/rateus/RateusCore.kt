package com.example.rateus

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.edit
import android.widget.Toast


object RateusCore {
  private const val RATE_PREFERENCE = "rateus.core.preference"
  private const val TIME_SPENT = "rateus.time_spent"
  private const val INTERACTION = "rateus.interaction"

  fun init(app: Application) {
    val ratePreference = app.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
    var timeSpent = ratePreference.getLong(TIME_SPENT, 0L)
    var sessionStartTime = 0L

    app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
      override fun onActivityPaused(p0: Activity?) {
        //no-op
      }

      override fun onActivityResumed(p0: Activity?) {
        //no-op
      }

      override fun onActivityStarted(p0: Activity?) {
        timeSpent = ratePreference.getLong(TIME_SPENT, 0L)
        sessionStartTime = System.currentTimeMillis()
      }

      override fun onActivityDestroyed(p0: Activity?) {
        //no-op
      }

      override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
      }

      override fun onActivityStopped(p0: Activity?) {
        ratePreference.edit {
          putLong(TIME_SPENT, (System.currentTimeMillis() - sessionStartTime) + timeSpent)
        }
      }

      override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        //no-op
      }

    })
  }

  fun showRateUsDialog(activity: Activity) {
    try {
      val ratePreference = activity.getSharedPreferences(RATE_PREFERENCE, MODE_PRIVATE)
      val interacted = ratePreference.getBoolean(INTERACTION, false)
      val timeSpent = ratePreference.getLong(TIME_SPENT, 0L)
      if (timeSpent > 180000 && !interacted) {
        val cView = LayoutInflater.from(activity)
            .inflate(R.layout.rate_us_dialog, null)


        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(cView)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btClose = dialog.findViewById<ImageView>(R.id.ivCross)
        val btYes = dialog.findViewById<Button>(R.id.btnSave)
        btClose.setOnClickListener {
          ratePreference.edit {
            putBoolean(INTERACTION, true)
          }
          dialog.dismiss()
        }

        btYes.setOnClickListener {
          try {
            ratePreference.edit {
              putBoolean(INTERACTION, true)
            }
            val viewIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=wordtextcounter.details.main"))
            activity.startActivity(viewIntent)
          } catch (e: Exception) {
            Toast.makeText(activity, "Oops! We were not able to reach play store. Sorry for that!", Toast.LENGTH_LONG).show()
          }
        }

        dialog.show()
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

}