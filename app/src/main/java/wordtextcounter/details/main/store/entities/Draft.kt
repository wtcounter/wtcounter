package wordtextcounter.details.main.store.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = Draft.TABLE_NAME)
data class Draft(
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "createdAt") var createdAt: Long) {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Int? = null

  companion object {
    internal const val TABLE_NAME = "Draft"
  }
}