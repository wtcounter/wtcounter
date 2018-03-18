package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.MutableLiveData
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.entities.Report

class NotesViewModel : BaseViewModel() {

  data class ViewState(val reports: List<Report>? = null)

  val viewState: MutableLiveData<ViewState> = MutableLiveData()

  init {
    viewState.value = ViewState()
  }

  fun getCurrentViewState() = viewState.value!!

  fun getAllSavedNotes() {
    viewState.value = getCurrentViewState().copy(reports = getDummyReports())
  }

  private fun getDummyReports(): List<Report> {
    val reports = mutableListOf<Report>()
    reports.add(Report(1, "Name 1", "Date", "Words", "Characters", "Paragraphs", "Sentences", 1521236243340))
    reports.add(Report(2, "Name 2", "Date", "Words", "Characters", "Paragraphs", "Sentences", 1521236243340))
    reports.add(Report(3, "Name 3", "Date", "Words", "Characters", "Paragraphs", "Sentences", 1521236243340))
    reports.add(Report(4, "Name 4", "Date", "Words", "Characters", "Paragraphs", "Sentences", 1521236243340))
    return reports
  }
}