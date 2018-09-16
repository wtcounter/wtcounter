package wordtextcounter.details.main.store.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import android.arch.persistence.room.Update
import io.reactivex.Flowable
import wordtextcounter.details.main.store.data.DraftWithHistory
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory

@Dao
interface DraftDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveDraft(draft: Draft): Long

  @Update
  fun updateDraft(draft: Draft)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveDraftHistory(draftHistory: DraftHistory)

  @Transaction
  @Query("SELECT * FROM Draft")
  fun getAllDrafts(): Flowable<List<DraftWithHistory>>

  @Delete
  fun deleteDraft(draft: Draft)

  @Delete
  fun deleteDraftHisoty(draftHistory: DraftHistory)
}