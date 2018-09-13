package wordtextcounter.details.main.store.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory

@Dao
interface DraftDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveDraft(draft: Draft) : Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveDraftHistory(draftHistory: DraftHistory)
}