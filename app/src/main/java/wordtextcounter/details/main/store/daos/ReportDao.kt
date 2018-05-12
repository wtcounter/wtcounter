package wordtextcounter.details.main.store.daos

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable
import wordtextcounter.details.main.store.entities.Report

@Dao
interface ReportDao {
  @Query("SELECT * FROM Details ORDER BY time_added DESC")
  fun getAllReports(): Flowable<List<Report>>

  @Insert(onConflict = REPLACE)
  fun saveReport(report: Report)

  @Update
  fun updateReports(reports: List<Report>)

  @Update
  fun updateReport(report: Report)
  
  @Delete
  fun deleteReport(report: Report)
}