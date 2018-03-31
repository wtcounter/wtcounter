package wordtextcounter.details.main.store.migration

import android.content.Context

object MigrationProcessor {
  fun process(context: Context, storedVersion: Int) {
    val migrations = Migrations(context)
    //version 2 migration
    if (storedVersion < 2) {
      migrations.migratefrom1to2()
    }
  }
}