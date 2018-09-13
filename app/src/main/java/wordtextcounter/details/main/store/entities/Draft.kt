package wordtextcounter.details.main.store.entities

import android.arch.persistence.room.Entity

@Entity(tableName = Draft.TABLE_NAME)
class Draft {

  companion object {
    internal const val TABLE_NAME = "Draft"
  }
}