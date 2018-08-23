package wordtextcounter.details.main.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsHelper {

  fun logAnalytics(context: Context) {
    val analyticsInstnce = FirebaseAnalytics.getInstance(context)
  }
}