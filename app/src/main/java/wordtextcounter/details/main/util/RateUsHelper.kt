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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.rateus.RateusCore.onCancelSelected
import com.example.rateus.RateusCore.onNoSelected
import com.example.rateus.RateusCore.onRemindLaterSelected
import com.example.rateus.RateusCore.onYesSelected
import com.example.rateus.RateusCore.shouldShowRateUsDialog
import wordtextcounter.details.main.R

object RateUsHelper {
  fun showRateUsDialog(activity: Activity) {
    if (shouldShowRateUsDialog(activity)) {
      val cView = LayoutInflater.from(activity)
          .inflate(R.layout.rate_us_dialog, null)


      val dialog = Dialog(activity)
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
      dialog.setContentView(cView)
      dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      val btClose = dialog.findViewById<ImageView>(R.id.ivCross)
      val btYes = dialog.findViewById<Button>(R.id.btnSave)
      val btLater = dialog.findViewById<Button>(R.id.btnLater)
      val btNever = dialog.findViewById<Button>(R.id.btnNever)
      btClose.setOnClickListener {
        onCancelSelected(activity)
        dialog.dismiss()
      }

      btYes.setOnClickListener {
        onYesSelected(activity)
        try {
          val viewIntent = Intent(Intent.ACTION_VIEW,
              Uri.parse("market://details?id=wordtextcounter.details.main"))
          activity.startActivity(viewIntent)
        } catch (e: ActivityNotFoundException) {
          activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=wordtextcounter.details.main")))
        } catch (e : Exception) {
          Toast.makeText(activity, activity.getString(R.string.rate_us_error), Toast.LENGTH_LONG).show()
        }
      }

      btLater.setOnClickListener {
        onRemindLaterSelected(activity)
      }

      btNever.setOnClickListener {
        onNoSelected(activity)
      }

      dialog.setOnKeyListener { _, keyCode, _ ->
        if (keyCode == KEYCODE_BACK) {
          onNoSelected(activity)
          dialog.dismiss()
        }
        return@setOnKeyListener true
      }

      dialog.setCancelable(false)
      dialog.show()
    }
  }
}