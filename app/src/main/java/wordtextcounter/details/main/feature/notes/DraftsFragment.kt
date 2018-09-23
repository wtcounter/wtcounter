package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_notes.rvNotes
import kotlinx.android.synthetic.main.fragment_notes.tvEmptyNotes
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.ReportDatabase

class DraftsFragment : BaseFragment() {

  private lateinit var viewModelFactory: DraftsViewModelFactory

  private lateinit var viewModel: DraftsViewModel

  private lateinit var adapter: DraftsAdapter

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
    rvNotes.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
    adapter = DraftsAdapter()
    rvNotes.adapter = adapter
    initClickListeners()
    viewModel.getAllDrafts()
    disposable.add(viewModel.viewState.subscribe({
      handleViewState(it)
    }, {
      it.printStackTrace()
    }))
  }

  private fun initClickListeners() {
    disposable.add(adapter.clickRelay.subscribe({
      when (it) {
        is DraftsAdapter.DraftActions.DraftEdit -> {
          logAnalytics(Click("draft_edit"))
          viewModel.editDraft(it.draft)
        }
        is DraftsAdapter.DraftActions.DraftDelete -> {
          logAnalytics(Click("draft_delete"))
          //TODO Think about generalizing this kind of dialogs. They are spreaded at three places right now.
          AlertDialog.Builder(context!!)
              .setTitle(R.string.edit_alert_title)
              .setMessage(R.string.draft_delete)
              .setPositiveButton(
                  R.string.yes
              ) { dialog, _ ->
                viewModel.deleteDraft(it.draft)
                dialog.dismiss()
              }
              .setNegativeButton(
                  R.string.no
              ) { dialog, _ ->
                dialog.dismiss()
              }
              .setIcon(R.drawable.ic_warning_black_24dp)
              .create()
              .show()
        }
        is DraftsAdapter.DraftActions.DraftHistoryEdit -> {
          logAnalytics(Click("draft_history_edit"))
          viewModel.editDraftHistory(it.draftHistory, it.parentDraft)
        }
        is DraftsAdapter.DraftActions.DraftHistoryDelete -> {
          logAnalytics(Click("draft_history_delete"))
          AlertDialog.Builder(context!!)
              .setTitle(R.string.edit_alert_title)
              .setMessage(R.string.draft_history_delete)
              .setPositiveButton(
                  R.string.yes
              ) { dialog, _ ->
                viewModel.deleteDraftHistory(it.draftHistory)
                dialog.dismiss()
              }
              .setNegativeButton(
                  R.string.no
              ) { dialog, _ ->
                dialog.dismiss()
              }
              .setIcon(R.drawable.ic_warning_black_24dp)
              .create()
              .show()
        }
      }
    }, {
      showError(getString(R.string.generic_error_message))
    }))
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
      adapter.dispatchUpdates(state.drafts)
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
     * @return A new instance of fragment DraftsFragment.
     */
    fun newInstance(): DraftsFragment {
      val fragment = DraftsFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}