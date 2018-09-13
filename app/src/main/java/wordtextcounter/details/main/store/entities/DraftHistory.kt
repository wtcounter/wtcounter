package wordtextcounter.details.main.store.entities

import android.arch.persistence.room.Entity

@Entity(tableName = DraftHistory.TABLE_NAME)
class DraftHistory {

  companion object {
    internal const val TABLE_NAME = "DraftHistory"
  }
}
