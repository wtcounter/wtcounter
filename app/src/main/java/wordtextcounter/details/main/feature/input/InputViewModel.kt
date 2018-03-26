package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.MutableLiveData
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.util.Helper

class InputViewModel : BaseViewModel() {

    data class ViewState(
            val showError: Boolean = false,
            val errorMessage: String = "",
            val noOfWords: String = "0",
            val noOfCharacters: String = "0",
            val noOfSentences: String = "0",
            val reportText: String = ""
    )

    data class Report(
            val value: String = "0",
            val reportType: ReportType
    )

    data class ReportState(val list: List<Report>)

    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    init {
        viewState.value = ViewState()
    }

    private fun currentViewState(): ViewState = viewState.value!!

    fun onClickConfirm(input: String) {

        if (input.trim().isEmpty()) {
            viewState.value = ViewState()
            return
        }

        viewState.value = currentViewState().copy(reportText = input,
                noOfWords = Helper.countWords(input).toString(),
                noOfCharacters = input.length.toString(),
                noOfSentences = Helper.countSentences(input).toString())

    }

    fun onClickSaveCurrent() {

    }
}