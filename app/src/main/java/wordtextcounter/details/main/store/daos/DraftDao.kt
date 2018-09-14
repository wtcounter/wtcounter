package wordtextcounter.details.main.store.daos

import android.arch.persistence.room.*
import io.reactivex.Flowable
import wordtextcounter.details.main.store.data.DraftWithHistory
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory

@Dao
interface DraftDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveDraft(draft: Draft) : Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveDraftHistory(draftHistory: DraftHistory)

  @Transaction
  @Query("SELECT * FROM Draft")
  fun getAllDrafts(): Flowable<List<DraftWithHistory>>
}