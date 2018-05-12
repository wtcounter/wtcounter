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
import kotlinx.android.synthetic.main.fragment_notes.rvNotes
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.notes.NotesViewModel.ViewState
import wordtextcounter.details.main.store.ReportDatabase

/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : BaseFragment() {

  private lateinit var viewModelFactory: NotesViewModelFactory

  private lateinit var viewModel: NotesViewModel

  private lateinit var notesAdapter: NotesAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModelFactory = NotesViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).reportDao())
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(NotesViewModel::class.java)
    notesAdapter = NotesAdapter()
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_notes, container, false)
  }

  override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    rvNotes.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

    disposable.add(notesAdapter.clickRelay.subscribe {
      when (it) {
        is Edit -> {
          viewModel.editReport(it.position)
        }
        is Delete -> {
          viewModel.deleteReport(it.position)
        }
      }
    })
    rvNotes.adapter = notesAdapter
    viewModel.viewState.observe(this, Observer {
      it?.let { it1 -> handleViewState(it1) }
    })

    viewModel.getAllSavedNotes()
  }


  private fun handleViewState(viewState: ViewState) {
    if (viewState.reports != null) {
      notesAdapter.setReports(viewState.reports)
    }
  }

  override val baseViewModel: BaseViewModel
    get() = viewModel

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
