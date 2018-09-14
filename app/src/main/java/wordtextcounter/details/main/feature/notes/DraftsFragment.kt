package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.ReportDatabase

class DraftsFragment : BaseFragment() {

  private lateinit var viewModelFactory: DraftsViewModelFactory

  private lateinit var viewModel: DraftsViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModelFactory = DraftsViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).draftDao())
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(DraftsViewModel::class.java)
  }

  override val baseViewModel: BaseViewModel
    get() = viewModel
}