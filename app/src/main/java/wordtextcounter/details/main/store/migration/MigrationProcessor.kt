package wordtextcounter.details.main.store.migration

import android.content.Context
import android.content.SharedPreferences
import androidx.content.edit
import wordtextcounter.details.main.store.ReportDatabase.Companion.DB_VERSION
import wordtextcounter.details.main.util.Constants.PREF_DB_STORED
import kotlin.reflect.full.functions

object MigrationProcessor {
  fun process(context: Context, preferences: SharedPreferences, storedVersion: Int) {
    val migrations = Migrations(context)
    val functions = Migrations::class.functions
    for (function in functions) {
      val annots = function.annotations
      for (annot in annots) {
        if (annot is Migration && storedVersion < annot.from) {
          function.call(migrations)
          break
        }
      }
    }
    preferences.edit {
      putString(PREF_DB_STORED, DB_VERSION.toString())
    }
  }
}