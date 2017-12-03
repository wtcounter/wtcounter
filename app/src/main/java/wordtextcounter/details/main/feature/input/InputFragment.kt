package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED
import wordtextcounter.details.main.MainActivity.ToolbarTitle
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.input.InputViewModel.ReportState
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import wordtextcounter.details.main.util.RxBus

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : BaseFragment() {

  lateinit var viewModel: InputViewModel

  @BindView(R.id.etInput) lateinit var etInput: TextInputEditText
  @BindView(R.id.slidingUpPanelLayout) lateinit var slidingUpPanelLayout: SlidingUpPanelLayout

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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    RxBus.instance.send(ToolbarTitle(R.string.title_input))
    slidingUpPanelLayout.panelHeight = 0
    slidingUpPanelLayout.panelState = EXPANDED
    viewModel.viewState.observe(this, Observer {
      it?.let { it1 -> handleViewState(it1) }
    })

    viewModel.reportState.observe(this, Observer {
      it?.let { it1 -> handleReportState(it1) }
    })
  }

  private fun handleViewState(viewState: ViewState) {
    if (viewState.showKeyboard) {
      etInput.requestFocus()
    } else {
      hideSoftKeyboard()
    }

    Handler().postDelayed(Runnable {
      if (viewState.showReport) {
        slidingUpPanelLayout.panelState = EXPANDED
      } else {
        slidingUpPanelLayout.panelState = COLLAPSED
      }
    }, if (viewState.showKeyboardDelay) 500 else 0)
  }

  private fun handleReportState(reportState: ReportState) {
    RxBus.instance.send(reportState)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.confirm -> {
        viewModel.confirmClicked(etInput.text.toString())
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }


  override fun onDetach() {
    super.onDetach()
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_fragment_input, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }


  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputFragment.
     */
    fun newInstance(): InputFragment {
      val fragment = InputFragment()
      return fragment
    }
  }
}// Required empty public constructor
