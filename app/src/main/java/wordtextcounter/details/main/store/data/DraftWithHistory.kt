package wordtextcounter.details.main.store.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory

class DraftWithHistory {
  @Embedded
  lateinit var draft: Draft

  @Relation(
      parentColumn = "id", entityColumn = "draftId",
      entity = DraftHistory::class
  )
  lateinit var draftHistories: List<DraftHistory>
}