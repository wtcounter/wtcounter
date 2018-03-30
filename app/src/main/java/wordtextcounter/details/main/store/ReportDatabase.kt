package wordtextcounter.details.main.store

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import wordtextcounter.details.main.store.ReportDatabase.Companion.DB_VERSION
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.entities.Report
import wordtextcounter.details.main.store.entities.Report.Companion.TABLE_NAME

@Database(entities = [(Report::class)], version = DB_VERSION)
abstract class ReportDatabase : RoomDatabase() {
  
  abstract fun reportDao(): ReportDao
  
  companion object {
    const val DB_VERSION = 2
    private lateinit var INSTANCE: ReportDatabase
    
    @Suppress("SENSELESS_COMPARISON")
    fun getInstance(context: Context): ReportDatabase {
      if (INSTANCE == null) {
        synchronized(ReportDatabase::class) {
          INSTANCE = Room.databaseBuilder(context.applicationContext,
              ReportDatabase::class.java, "Counter.db")
              .addMigrations(object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                  // 1 to 2
                  database.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN 'time_added' INTEGER;")
                  database.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN 'size' TEXT;")
                }
              }).build()
        }
      }
      return INSTANCE
    }
  }
}