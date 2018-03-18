package wordtextcounter.details.main.feature.input

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED
import kotlinx.android.synthetic.main.fragment_input.etInput
import kotlinx.android.synthetic.main.fragment_input.slidingUpPanelLayout
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import wordtextcounter.details.main.feature.main.MainActivity.ToolbarTitle
import wordtextcounter.details.main.util.RxBus

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : BaseFragment() {

  lateinit var viewModel: InputViewModel
  
  private val TEXT = "TEXT"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)

    viewModel = ViewModelProviders.of(this).get(InputViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_input, container, false)
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    etInput.setText(savedInstanceState?.getString(TEXT))
    etInput.setOnTouchListener { _, _ ->
      viewModel.onStartEdit()
      false
    }

    etInput.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> true }

    RxBus.instance.send(ToolbarTitle(R.string.title_input))
    slidingUpPanelLayout.panelHeight = 0
    viewModel.viewState.observe(this, Observer {
      it?.let { it1 -> handleViewState(it1) }
    })


  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(TEXT, etInput.text.toString())
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_fragment_input, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  private fun handleViewState(viewState: ViewState) {

    if (viewState.showError) {
      showError(viewState.errorMessage)
    }

    if (viewState.showKeyboard) {
      showSoftKeyboard(etInput)
    } else {
      hideSoftKeyboard()
    }

    Handler().postDelayed(Runnable {
      if (viewState.showReport)
        slidingUpPanelLayout.panelState = EXPANDED
      else
        slidingUpPanelLayout.panelState = COLLAPSED
    }, if (viewState.showKeyboardDelay) 500 else 0)
  }


  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.confirm -> {
        viewModel.onClickConfirm(etInput.text.toString())
        true
      }
      R.id.save -> {
        viewModel.onClickSaveCurrent()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputFragment.
     */
    fun newInstance(): InputFragment {
      return InputFragment()
    }
  }
}// Required empty public constructor
