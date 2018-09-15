package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_notes.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.store.ReportDatabase

class DraftsFragment : BaseFragment() {

  private lateinit var viewModelFactory: DraftsViewModelFactory

  private lateinit var viewModel: DraftsViewModel

  private var adapter: DraftsAdapter?= null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModelFactory = DraftsViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).draftDao())
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(DraftsViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_notes, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getAllDrafts()
    viewModel.viewState.subscribe({
      rvNotes.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
      if (adapter == null && it?.drafts != null) {
        Logger.d("initng adapter with " + it.drafts.size)
        adapter = DraftsAdapter(it.drafts)
        rvNotes.adapter = adapter
      }
    }, {
      it.printStackTrace()
    })

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