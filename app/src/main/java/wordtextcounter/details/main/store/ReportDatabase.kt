package wordtextcounter.details.main.store

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import wordtextcounter.details.main.store.ReportDatabase.Companion.DB_VERSION
import wordtextcounter.details.main.store.daos.DraftDao
import wordtextcounter.details.main.store.daos.ReportDao
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory
import wordtextcounter.details.main.store.entities.Report
import wordtextcounter.details.main.store.entities.Report.Companion.TABLE_NAME

@Database(entities = [(Report::class), (Draft::class), (DraftHistory::class)], version = DB_VERSION)
abstract class ReportDatabase : RoomDatabase() {
  
  abstract fun reportDao(): ReportDao
  abstract fun draftDao(): DraftDao
  
  companion object {
    const val DB_VERSION = 3
    const val DB_NAME = "Counter.db"
    private var INSTANCE: ReportDatabase? = null
    
    @Suppress("SENSELESS_COMPARISON")
    fun getInstance(context: Context): ReportDatabase {
      if (INSTANCE == null) {
        synchronized(ReportDatabase::class) {
          INSTANCE = Room.databaseBuilder(context.applicationContext,
              ReportDatabase::class.java, DB_NAME)
              .addMigrations(object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                  // 1 to 2
                  database.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN 'time_added' INTEGER;")
                  database.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN 'size' TEXT;")
                }
                
              }, object : Migration(2 ,3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                  // 2 to 3
                  database.execSQL("CREATE TABLE IF NOT EXISTS `Draft` (`id` INTEGER, `text` TEXT NOT NULL," +
                      " `createdAt` INTEGER NOT NULL, `lastUpdatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
                  database.execSQL("CREATE TABLE IF NOT EXISTS `DraftHistory` (`id` INTEGER, `text` TEXT NOT NULL," +
                      " `createdAt` INTEGER NOT NULL, `draftId` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`draftId`) REFERENCES Draft(`id`))")
                }
              }).build()
        }
      }
      return INSTANCE!!
    }
  }
}