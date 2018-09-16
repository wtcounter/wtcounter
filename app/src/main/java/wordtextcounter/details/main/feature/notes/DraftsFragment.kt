package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_notes.rvNotes
import kotlinx.android.synthetic.main.fragment_notes.tvEmptyNotes
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.ReportDatabase

class DraftsFragment : BaseFragment() {

  private lateinit var viewModelFactory: DraftsViewModelFactory

  private lateinit var viewModel: DraftsViewModel

  private var adapter: DraftsAdapter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModelFactory = DraftsViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).draftDao()
    )
    viewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(DraftsViewModel::class.java)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_notes, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getAllDrafts()
    disposable.add(viewModel.viewState.subscribe({
      handleViewState(it)
    }, {
      it.printStackTrace()
    }))
  }

  private fun initClickListeners() {
    adapter?.clickRelay?.subscribe({
      when (it) {
        is DraftsAdapter.DraftActions.DraftEdit -> {
          viewModel.editDraft(it.draft)
        }
        is DraftsAdapter.DraftActions.DraftDelete -> {
          viewModel.deleteDraft(it.draft)
        }
        is DraftsAdapter.DraftActions.DraftHistoryEdit -> {
          viewModel.editDraftHistory(it.draftHistory)
        }
        is DraftsAdapter.DraftActions.DraftHistoryDelete -> {
          viewModel.deleteDraftHistory(it.draftHistory)
        }
      }
    }, {
      showError(getString(R.string.generic_error_message))
    })
        ?.let { disposable.add(it) }
  }

  private fun handleViewState(state: DraftsViewModel.ViewState) {
    if (state.noReports) {
      tvEmptyNotes.visibility = View.VISIBLE
      rvNotes.visibility = View.GONE
    } else {
      tvEmptyNotes.visibility = View.GONE
      rvNotes.visibility = View.VISIBLE
    }

    if (state.drafts != null) {
      if (adapter == null) {
        rvNotes.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        adapter = DraftsAdapter(state.drafts.toMutableList())
        rvNotes.adapter = adapter
        initClickListeners()
      } else {
        adapter?.dispatchUpdates(state.drafts)
      }
    }

    if (state.successDraftDeletion) {
      showError(getString(R.string.draft_deletion_success))
    }

    if (state.successHistoryDeletion) {
      showError(getString(R.string.draft_history_deletion_success))
    }

    if (state.showError) {
      showError(getString(R.string.generic_error_message))
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
    fun newInstance(): DraftsFragment {
      val fragment = DraftsFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}