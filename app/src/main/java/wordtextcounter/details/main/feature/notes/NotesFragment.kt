package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_notes.rvNotes
import wordtextcounter.details.main.R
import wordtextcounter.details.main.R.string
import wordtextcounter.details.main.feature.main.ToolbarTitle
import wordtextcounter.details.main.feature.notes.NotesViewModel.ViewState
import wordtextcounter.details.main.util.RxBus

/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {

  lateinit var viewModel: NotesViewModel

  lateinit var notesAdapter: NotesAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    Logger.d("onCreate")
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this)
        .get(NotesViewModel::class.java)
    notesAdapter = NotesAdapter()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    Logger.d("onCreateView")
    return inflater.inflate(R.layout.fragment_notes, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    Logger.d("onViewCreated")

    RxBus.instance.send(ToolbarTitle(string.title_notes))

    rvNotes.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
    rvNotes.adapter = notesAdapter
    viewModel.viewState.observe(this, Observer {
      it?.let { it1 -> handleViewState(it1) }
    })

    viewModel.getAllSavedNotes()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    Logger.d("onSaveInstanceState")
  }

  override fun onDestroyView() {
    super.onDestroyView()
    Logger.d("onDestroyView")
  }

  override fun onDestroy() {
    super.onDestroy()
    Logger.d("onDestroy")
  }

  private fun handleViewState(viewState: ViewState) {
    if (viewState.reports != null) {
      notesAdapter.setReports(viewState.reports)
    }
  }

  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotesFragment.
     */
    fun newInstance(): NotesFragment {
      val fragment = NotesFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}// Required empty public constructor
