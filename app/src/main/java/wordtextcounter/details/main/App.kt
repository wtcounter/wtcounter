package wordtextcounter.details.main

import android.app.Application
import androidx.core.content.edit
import com.example.rateus.RateusCore
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import wordtextcounter.details.main.analytics.AnalyticsLogger
import wordtextcounter.details.main.store.ReportDatabase.Companion.DB_NAME
import wordtextcounter.details.main.store.ReportDatabase.Companion.DB_VERSION
import wordtextcounter.details.main.store.migration.MigrationProcessor
import wordtextcounter.details.main.util.Constants.PREF_DB_STORED
import wordtextcounter.details.main.util.dbExists
import wordtextcounter.details.main.util.extensions.getPreference

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    app = this
    val preferences = getPreference()
    RateusCore.init(this)
    AnalyticsLogger.init(this)
    val formatStrategy = PrettyFormatStrategy.newBuilder()
        .methodCount(7)
        .tag(BuildConfig.APPLICATION_ID)
        .build()
    Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
      override fun isLoggable(priority: Int, tag: String?): Boolean {
        return BuildConfig.DEBUG
      }
    })

    val storedVersion = preferences.getString(PREF_DB_STORED, "0")
    if (storedVersion.toInt() < DB_VERSION && dbExists(this, DB_NAME)) {
      MigrationProcessor.process(this, storedVersion.toInt())
      preferences.edit {
        putString(PREF_DB_STORED, DB_VERSION.toString())
      }
    }
  }

  companion object {
    lateinit var app: App

    @JvmStatic
    fun getAppInstance() = app
  }
}
