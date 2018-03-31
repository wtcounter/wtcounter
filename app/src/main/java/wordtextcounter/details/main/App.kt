package wordtextcounter.details.main

import android.app.Application
import androidx.content.edit
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import wordtextcounter.details.main.store.ReportDatabase.Companion.DB_NAME
import wordtextcounter.details.main.store.ReportDatabase.Companion.DB_VERSION
import wordtextcounter.details.main.store.migration.MigrationProcessor
import wordtextcounter.details.main.util.Constants.PREF_DB_STORED
import wordtextcounter.details.main.util.dbExists

class App : Application() {
  
  override fun onCreate() {
    super.onCreate()
    val formatStrategy = PrettyFormatStrategy.newBuilder()
        .methodCount(5)         // (Optional) How many method line to show. Default 2
        .tag(
            BuildConfig.APPLICATION_ID)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
        .build()
    
    Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
      override fun isLoggable(priority: Int, tag: String?): Boolean {
        return BuildConfig.DEBUG
      }
    })
    
    val preferences = getPreference()
    val storedVersion = preferences.getString(PREF_DB_STORED, "0")
    if (storedVersion.toInt() < DB_VERSION && dbExists(this, DB_NAME)) {
      MigrationProcessor.process(this, storedVersion.toInt())
      preferences.edit {
        putString(PREF_DB_STORED, DB_VERSION.toString())
      }
    }
  }
}
