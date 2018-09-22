package wordtextcounter.details.main.feature.extrastats


import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_extra_stats.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.util.ExtraStatText
import wordtextcounter.details.main.util.NoEvent
import wordtextcounter.details.main.util.RxBus


/**
 * A simple [Fragment] subclass.
 * Use the [ExtraStatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ExtraStatsFragment : DialogFragment() {

  private lateinit var adapter: ExtraStatsAdapter
  private lateinit var viewModel: ExtraStatsViewModel
  private var disposable: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapter = ExtraStatsAdapter()
    viewModel = ViewModelProviders.of(this)
        .get(ExtraStatsViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_extra_stats, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rvStats.adapter = adapter
    
    adapter.expandLess = ContextCompat.getDrawable(context!!,
        R.drawable.ic_expand_less_black_24dp)!!
    adapter.expandMore = ContextCompat.getDrawable(context!!,
        R.drawable.ic_expand_more_black_24dp)!!

    viewModel.viewState.subscribe {
      adapter.setStats(it.extraStatGroups)
    }
    disposable = RxBus.subscribe(ExtraStatText::class.java, Consumer {
      RxBus.send(NoEvent)
      viewModel.getAllStats(it.text)
    })
  }

  override fun onDestroyView() {
    disposable?.dispose()
    super.onDestroyView()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
    return dialog
  }

  override fun onStart() {
    super.onStart()
    dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT)
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExtraStatsFragment.
     */
    @JvmStatic
    fun newInstance() =
        ExtraStatsFragment()
  }
}
