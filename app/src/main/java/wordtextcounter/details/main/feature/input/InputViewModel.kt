package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.MutableLiveData
import wordtextcounter.details.main.feature.base.BaseViewModel

/**
 * Created by hirak on 03/12/17.
 */
class InputViewModel : BaseViewModel() {

  data class ViewState(val showKeyboard: Boolean = false,

      val showReport: Boolean = false,
      val noOfChars: Int = 0)

  val viewState: MutableLiveData<ViewState> = MutableLiveData()

  init {
    viewState.value = ViewState()
  }

  private fun currentViewState(): ViewState = viewState.value!!

  fun confirmClicked(input: String) {
    viewState.value = currentViewState().copy(showReport = true, noOfChars = input.length)
  }
}