package wordtextcounter.details.main.store.migration

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers.io
import wordtextcounter.details.main.store.ReportDatabase

class Migrations(val context: Context) {

  @Migration(1, 2)
  fun migratefrom1to2() {
    val db = ReportDatabase.getInstance(context)
    val dao = db.reportDao()
    dao.getAllReports()
        .subscribeOn(io())
        .flatMap { t ->
          return@flatMap Flowable.fromIterable(t)
              .map { r ->
                r.size = "0b"
                return@map r
              }
              .toList()
              .toFlowable()
        }
        .take(1)
        .subscribe { t ->
          dao.updateReports(ArrayList(t))
        }
  }
}