package wordtextcounter.details.main.store.migration

import android.content.Context
import io.reactivex.schedulers.Schedulers.io
import wordtextcounter.details.main.store.ReportDatabase
import wordtextcounter.details.main.store.entities.Report
import wordtextcounter.details.main.util.Helper

class Migrations(val context: Context) {
  
  @Migration(1, 2)
  fun initialMigration() {
    val db = ReportDatabase.getInstance(context)
    val dao = db.reportDao()
    dao.getAllReports()
        .subscribeOn(io())
        .flatMapIterable { t -> t }
        .map { t ->
          t.size = t.dateText?.let { Helper.calculateSize(it) }
          return@map t
        }
        .toList()
        .subscribe { t: MutableList<Report>? ->
           dao.updateReports(ArrayList(t))
        }
  }
}