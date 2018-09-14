package wordtextcounter.details.main.store.entities

import android.arch.persistence.room.*

@Entity(tableName = DraftHistory.TABLE_NAME
    , foreignKeys = [(ForeignKey(entity = Draft::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("draftId"),
    onDelete = ForeignKey.CASCADE))], indices = [(Index(value = ["draftId"]))])
data class DraftHistory(
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "createdAt") var createdAt: Long ,
    @ColumnInfo(name = "draftId") val draftId: Long) {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Long? = null

  companion object {
    internal const val TABLE_NAME = "DraftHistory"
  }
}
