package wordtextcounter.details.main.store.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = DraftHistory.TABLE_NAME
    , foreignKeys = [(ForeignKey(
    entity = Draft::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("draftId"),
    onDelete = ForeignKey.CASCADE
))], indices = [(Index(value = ["draftId"]))]
)
data class DraftHistory(
  @ColumnInfo(name = "text") var text: String,
  @ColumnInfo(name = "createdAt") var createdAt: Long,
  @ColumnInfo(name = "draftId") val draftId: Long
) {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Long = 0

  companion object {
    internal const val TABLE_NAME = "DraftHistory"
  }
}
