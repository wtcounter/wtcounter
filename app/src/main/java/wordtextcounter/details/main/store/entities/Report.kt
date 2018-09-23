package wordtextcounter.details.main.store.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import wordtextcounter.details.main.store.entities.Report.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class Report(
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "data_text") var dataText: String?,
    @ColumnInfo(name = "word") var words: String?,
    @ColumnInfo(name = "character") var characters: String?,
    @ColumnInfo(name = "paragraph") var paragraphs: String?,
    @ColumnInfo(name = "sentence") var sentences: String?,
    @ColumnInfo(name = "time_added") var time_added: Long?,
    @Deprecated(
        "Size will be removed in coming version as its value is now dependent on user prefs")
    @ColumnInfo(name = "size") var size: String?) {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Int? = null

  companion object {
    internal const val TABLE_NAME = "Details"
  }
}
