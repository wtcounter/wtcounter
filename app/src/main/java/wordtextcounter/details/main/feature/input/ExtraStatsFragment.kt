package wordtextcounter.details.main.feature.input


import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_extra_stats.*
import wordtextcounter.details.main.R


/**
 * A simple [Fragment] subclass.
 * Use the [ExtraStatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ExtraStatsFragment : DialogFragment() {

  private lateinit var adapter: ExtraStatsAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapter = ExtraStatsAdapter()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_extra_stats, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rvStats.adapter = adapter
    adapter.setStats(getStats())
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

  private fun getStats(): List<ExtraStatGroup> {
    val stats = mutableListOf<ExtraStatGroup>()
    for (i in 0..5) {
      val extraStats = mutableListOf<ExtraStat>()
      for (j in 0..10) {
        extraStats.add(ExtraStat("Extra stat ", ((i * 100) + j).toString()))
      }
      stats.add(ExtraStatGroup("Extra Stat Group $i", extraStats))
    }
    return stats
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
