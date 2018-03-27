package wordtextcounter.details.main.store.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import wordtextcounter.details.main.store.entities.Report

@Dao interface ReportDao {
  @Query("SELECT * FROM Details")
  fun getAllReports(): Flowable<List<Report>>
  
  @Insert(onConflict = REPLACE)
  fun saveReport(report: Report)
}