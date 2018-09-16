package wordtextcounter.details.main.store.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import wordtextcounter.details.main.store.data.DraftData

@Entity(tableName = Draft.TABLE_NAME)
data class Draft(
  @Embedded val draftData: DraftData, @ColumnInfo(
      name = "lastUpdatedAt"
  ) val lastUpdatedAt: Long
) {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Long? = null

  companion object {
    internal const val TABLE_NAME = "Draft"
  }
}