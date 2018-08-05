package wordtextcounter.details.main.feature.input


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_extra_stats.*

import wordtextcounter.details.main.R
import wordtextcounter.details.main.store.entities.Stat


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

  private fun getStats(): List<Stat> {
    var stats = mutableListOf<Stat>()
    for (i in 0..10) {
      stats.add(Stat("Name of Stats ", i.toString()))
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
