package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.MutableLiveData
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.input.ReportType.CHARS
import wordtextcounter.details.main.feature.input.ReportType.PARAGRAPHS
import wordtextcounter.details.main.feature.input.ReportType.SIZE
import wordtextcounter.details.main.feature.input.ReportType.WORDS
import wordtextcounter.details.main.util.Helper

/**
 * Created by hirak on 03/12/17.
 */
class InputViewModel : BaseViewModel() {

  data class ViewState(val showKeyboard: Boolean = true,
      val showError: Boolean = false,
      val showReport: Boolean = false,
      val showKeyboardDelay: Boolean = false,
      val errorMessage: String = "")


  data class Report(
      val value: String = "0",
      val reportType: ReportType
  )

  data class ReportState(val list: List<Report>)

  val viewState: MutableLiveData<ViewState> = MutableLiveData()
  val reportState: MutableLiveData<ReportState> = MutableLiveData()


  init {
    viewState.value = ViewState()
  }

  private fun currentViewState(): ViewState = viewState.value!!


  fun onClickConfirm(input: String) {

    if (input.trim().isEmpty()) {
      viewState.value = currentViewState().copy(showError = true, errorMessage = "No input")
      return
    }

    viewState.value = currentViewState().copy(showKeyboard = false, showReport = true,
        showKeyboardDelay = currentViewState().showKeyboard, showError = false, errorMessage = "")

    val reports = mutableListOf<Report>()
    reports.add(Report(input.length.toString(), CHARS))
    reports.add(Report(Helper.countWords(input).toString(), WORDS))
    reports.add(Report(Helper.countParagraphs(input).toString(), PARAGRAPHS))
    reports.add(Report(Helper.calculateSize(input), SIZE))
    reportState.value = ReportState(reports)

  }

  fun onStartEdit() {
    if (!currentViewState().showKeyboard)
      viewState.value = currentViewState().copy(showKeyboard = true, showReport = false,
          showKeyboardDelay = true)
  }

}