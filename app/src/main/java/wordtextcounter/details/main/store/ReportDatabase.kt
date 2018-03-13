package wordtextcounter.details.main.store

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.entities.Report

@Database(entities = [(Report::class)], version = 2)
abstract class ReportDatabase : RoomDatabase() {
  
  abstract fun reportDao(): ReportDao
  
  companion object {
    private var INSTANCE: ReportDatabase? = null
    
    fun getInstance(context: Context): ReportDatabase? {
      if (INSTANCE == null) {
        synchronized(ReportDatabase::class) {
          INSTANCE = Room.databaseBuilder(context.applicationContext,
              ReportDatabase::class.java, "Counter.db")
              .addMigrations(object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                  // just a dummy migration to get room going.
                  //no-op
                }
              }).build()
        }
      }
      return INSTANCE
    }
  }
}