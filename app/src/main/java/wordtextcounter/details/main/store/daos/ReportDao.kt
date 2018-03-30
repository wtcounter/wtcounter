package wordtextcounter.details.main.store.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable
import wordtextcounter.details.main.store.entities.Report

@Dao interface ReportDao {
  @Query("SELECT * FROM Details")
  fun getAllReports(): Flowable<List<Report>>
  
  @Update
  fun updateReports(reports: List<Report>)
}