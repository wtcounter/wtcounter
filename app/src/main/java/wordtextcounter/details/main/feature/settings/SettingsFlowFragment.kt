package wordtextcounter.details.main.feature.settings

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.notes.NotesFragment
import wordtextcounter.details.main.feature.notes.NotesViewModel
import wordtextcounter.details.main.feature.notes.NotesViewModelFactory
import wordtextcounter.details.main.store.ReportDatabase

class SettingsFlowFragment: BaseFragment() {

  private lateinit var viewModelFactory: NotesViewModelFactory

  //TODO Replace this with Setting's ViewModel
  private lateinit var viewModel: NotesViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModelFactory = NotesViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).reportDao())
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(NotesViewModel::class.java)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_settings_flow, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    addChildFragment(SettingsFragment.newInstance(), R.id.settings_container)
  }

  //TODO Replace this with Setting's ViewModel
  override val baseViewModel: BaseViewModel
  get() = viewModel

  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFlowFragment.
     */
    fun newInstance(): SettingsFlowFragment {
      val fragment = SettingsFlowFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}