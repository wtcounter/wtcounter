package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.MutableLiveData
import wordtextcounter.details.main.feature.base.BaseViewModel

/**
 * Created by hirak on 03/12/17.
 */
class InputViewModel : BaseViewModel() {

  data class ViewState(val showKeyboard: Boolean = false,
      val showError: Boolean = false,
      val errorMessage: String = "")


  data class ReportState(
      val showReport: Boolean = false,
      val noOfChars: Int = 0,
      val noOfLines: Int = 0,
      val noOfParagraphs: Int = 0)

  val viewState: MutableLiveData<ViewState> = MutableLiveData()
  val reportState: MutableLiveData<ReportState> = MutableLiveData()


  init {
    viewState.value = ViewState()
    reportState.value = ReportState()
  }

  private fun currentViewState(): ViewState = viewState.value!!

  private fun currentReportState(): ReportState = reportState.value!!

  fun confirmClicked(input: String) {

    if (input.trim().isEmpty()) {
      viewState.value = currentViewState().copy(showError = true, errorMessage = "No input")
      return
    }
    viewState.value = currentViewState().copy(showKeyboard = false)
    reportState.value = currentReportState().copy(showReport = true, noOfChars = input.length)
  }
}